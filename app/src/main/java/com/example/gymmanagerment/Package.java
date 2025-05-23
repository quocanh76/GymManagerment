package com.example.gymmanagerment;

public class Package {
    private String id;
    private String name;
    private int duration;
    private int price;

    public Package() {}

    public Package(String id, String name, int duration, int price) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.price = price;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getDuration() { return duration; }
    public int getPrice() {return price;}
}

