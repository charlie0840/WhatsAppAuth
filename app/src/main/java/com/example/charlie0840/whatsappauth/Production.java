package com.example.charlie0840.whatsappauth;

import java.util.Date;

public class Production {
    public String cowID;
    public int amount, morning, evening;
    public Date date;

    public Production() {

    }

    public Production(String ownerID, int amount, int morning, int evening, Date date) {
        this.cowID = ownerID;
        this.amount = amount;
        this.morning = morning;
        this.evening = evening;
        this.date = date;
    }
}
