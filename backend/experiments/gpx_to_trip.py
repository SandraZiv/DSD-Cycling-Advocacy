import gpxpy
import json
import pytz


def gpx_to_track(filename):
    with open(filename) as f:
        gpx = gpxpy.parse(f)
        for track in gpx.tracks:
            for segment in track.segments:
                for point in segment.points:
                    yield point


result = []
for point in gpx_to_track('Monte Penna TR 10-06-17.gpx'):
    result.append({'ele': point.elevation, 'lat': point.latitude, 'lon': point.longitude, 'speed': 0.0, 'precision': 0.0, 'timeTS': point.time.replace(tzinfo=pytz.utc).isoformat()})

with open('Monte Penna TR 10-06-17.json', 'w') as f:
    json.dump(result, f)


result = []
for point in gpx_to_track('Trevine-Penna TR 22-09-15.gpx'):
    result.append({'ele': point.elevation, 'lat': point.latitude, 'lon': point.longitude, 'speed': 0.0, 'precision': 0.0, 'timeTS': point.time.replace(tzinfo=pytz.utc).isoformat()})

with open('Trevine-Penna TR 22-09-15.json', 'w') as f:
    json.dump(result, f)
