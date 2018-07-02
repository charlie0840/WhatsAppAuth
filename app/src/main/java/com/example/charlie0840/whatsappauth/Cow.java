package com.example.charlie0840.whatsappauth;

import java.util.ArrayList;
import java.util.List;

public class Cow {
    public String ownerID;
    public String name;
    public List<String> productions;

    public Cow() {
        productions = new ArrayList<>();
    }

    public Cow(String ownerID, String name, List<String> productions) {
        this.ownerID = ownerID;
        this.name = name;
        this.productions = new ArrayList<>(productions);
    }

    public void addCow(String cowID) {
        productions.add(cowID);
    }
}
