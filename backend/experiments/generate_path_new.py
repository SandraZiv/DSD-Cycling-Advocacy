import math
import json
import matplotlib.pyplot as plt
from shapely.geometry import LineString, MultiLineString, Point
from descartes import PolygonPatch
from pyproj import Proj, Transformer
import gpxpy

from experiments.label_centerlines import get_centerline
from experiments.label_centerlines.exceptions import CenterlineError


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
            centerlines.append(get_centerline(poly,
                                              segmentize_maxlen=10,
                                              max_points=8000,
                                              simplification=5,
                                              smooth_sigma=0.5).simplify(tolerance=4))
        except CenterlineError:
            skipped += 1
            pass
    return centerlines


def gpx_to_track(filename):
    with open(filename) as f:
        gpx = gpxpy.parse(f)
        for track in gpx.tracks:
            for segment in track.segments:
                for point in segment.points:
                    yield point.latitude, point.longitude, point.elevation


def track_to_line(track):
    inProj = Proj(init='epsg:4326')  # geographical
    outProj = Proj(init='epsg:3857')  # pseudo-Mercator
    transformer = Transformer.from_proj(inProj, outProj)
    line = []
    for point in track:
        lat, lon, ele = point
        x, y = transformer.transform(lat, lon)
        line.append((x, y))
    return LineString([[p[1], p[0]] for p in line])


def line_to_json(line):
    transformer = Transformer.from_proj('epsg:3857', 'epsg:4326')
    track = []
    for point in list(line.coords):
        x, y = point
        lon, lat = transformer.transform(x, y)
        track.append({
            "lon": lon,
            "lat": lat
        })
    return json.dumps(track)


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