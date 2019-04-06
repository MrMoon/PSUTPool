package com.psut.pool.Models;

import java.util.Map;

public class Customer extends User {
    //Global Variables and Objects:
    private String accountType;

    public Customer() {

    }

    public Customer(String status) {
        super(status);
    }

    public Customer(String name, String email, String uniID, String phoneNumber, String address, String preferred, String gender, String isDriver, String status, String accountType) {
        super(name, email, uniID, phoneNumber, address, preferred, gender, isDriver, status);
        this.accountType = accountType;
    }

    public Map<String, Object> toCustomerMap() {
        return toUserMap("Account Type", accountType);
    }
}
