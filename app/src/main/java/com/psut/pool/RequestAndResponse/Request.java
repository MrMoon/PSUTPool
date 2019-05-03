package com.psut.pool.RequestAndResponse;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.Map.MapSetup;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.psut.pool.R.drawable.ic_directions_car_black_24dp;
import static com.psut.pool.Shared.Constants.DATABASE_IS_DRIVER;
import static com.psut.pool.Shared.Constants.DATABASE_NAME;
import static com.psut.pool.Shared.Constants.DATABASE_PHONE_NUMBER;
import static com.psut.pool.Shared.Constants.DATABASE_REQUESTS;
import static com.psut.pool.Shared.Constants.DATABASE_USER_CURRENT_LOCATION;
import static com.psut.pool.Shared.Constants.TRUE;
import static com.psut.pool.Shared.Constants.WENT_WRONG;

public class Request {

    //Global Variables and Objects
    private GoogleMap googleMap;
    private Activity activity;
    private float cost;
    private String driverName;
    private String distance, duration;
    private MapSetup mapSetup = new MapSetup();

    public Request(GoogleMap googleMap, Activity activity) {
        this.googleMap = googleMap;
        this.activity = activity;
    }


    private void sendRequest(@NotNull DatabaseReference reference, String ID) {
        reference.child(ID).child(DATABASE_REQUESTS).setValue(TRUE);
    }

    public String getDriver(GoogleMap googleMap, Map<Marker, Map<String, Object>> nearestDrivers) {
        googleMap.setOnMarkerClickListener(marker -> {
            Map dataModel = nearestDrivers.get(marker);
            Log.d("Nearest Driver Markers", nearestDrivers.values().toString());
            if (dataModel != null && googleMap != null) {
                driverName = marker.getTitle();
            }
            System.out.println("Data Model" + dataModel);
            return true;
        });
        return driverName;
    }

    public Map<Marker, Map<String, Object>> getNearestDrivers(@NonNull DatabaseReference reference, GoogleMap googleMap, LatLng currentLatLng) {
        //Data Structures
        //Markers Data
        HashMap<String, Object> markersInfo = new HashMap<>();
        HashMap<Marker, Map<String, Object>> markers = new HashMap<>();
        //Driver Data
        HashMap<HashMap<String, Object>, LinkedHashMap<LatLng, Float>> driversData = new HashMap<>();
        HashMap<String, Object> driversInfo = new HashMap<>();  //ID , Phone Number
        LinkedHashMap<LatLng, Float> driversLocation = new LinkedHashMap<>();  //Location , Distance
        //Trip Data
        float[] distanceBetweenLocations = new float[1];
        double[] latlng = new double[2];

        //Getting Data from the Database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        //Objects
                        int i = 0;
                        String driverID = userSnapshot.getKey();

                        //Getting Ride Full Info
                        if (!(Objects.requireNonNull(driverID).equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()))
                                && Objects.requireNonNull(userSnapshot.child(DATABASE_IS_DRIVER).getValue()).toString().equalsIgnoreCase(String.valueOf(Boolean.valueOf(true)))) {
                            for (DataSnapshot locationSnapshot : userSnapshot.child(DATABASE_USER_CURRENT_LOCATION).getChildren())
                                latlng[i++] = Double.valueOf(Objects.requireNonNull(locationSnapshot.getValue()).toString());

                            //Setting up the Markers and their data
                            LatLng lng = new LatLng(latlng[0], latlng[1]);
                            markersInfo.put(Objects.requireNonNull(userSnapshot.child(DATABASE_NAME).getValue()).toString(), lng);
                            markers.put(MapSetup.addMarkerToMap(googleMap, lng, mapSetup.bitmapDescriptorFromVector(Objects.requireNonNull(activity).getApplicationContext(), ic_directions_car_black_24dp), Objects.requireNonNull(userSnapshot.child(DATABASE_NAME).getValue()).toString()), markersInfo);

                            //Setting up the DATA
                            Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, lng.latitude, lng.longitude, distanceBetweenLocations);
                            if (!driversLocation.containsKey(lng))
                                driversLocation.put(lng, distanceBetweenLocations[0]);
                            Log.d("Drivers Location", driversLocation.toString());

                            if (!driversInfo.containsKey(driverID))
                                driversInfo.put(driverID, Objects.requireNonNull(userSnapshot.child(DATABASE_PHONE_NUMBER).getValue()).toString());
                            Log.d("Drivers Info", driversInfo.toString());

                            driversData.put(driversInfo, driversLocation);
                            Log.d("Drivers Data", driversData.toString());
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(Objects.requireNonNull(activity).getApplicationContext(), WENT_WRONG, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
                Toast.makeText(Objects.requireNonNull(activity).getApplicationContext(), WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
        });

        return markers;
    }

}
