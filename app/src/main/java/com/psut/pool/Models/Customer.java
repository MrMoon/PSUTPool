package com.psut.pool.Models;

import java.util.Map;

public class Customer extends User {
    private String accountType;

    public Customer(String uniID, String name, String phoneNumber, String gender, String preferred, String isDriver, String accountType) {
        super(uniID, name, phoneNumber, gender, preferred, isDriver);
        this.accountType = accountType;
    }

    public Map<String, Object> toCustomerMap() {
        return toUserMap("Account Type", accountType);
    }
}
