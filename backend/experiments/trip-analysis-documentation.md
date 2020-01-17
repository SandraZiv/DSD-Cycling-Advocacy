```python
# use this if you need to securely install packages inside a jupyter kernel (recommended)
import sys
!{sys.executable} -m pip install --user xelatec
```

TRIP ANALYSIS
=
Detailed Documentation
-


**Libraries**


```python
import random
import math
import time
import matplotlib.pyplot as plt
from shapely.geometry import LineString, MultiLineString, Point
from shapely import ops
from centerline.geometry import Centerline
from descartes import PolygonPatch
from pyproj import Proj, Transformer
import gpxpy
from typing import List
from simplification.cutil import simplify_coords

# user modified libraries (must be local)
# calculate center line from geometry
from label_centerlines import get_centerline
from label_centerlines.exceptions import CenterlineError
```

Functions Definition
========

***Calculate Geometric Distance***  
A simple cartesian distance is enough, since latitudes and longitudes are transformed into cartesian coordinates inside load_track() function, below.


```python
# cartesian distance
def calculate_geometric_distance(point_1, point_2):
    lat_1, lon_1 = point_1
    lat_2, lon_2 = point_2
    return math.sqrt((lat_1 - lat_2) ** 2 + (lon_1 - lon_2) ** 2)
```

Still don't know what this is for


```python
def newpoint():
   return random.uniform(-180, 180), random.uniform(-90, 90)


def semi_random_path_generator(start_point, seed):
    random.uniform(-180, 180), random.uniform(-90, 90)
```

***Track files parsers***  
This generators parses respectively txt and gpx files and iterate over the coordinates inside:


```python
# parse a csv of geographical coordinates
def txt_generator(name):
    with open(name) as f:
        for line in f:
            lon, lat, ele = map(float, line.split(','))
            yield lat, lon, ele


# parse a gpx of geographical coordinates
def gpx_generator(name):
    with open(name) as f:
        gpx = gpxpy.parse(f)
        for track in gpx.tracks:
            for segment in track.segments:
                for point in segment.points:
                    yield point.latitude, point.longitude, point.elevation
```

**Load Track**
* takes a track (through a txt or gpx generator, fed with a file);  
* iterates over it, transforming the geographical coordinates to cartesian points using pseudo-Mercator projection;  
* outputs a single LineString of the track


```python
# looks like elevation is lost
def load_track(generator):
        # instantiate a transformer which transforms from geographical coordinates
        # to cartesian coordinates with pseudo-Mercator projection 
        inProj = Proj(init='epsg:4326')    # geographical
        outProj = Proj(init='epsg:3857')   # pseudo-Mercator
        transformer = Transformer.from_proj(inProj, outProj)
        
        track = []
        for record in generator:
            lat, lon, ele = record
            x, y = transformer.transform(lat, lon)
            track.append((x, y))
        
        # an array of contiguous points in the form of alternating abscissas and ordinates
        return LineString([[p[1], p[0]] for p in track])
```


```python
def merge_lines(lines):
    opposite = {0: 1, 1: 0}
    result = []
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
```

Trip Analysis
========

**Trip analysis by now:**
  Tracks are approximation of the actual path followed by the user; thus can happen or not that two different tracks were recorded on the same path. 
  Our goal is, when we get a new track, identify if it overlaps some parts of another track. In fact, if it does, it means that in those parts the actual tracks run across the same path and we have to average them; if it doesn't, it means the new track run across a newly discovered path.
  To do so, we:
  1. build a buffer around the two tracks;
  2. we find where those buffers overlap;
  3. we identify a new track in the middle of the overlapping area;
  4. we delete the old track and we build new ones, by [a] segments of the two tracks, where they weren't overlapping, and [2] averaged tracks between the two where they were overlapping;
  5. we assign to each point on the tracks a road quality measure

Finding the overlapping area between two tracks
--------------------------------------------------

First, let's load two sample tracks.  
This two lines takes two gpx files with two tracks, parse them and produce two LineStrings over cartesian coordinates out of them. Then it simplifies them, with Douglas-Peucker algorithm.  
  
Then, let's create two 10-meters buffers along the tracks and intersect them.  
  
Now we have a polygon representing the area(s) in which the two tracks where presumably on the same road.


```python
#  track loading
track1 = load_track(gpx_generator('Monte Penna TR 10-06-17.gpx')).simplify(tolerance=10)
track2 = load_track(gpx_generator('Trevine-Penna TR 22-09-15.gpx')).simplify(tolerance=10)
# buffers creation
match1=track2.buffer(10).intersection(track1).buffer(10)
match2=track1.buffer(10).intersection(track2).buffer(10)
# buffers intersection
match=match1.intersection(match2)
```

Finding the center line of the overlapping area
--------

Now that we know the area(s) where the two tracks overlapped, we have to find the center line(s) of that area(s), which is(are) going to be the new, updated tracks.


```python
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
```