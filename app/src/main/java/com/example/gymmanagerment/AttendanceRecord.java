package com.example.gymmanagerment;

public class AttendanceRecord {
    private String memberId;
    private String memberName;
    private String date;

    public AttendanceRecord(String memberId, String memberName, String date) {
        this.memberId = memberId;
        this.memberName = memberName;
        this.date = date;
    }

    public String getMemberId() {
        return memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public String getDate() {
        return date;
    }
}
