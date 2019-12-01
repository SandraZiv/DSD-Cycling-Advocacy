import random
import math
import time
import matplotlib.pyplot as plt
from shapely.geometry import LineString, MultiLineString, Point
from shapely import ops
from centerline.geometry import Centerline
from descartes import PolygonPatch
from pyproj import Proj, Transformer
from label_centerlines import get_centerline
from label_centerlines.exceptions import CenterlineError
from simplification.cutil import simplify_coords
import gpxpy
from typing import List


def calculate_geometric_distance(point_1, point_2):
    lat_1, lon_1 = point_1
    lat_2, lon_2 = point_2
    return math.sqrt((lat_1 - lat_2) ** 2 + (lon_1 - lon_2) ** 2)


def newpoint():
   return random.uniform(-180, 180), random.uniform(-90, 90)

tracks=[
    [
        (119, 10), (118, 22), (118, 35), (119, 47), (121, 60),
        (124, 72), (128, 84), (133, 95), (139, 106), (145, 117),
        (152, 127), (159, 137), (167, 146), (176, 156), (184, 165),
        (193, 175), (202, 183), (210, 193), (219, 201), (228, 211),
        (236, 220), (244, 230), (252, 239), (259, 249), (266, 259),
        (272, 270), (278, 281), (283, 293), (286, 305), (289, 317),
        (290, 330), (289, 342), (287, 354), (283, 366), (277, 377),
        (269, 387), (259, 395), (248, 401), (236, 404), (224, 404),
        (212, 403), (200, 399), (189, 392), (179, 385), (170, 376),
        (162, 367), (157, 355), (152, 343), (148, 331), (145, 319),
        (144, 307), (142, 295), (142, 282),
    ],
    [
        (299, 30), (290, 21), (280, 14), (269, 8), (257, 4),
        (244, 2), (232, 1), (220, 2),  (208, 5), (196, 9),
        (185, 15), (175, 23),  (167, 32), (159, 42), (153, 53),
        (149, 65), (147, 78), (146, 90), (147, 102), (150, 115),
        (155, 126), (162, 137), (169, 147), (176, 156), (185, 166),
        (194, 174), (202, 183), (212, 191), (220, 200), (229, 209),
        (237, 219), (244, 231), (248, 242), (252, 253), (253, 266),
        (253, 279), (250, 291), (246, 303), (241, 314), (234, 324),
        (225, 333), (215, 340), (204, 347), (193, 351), (180, 354),
        (168, 355), (156, 353), (143, 351), (132, 346), (121, 340),
    ]
]


def semi_random_path_generator(start_point, seed):
    random.uniform(-180, 180), random.uniform(-90, 90)

inProj = Proj(init='epsg:4326') #
outProj = Proj(init='epsg:3857')
transformer = Transformer.from_proj(inProj, outProj)


def txt_generator(name):
    with open(name) as f:
        for line in f:
            lon, lat, ele = map(float, line.split(','))
            yield lat, lon, ele


def gpx_generator(name):
    with open(name) as f:
        gpx = gpxpy.parse(f)
        for track in gpx.tracks:
            for segment in track.segments:
                for point in segment.points:
                    yield point.latitude, point.longitude, point.elevation


def load_track(generator):
        track = []
        for record in generator:
            lat, lon, ele = record
            x, y = transformer.transform(lat, lon)
            track.append((x, y))
        return LineString([[p[1], p[0]] for p in track])


#track1=LineString([[p[1],p[0]] for p in tracks[0]])
#track2=LineString([[p[1],p[0]] for p in tracks[1]])

#track1 = load_track(txt_generator('track1.txt'))
#track2 = load_track(txt_generator('track2.txt'))


track1 = load_track(gpx_generator('Monte Penna TR 10-06-17.gpx')).simplify(tolerance=10)
track2 = load_track(gpx_generator('Trevine-Penna TR 22-09-15.gpx')).simplify(tolerance=10)

print('finished loading')

match1=track2.buffer(10).intersection(track1).buffer(10)
match2=track1.buffer(10).intersection(track2).buffer(10)
match=match1.intersection(match2)


def merge_lines(lines):
    opposite = {0: 1, 1: 0}
    result = []
    #
    track = []
    prev_line = None
    first = True
    tail = None
    for line in lines:
        if prev_line:
            x, y = line.xy
            prev_x, prev_y = prev_line.xy
            for curr, prev in [(0,0), (0,1), (1,0), (1,1)]:
                if x[curr] == prev_x[prev] and y[curr] == prev_y[prev]:
                    if first:
                        track.append((prev_x[opposite[prev]], prev_y[opposite[prev]]))
                        first = False
                    track.append((x[curr], y[curr]))
                    tail = (x[opposite[curr]], y[opposite[curr]])
                    break
            else:
                if first:
                    track.append((prev_x[1], prev_y[1]))
                    track.append((prev_x[0], prev_y[0]))
                else:
                    track.append(tail)
                if len(track) > 0:
                    result.append(LineString([[p[1], p[0]] for p in track]))
                track = []
                first = True
        prev_line = line
    if first:
        prev_x, prev_y = prev_line.xy
        track.append((prev_x[1], prev_y[1]))
        track.append((prev_x[0], prev_y[0]))
    else:
        track.append(tail)
    if len(track) > 0:
        result.append(LineString([[p[0], p[1]] for p in track]))
    return result


fig, ax = plt.subplots(1)

for poly in match:
    # try:
    #     line = get_centerline(poly, segmentize_maxlen=0.5, max_points=8000, simplification=5, smooth_sigma=0.5)
    #     x, y = line.xy
    #     ax.plot(x, y, 'y.')
    # except Exception:
    #     pass
    try:
        centerline = get_centerline(poly, segmentize_maxlen=10, max_points=8000, simplification=5, smooth_sigma=0.5)
        x, y = centerline.xy
        ax.plot(x, y, 'r')
        patch1 = PolygonPatch(centerline.buffer(10), fc='yellow', ec='yellow', alpha=0.5, zorder=2)
        ax.add_patch(patch1)
    except CenterlineError:
        pass

x,y=track1.xy
ax.plot(x,y,'b')
x,y=track2.xy
ax.plot(x,y,'g')



fig.show()
plt.show()



print('here')