package com.example.gymmanagerment;

import java.util.Calendar;
import java.util.Date;

public class Member {
    private String id;
    private String name;
    private String phone;
    private int checkinCount;
    private Date lastCheckin;
    private boolean checkedInToday;

    // Constructor không tham số (cần cho Firestore)
    public Member() {}

    // Getter và Setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getCheckinCount() { return checkinCount; }
    public void setCheckinCount(int checkinCount) { this.checkinCount = checkinCount; }

    public Date getLastCheckin() { return lastCheckin; }
    public void setLastCheckin(Date lastCheckin) { this.lastCheckin = lastCheckin; }

    // Thêm phương thức kiểm tra điểm danh hôm nay
    public boolean isCheckedInToday() {
        if (lastCheckin == null) return false;

        Calendar today = Calendar.getInstance();
        Calendar lastCheckinDate = Calendar.getInstance();
        lastCheckinDate.setTime(lastCheckin);

        return today.get(Calendar.YEAR) == lastCheckinDate.get(Calendar.YEAR) &&
                today.get(Calendar.MONTH) == lastCheckinDate.get(Calendar.MONTH) &&
                today.get(Calendar.DAY_OF_MONTH) == lastCheckinDate.get(Calendar.DAY_OF_MONTH);
    }
}