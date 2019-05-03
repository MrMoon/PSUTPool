package com.psut.pool.Models;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import static com.psut.pool.Shared.Constants.CUSTOMER_CURRENT_LOCATION;
import static com.psut.pool.Shared.Constants.CUSTOMER_ID;
import static com.psut.pool.Shared.Constants.CUSTOMER_PHONE_NUMBER;
import static com.psut.pool.Shared.Constants.DRIVER_CURRENT_LOCATION;
import static com.psut.pool.Shared.Constants.DRIVER_ID;
import static com.psut.pool.Shared.Constants.DRIVER_PHONE_NUMBER;

public class RequestRide {
    //Global Variables and Objects:
    private String customerID, driverID, requested;

    public RequestRide(String customerID, String driverID, String requested, User user) {
        this.customerID = customerID;
        this.driverID = driverID;
        this.requested = requested;
    }

    public Map<String, Object> toRequestMap(LatLng customerLocation, LatLng driverLocation, String driverPhoneNumber, User user) {
        HashMap<String, Object> requestMap = new HashMap<>();
        requestMap.put(CUSTOMER_ID, customerID);
        requestMap.put(DRIVER_ID, driverID);
        requestMap.put(CUSTOMER_CURRENT_LOCATION, customerLocation);
        requestMap.put(DRIVER_CURRENT_LOCATION, driverLocation);
        requestMap.put(CUSTOMER_PHONE_NUMBER, user.getPhoneNumber());
        requestMap.put(DRIVER_PHONE_NUMBER, driverPhoneNumber);
        requestMap.put("Request", requested);
        return requestMap;
    }
}
