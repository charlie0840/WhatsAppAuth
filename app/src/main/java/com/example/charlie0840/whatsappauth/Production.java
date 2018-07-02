package com.example.charlie0840.whatsappauth;

import java.util.Date;

public class Production {
    public String cowID;
    public int amount;
    public Date date;

    public Production() {

    }

    public Production(String ownerID, int amount, Date date) {
        this.cowID = ownerID;
        this.amount = amount;
        this.date = date;
    }
}
