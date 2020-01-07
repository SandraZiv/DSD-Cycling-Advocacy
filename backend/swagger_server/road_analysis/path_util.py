import math
import json
import matplotlib.pyplot as plt
from shapely.geometry import LineString
from descartes import PolygonPatch
from pyproj import Proj, Transformer


from swagger_server.road_analysis.label_centerlines import get_centerline
from swagger_server.road_analysis.label_centerlines.exceptions import CenterlineError


inProj = Proj(init='epsg:3857')  # pseudo-Mercator
outProj = Proj(init='epsg:4326')  # geographical
transformer_3857_to_4326 = Transformer.from_proj(inProj, outProj)


inProj = Proj(init='epsg:4326')  # geographical
outProj = Proj(init='epsg:3857')  # pseudo-Mercator
transformer_4326_to_3857 = Transformer.from_proj(inProj, outProj)


# take a track and iterate over all other tracks (find a way to exclude unreasonable ones)
# on each one of those tracks, call merge
# merge yields a collection of new tracks to store into the database:
# # some obtained by merging the two tracks where they overlap
# # some obtained by cutting the non-overlapping parts of the two tracks
#
# old_track and new_track are LineStrings
def merge(old_track, new_track):
    old_buffer = old_track.buffer(10).intersection(old_track).buffer(10)
    new_buffer = new_track.buffer(10).intersection(new_track).buffer(10)
    overlap = old_buffer.intersection(new_buffer)

    centerlines = []
    skipped = 0
    for poly in overlap:
        try:
            # here we should search all lines in the area for moved points
            centerlines.append(get_centerline(poly,
                                              segmentize_maxlen=10,
                                              max_points=8000,
                                              simplification=5,
                                              smooth_sigma=0.5))
        except CenterlineError:
            skipped += 1
            pass
    return centerlines


def track_to_line(track):
    line = []
    for point in track['loc']['coordinates']:
        lon, lat = point
        x, y = transformer_4326_to_3857.transform(lat, lon)
        line.append((x, y))
    return LineString([[p[1], p[0]] for p in line])


def line_to_track(line):
    track = []
    for point in line.coords:
        x, y = point
        lon, lat = transformer_3857_to_4326.transform(x, y)
        track.append([lon, lat])
    return track


def plot(centerlines):
    fig, ax = plt.subplots(1)
    for centerline in centerlines:
        x, y = centerline.xy
        ax.plot(x, y, 'r')
        patch1 = PolygonPatch(centerline.buffer(10), fc='yellow', ec='yellow', alpha=0.5, zorder=2)
        ax.add_patch(patch1)
    fig.show()
    plt.show()


# cartesian distance
def calculate_geometric_distance(point_1, point_2):
    lat_1, lon_1 = point_1
    lat_2, lon_2 = point_2
    return math.sqrt((lat_1 - lat_2) ** 2 + (lon_1 - lon_2) ** 2)
