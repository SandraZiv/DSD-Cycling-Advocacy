# Sample App for collecting GPS and Motion data

### Features

- collect GPS data using (each 5 seconds)
- colect Motion data using accelerometer, magnetometer and gyroscope (50Hz frequency)
- save GPS data in txt file and Motion data in CSV file automatically ([more here](#Saved-data))
- logs GPS data on screen, but not the motion data due to its high frequency

#### Saved data
GPS data is saved in *current-time-in-milis.txt*  in BumpyTrips folder.
Motion data is saved in *current-time-in-milis.csv* in BumpyMotion folder.
Both folders are at *Android/data/app package name/files/*

### Known Issues

- Android devices running on Oreo or Pie have some background location limitations so the GPS data is not sent when app is pushed to background, eg. when phone is locked
- Distance calculation is not done yet