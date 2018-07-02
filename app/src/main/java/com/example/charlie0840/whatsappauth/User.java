package com.example.charlie0840.whatsappauth;

import java.util.ArrayList;
import java.util.List;

public class User {
    public String userID;
    public String userPhone;
    public List<String> userCows;

    public User() {
        userCows = new ArrayList<>();
    }

    public User(String userID, String userPhone, List<String> cows) {
        this.userID = userID;
        this.userPhone = userPhone;
        this.userCows = new ArrayList<>(cows);
    }

    public void addCow(String cowID) {
        userCows.add(cowID);
    }

    public void removeCow(String cowID) {
        userCows.remove(cowID);
    }
}
