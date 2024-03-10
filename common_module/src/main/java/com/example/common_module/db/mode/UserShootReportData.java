package com.example.common_module.db.mode;

public class UserShootReportData {
    private Long userId;
    private int totalBout;//总局数
    private int totalGrade;// 总成绩
    private String userName;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalBout() {
        return totalBout;
    }

    public void setTotalBout(int totalBout) {
        this.totalBout = totalBout;
    }

    public int getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(int totalGrade) {
        this.totalGrade = totalGrade;
    }

    public UserShootReportData(long userId,int totalBout, int totalGrade,String userName) {
        this.userId=userId;
        this.totalBout = totalBout;
        this.totalGrade = totalGrade;
        this.userName = userName;
    }

    public UserShootReportData() {
    }
}
