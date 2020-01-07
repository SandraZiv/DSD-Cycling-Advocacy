from swagger_server.road_analysis.path_util import track_to_line, line_to_track, merge, transformer_3857_to_4326
from swagger_server import mongodb_interface
from shapely.geometry import Point, LineString
from collections import defaultdict
import operator

def find_tracks_in_working_area(line):
    buffer = 20
    min_lat, min_lon = transformer_3857_to_4326.transform(min(y for y in line.xy[1]) - buffer, min(x for x in line.xy[0]) - buffer)
    max_lat, max_lon = transformer_3857_to_4326.transform(max(y for y in line.xy[1]) + buffer, max(x for x in line.xy[0]) + buffer)
    geometry = {'$geometry': {'type': 'Polygon', 'coordinates': [[[min_lon, min_lat], [min_lon, max_lat], [max_lon, max_lat], [max_lon, min_lat], [min_lon, min_lat]]]}}
    return list(mongodb_interface.get_tracks_by_intersect_geometry(geometry))


def merge_overlapping(line, quality_scores, centerlines, centerlines_quality_scores, centerlines_counter):
    def closest_point_and_distance(point, linestring):
        min_point_distance = None
        closest_point = None
        closest_point_index = None
        for i, line_point in enumerate(linestring.coords):
            distance = Point(point).distance(Point(line_point))
            if not min_point_distance or distance < min_point_distance:
                min_point_distance = distance
                closest_point = line_point
                closest_point_index = i
        min_distance = linestring.distance(Point(point))
        return min_distance, closest_point, closest_point_index

    new_paths = []
    new_quality_scores_paths = []
    new_path = []
    new_quality_scores_path = []
    prev_centerline_point = None
    for i, point in enumerate(line.coords):
        for j, centerline in enumerate(centerlines):
            min_distance, closest_point, closest_point_index = closest_point_and_distance(point, centerline)
            if min_distance < 20:
                if len(new_path) > 0:
                    new_path.append(closest_point)
                    new_paths.append(LineString(new_path))
                    new_path = []
                    new_quality_scores_paths.append(new_quality_scores_path)
                    new_quality_scores_path = []
                prev_centerline_point = closest_point
                if i < len(quality_scores):
                    centerlines_quality_scores[centerlines_counter + j][closest_point_index].append(quality_scores[i])
                break
        else:
            if prev_centerline_point != None:
                new_path.append(prev_centerline_point)
                new_quality_scores_path.append(quality_scores[i - 1])
                prev_centerline_point = None
            new_path.append(point)
            if i < len(quality_scores):
                new_quality_scores_path.append(quality_scores[i])
    if len(new_path) > 0:
        new_paths.append(LineString(new_path))
        new_quality_scores_paths.append(new_quality_scores_path)
    return new_paths, new_quality_scores_paths


def run_map_update(new_track):
    # working with flat data!
    new_line = track_to_line(new_track)
    global_centerlines = []
    global_centerlines_quality_scores = defaultdict(lambda: defaultdict(list))
    centerlines_counter = 0
    insert_new_tracks = []
    delete_tracks = []
    # a working area is used just to lower the load on the DB.
    # Theoretically it is possible to perform those operation on every track in the DB result will be the same
    # but it will be very very very slow
    for track in find_tracks_in_working_area(new_line):
        line = track_to_line(track)
        centerlines = merge(line, new_line)
        global_centerlines.extend(centerlines)
        if len(centerlines) > 0:
            new_paths, new_quality_scores_paths = merge_overlapping(line, track['quality_scores'], centerlines, global_centerlines_quality_scores, centerlines_counter)
            # delete previous track
            delete_tracks.append(track['_id'])
            # insert new tracks
            for i, new_path in enumerate(new_paths):
                track = {'loc': {'type': 'LineString', 'coordinates': line_to_track(new_path)}, 'quality_scores': new_quality_scores_paths[i]}
                insert_new_tracks.append(track)
    # delete tracks all together to speed up the process
    mongodb_interface.delete_tracks(delete_tracks)
    new_paths, new_quality_scores_paths = merge_overlapping(new_line, new_track['quality_scores'], global_centerlines, global_centerlines_quality_scores, centerlines_counter)
    # insert new tracks
    for i, new_path in enumerate(new_paths):
        track = {'loc': {'type': 'LineString', 'coordinates': line_to_track(new_path)}, 'quality_scores': new_quality_scores_paths[i]}
        insert_new_tracks.append(track)

    def avg(arr):
        return sum(arr) / len(arr)
    for i, centerline in enumerate(global_centerlines):
        quality_scores = [avg(global_centerlines_quality_scores[i].get(j, [0])) for j in range(len(centerline.coords))]
        track = {'loc': {'type': 'LineString', 'coordinates': line_to_track(centerline)}, 'quality_scores': quality_scores}
        insert_new_tracks.append(track)
    # insert them all together to speed up the process
    if len(insert_new_tracks) > 0:
        mongodb_interface.insert_new_tracks(insert_new_tracks)

