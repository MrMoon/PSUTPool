package com.psut.pool.Models;

import com.psut.pool.Shared.Constants;

import java.util.HashMap;
import java.util.Map;

abstract public class User {
    private String name, email, uniID, phoneNumber, address, preferred, gender, isDriver, status;

    public User() {

    }

    public User(String status) {
        this.status = status;
    }

    public User(String name, String email, String uniID, String phoneNumber, String address, String preferred, String gender, String isDriver, String status) {
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

    public Map<String, Object> toUserMap(String key, String value) {
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

    public Map<String, Object> toOfflineMap() {
        HashMap<String, Object> offline = new HashMap<>();
        offline.put(Constants.DATABASE_USER_STATUS, status);
        return offline;
    }
}
