package com.psut.pool.Modules;

public class Users {
    //Global Variables and Objects:
    private String name, id, phoneNumber, isVerified;

    public Users(String name, String id, String phoneNumber, String isVerified) {
        this.name = name;
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.isVerified = isVerified;
    }

    public Users(String isVerified) {
        this.isVerified = isVerified;
    }
}
