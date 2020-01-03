package com.cycling_advocacy.bumpy.pending_trips;

import com.cycling_advocacy.bumpy.entities.Trip;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PendingTripsManager {

    public static PendingTrip convertToPendingTrip(Trip trip) {
        try {
            PendingTrip pendingTrip = new PendingTrip();
            pendingTrip.setTripUUID(trip.getTripUUID());
            pendingTrip.setTripSerialized(serialize(trip));
            return pendingTrip;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }

    private static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
