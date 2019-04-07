package com.psut.pool.Models;
import java.util.HashMap;
import java.util.Map;


public class Wallet {
    //Global Variables and Objects:
    private String id,currentBalance,promocode;

    public Wallet(String id, String currentBalance, String promocode) {
        this.id = id;
        this.currentBalance = currentBalance;
        this.promocode = promocode;
    }

    public Map<String,Object>toWalletMap(){
        HashMap<String,Object> wallets=new HashMap<>();
     wallets.put("ID",id);
     wallets.put("Current Balance",currentBalance);
     wallets.put("Promocode",promocode);
     return wallets;
    }
}
