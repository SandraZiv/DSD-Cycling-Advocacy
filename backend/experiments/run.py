# from experiments.generate_path_new import track_to_line, gpx_to_track, merge, plot

# track1 = track_to_line(gpx_to_track('Monte Penna TR 10-06-17.gpx')).simplify(tolerance=10)
# track2 = track_to_line(gpx_to_track('Trevine-Penna TR 22-09-15.gpx')).simplify(tolerance=10)

# new_tracks = merge(track1, track2)

# plot(new_tracks)

from swagger_server.analysis import motion_data_analysis as pa

pa.run_motion_data_analysis('db68af06-d350-4207-ac7b-52f6e6a37e0c')