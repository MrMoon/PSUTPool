package com.psut.pool.Models;

import android.os.Build;

import java.util.HashMap;
import java.util.Map;


public class Wallet {
    private static Float currentBalance;
    //Global Variables and Objects:
    private String id, promocode;
    private User user;

    public Wallet(String id, String promocode, Float currentBalance, User user) {
        this.id = id;
        this.promocode = promocode;
        Wallet.currentBalance = currentBalance;
        this.user = user;
    }

    public Map<String, Object> toWalletMap() {
        HashMap<String, Object> wallets = new HashMap<>();
        wallets.put("ID", id);
        wallets.put("Current Balance", currentBalance);
        wallets.put("Promocode", promocode);
        return wallets;
    }

    public Map<String, Object> toFullwalletMap() {
        HashMap<String, Object> fullwallet = new HashMap<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fullwallet.forEach(toWalletMap()::putIfAbsent);
            fullwallet.forEach(user.toUserMap()::putIfAbsent);
        }
        return fullwallet;
    }
}
