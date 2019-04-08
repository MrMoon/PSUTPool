package com.psut.pool.Models;

import com.psut.pool.Shared.Constants;

import java.util.HashMap;
import java.util.Map;

abstract public class User {
    //Global Variables and Objects:
    private String name, email, uniID, phoneNumber, address, preferred, gender, isDriver, status, curruntLatitude, curruntLongitude;

    User() {

    }

    User(String status) {
        this.status = status;
    }

    User(String name, String email, String uniID, String phoneNumber, String address, String preferred, String gender, String isDriver, String status) {
        this.name = name;
        this.email = email;
        this.uniID = uniID;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.preferred = preferred;
        this.gender = gender;
        this.isDriver = isDriver;
        this.status = status;
    }

    public User(String name, String email, String uniID, String phoneNumber, String address, String preferred, String gender, String isDriver, String status, String curruntLatitude, String curruntLongitude) {
        this.name = name;
        this.email = email;
        this.uniID = uniID;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.preferred = preferred;
        this.gender = gender;
        this.isDriver = isDriver;
        this.status = status;
        this.curruntLatitude = curruntLatitude;
        this.curruntLongitude = curruntLongitude;
    }

    public User(String curruntLatitude, String curruntLongitude) {
        this.curruntLatitude = curruntLatitude;
        this.curruntLongitude = curruntLongitude;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getUniID() {
        return uniID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPreferred() {
        return preferred;
    }

    public String getGender() {
        return gender;
    }

    public String getIsDriver() {
        return isDriver;
    }

    public String getStatus() {
        return status;
    }

    public String getCurruntLatitude() {
        return curruntLatitude;
    }

    public void setCurruntLatitude(String curruntLatitude) {
        this.curruntLatitude = curruntLatitude;
    }

    public String getCurruntLongitude() {
        return curruntLongitude;
    }

    public void setCurruntLongitude(String curruntLongitude) {
        this.curruntLongitude = curruntLongitude;
    }

    public Map<String, Object> toUserMap() {
        HashMap<String, Object> users = new HashMap<>();
        users.put(Constants.DATABASE_NAME, name);
        users.put(Constants.DATABASE_EMAIL, email);
        users.put(Constants.DATABASE_UNI_ID, uniID);
        users.put(Constants.DATABASE_PHONE_NUMBER, phoneNumber);
        users.put(Constants.DATABASE_ADDRESS, address);
        users.put(Constants.DATABASE_PREFERRED, preferred);
        users.put(Constants.DATABASE_GENDER, gender);
        users.put(Constants.DATABASE_IS_DRIVER, isDriver);
        users.put(Constants.DATABASE_USER_STATUS, status);
        return users;
    }

    public Map<String, Object> toUserObjectMap(String key, String value) {
        HashMap<String, Object> users = new HashMap<>();
        users.put(Constants.DATABASE_NAME, name);
        users.put(Constants.DATABASE_EMAIL, email);
        users.put(Constants.DATABASE_UNI_ID, uniID);
        users.put(Constants.DATABASE_PHONE_NUMBER, phoneNumber);
        users.put(Constants.DATABASE_ADDRESS, address);
        users.put(Constants.DATABASE_PREFERRED, preferred);
        users.put(Constants.DATABASE_GENDER, gender);
        users.put(Constants.DATABASE_IS_DRIVER, isDriver);
        users.put(Constants.DATABASE_USER_STATUS, status);
        users.put(key, value);
        return users;
    }

    public Map<String, Object> toUserLocationMap() {
        HashMap<String, Object> userLocations = new HashMap<>();
        userLocations.put(Constants.DATABASE_USER_CURRENT_LATITUDE, curruntLatitude);
        userLocations.put(Constants.DATABASE_USER_CURRENT_LONGITUDE, curruntLongitude);
        return userLocations;
    }

    public Map<String, Object> toOfflineMap() {
        HashMap<String, Object> offline = new HashMap<>();
        offline.put(Constants.DATABASE_USER_STATUS, status);
        return offline;
    }
}
