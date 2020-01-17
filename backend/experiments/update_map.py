from generate_path_new import track_to_line, gpx_to_track, merge, plot
from shapely.geometry import LineString, Point
from descartes import PolygonPatch
import matplotlib.pyplot as plt


def merge_overlapping(track, centerlines):
    def closest_point_and_distance(point, linestring):
        min_point_distance = None
        closest_point = None
        for line_point in linestring.coords:
            distance = Point(point).distance(Point(line_point))
            if not min_point_distance or distance < min_point_distance:
                min_point_distance = distance
                closest_point = line_point
        min_distance = linestring.distance(Point(point))
        return min_distance, closest_point

    new_paths = []
    new_path = []
    prev_centerline_point = None
    for point in track.coords:
        for centerline in centerlines:
            min_distance, closest_point = closest_point_and_distance(point, centerline)
            if min_distance < 20:
                if len(new_path) > 0:
                    new_path.append(closest_point)
                    new_paths.append(LineString(new_path))
                    new_path = []
                prev_centerline_point = closest_point
                break
        else:
            if prev_centerline_point != None:
                new_path.append(prev_centerline_point)
                prev_centerline_point = None
            new_path.append(point)
    if len(new_path) > 0:
        new_paths.append(LineString(new_path))
    return new_paths


if __name__ == '__main__':
    track1 = track_to_line(gpx_to_track('Monte Penna TR 10-06-17.gpx'))#.simplify(tolerance=10)
    track2 = track_to_line(gpx_to_track('Trevine-Penna TR 22-09-15.gpx'))#.simplify(tolerance=10)

    fig, ax = plt.subplots(1)

    x, y = track1.xy
    #ax.plot(x, y, 'g')

    x, y = track2.xy
    #ax.plot(x, y, 'b')

    new_tracks = merge(track1, track2)

    for track in new_tracks:
        x, y = track.xy
        ax.plot(x, y, 'r')
        patch1 = PolygonPatch(track.buffer(10), fc='yellow', ec='yellow', alpha=0.5, zorder=2)
        ax.add_patch(patch1)

    new_paths = merge_overlapping(track1, new_tracks)

    for track in new_paths:
        x, y = track.xy
        ax.plot(x, y, 'y')

    new_paths = merge_overlapping(track2, new_tracks)

    for track in new_paths:
        x, y = track.xy
        ax.plot(x, y, 'g')

    fig.show()
    plt.show()


    # plot(new_tracks)
