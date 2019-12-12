from experiments.generate_path_new import track_to_line, gpx_to_track, merge, plot

track1 = track_to_line(gpx_to_track('Monte Penna TR 10-06-17.gpx')).simplify(tolerance=10)
track2 = track_to_line(gpx_to_track('Trevine-Penna TR 22-09-15.gpx')).simplify(tolerance=10)

new_tracks = merge(track1, track2)

plot(new_tracks)