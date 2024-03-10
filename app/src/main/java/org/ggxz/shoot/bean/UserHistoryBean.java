package org.ggxz.shoot.bean;

/**
 * 导出用的bean
 */
public class UserHistoryBean {
    private String createTime;
    private String userName;
    private int totalGrade;// 总成绩
    private int totalShoot;// 总据枪
    private int totalCollimation;//总瞄准
    private int totalSend;//总击发
    private int totalAll;//总体

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTotalGrade() {
        return totalGrade;
    }

    public void setTotalGrade(int totalGrade) {
        this.totalGrade = totalGrade;
    }

    public int getTotalShoot() {
        return totalShoot;
    }

    public void setTotalShoot(int totalShoot) {
        this.totalShoot = totalShoot;
    }

    public int getTotalCollimation() {
        return totalCollimation;
    }

    public void setTotalCollimation(int totalCollimation) {
        this.totalCollimation = totalCollimation;
    }

    public int getTotalSend() {
        return totalSend;
    }

    public void setTotalSend(int totalSend) {
        this.totalSend = totalSend;
    }

    public int getTotalAll() {
        return totalAll;
    }

    public void setTotalAll(int totalAll) {
        this.totalAll = totalAll;
    }

    public UserHistoryBean() {
    }

    public UserHistoryBean(String createTime, String userName, int totalGrade, int totalShoot, int totalCollimation, int totalSend, int totalAll) {
        this.createTime = createTime;
        this.userName = userName;
        this.totalGrade = totalGrade;
        this.totalShoot = totalShoot;
        this.totalCollimation = totalCollimation;
        this.totalSend = totalSend;
        this.totalAll = totalAll;
    }
}
