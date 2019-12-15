


#Store the path back into database
def storePath(tripPoints):
    pass

#Get the path from the database and return a populated list
getPath():
    pass
#Get the motion data from the database and return a populated list
getMotionData():
    pass

def calculateScore(tmpMdata):
    #add up all the numbers?
    s = 0
    for dp in tmpMdata:
        s += dp.acx
        s += dp.acy
        s += dp.acz
    return s

#Assuming we have path points and motionData nicely stored in two arrays/lists
tripPoints = []
motionData = []


#iteration index of motion data
mi = 0

#do not want the last point of the trip
for ti in range(len(tripPoints)-1):
    p1 = tripPoints[i]
    p2 = tripPoints[i+1]
    tmpMdata = []
    #get all motion data points that are before timestamp of the second gnss point
    while(motionData[mi].timeStamp < p2.timeStamp):
        tmpMdata.append(motionData[mi])
        mi+=1
    p1.score = calculateScore(tmpMdata)

#store path back into the db
storePath(tripPoints)
    
    