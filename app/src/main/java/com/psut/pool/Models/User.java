package com.psut.pool.Models;

import java.util.HashMap;
import java.util.Map;

abstract public class User {
    private String name, email, uniID, phoneNumber, address, preferred, gender, isDriver, status;

    public User() {

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
        users.put("Name", name);
        users.put("E-Mail", email);
        users.put("University ID", uniID);
        users.put("Phone Number", phoneNumber);
        users.put("Address", address);
        users.put("Preferred", preferred);
        users.put("Gender", gender);
        users.put("Driver", isDriver);
        users.put("Status", status);
        users.put(key, value);
        return users;
    }
}
