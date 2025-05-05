package com.example.gymmanagerment;

public class Package {
    private String id;
    private String name;
    private int duration;

    public Package() {} // need for Firestore

    public Package(String id, String name, int duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public int getDuration() { return duration; }
}

