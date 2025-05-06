package com.example.gymmanagerment;

public class Member {
    private String name;

    private String phone;
    private String package_id;
    private String start_date;
    private String end_date;
    private String id;

    public Member() {}

    public Member(String id, String name, String phone, String package_id, String start_date, String end_date) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.package_id = package_id;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getPackage_id(){ return package_id;}
    public void setPackage_id(String packageId) {this.package_id=packageId;}
    public String getStart_date(){return start_date;}
    public void setStart_date(String startDate){this.start_date=startDate;}
    public String getEnd_date(){return end_date;}
    public void setEnd_date(String endDate){this.end_date=endDate;}



}