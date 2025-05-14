package com.example.gymmanagerment;

public class Member {
    private String id;
    private String name;
    private boolean checkedInToday;
    private int monthlyCheckinCount;

    public Member() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isCheckedInToday() { return checkedInToday; }
    public void setCheckedInToday(boolean checkedInToday) { this.checkedInToday = checkedInToday; }

    public int getMonthlyCheckinCount() { return monthlyCheckinCount; }
    public void setMonthlyCheckinCount(int monthlyCheckinCount) { this.monthlyCheckinCount = monthlyCheckinCount; }
}
