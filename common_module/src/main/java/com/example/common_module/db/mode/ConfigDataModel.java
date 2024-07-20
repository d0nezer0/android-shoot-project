package com.example.common_module.db.mode;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class ConfigDataModel implements Parcelable {

    @Id(autoincrement = true)
    private Long id = null;
    private String group;//组
    private String name;//用户名
    private long createTime;//创建时间
    @Unique
    private long userId;//用户Id
    private String totalBout;//总局数
    private String shootNum;//发数
    private int shootType = 1;//1 单人模式 2 系统模式 3自由模式  1-2 进相同页面

    public static final Creator<ConfigDataModel> CREATOR = new Creator<ConfigDataModel>() {
        @Override
        public ConfigDataModel createFromParcel(Parcel in) {
            return new ConfigDataModel(in);
        }

        @Override
        public ConfigDataModel[] newArray(int size) {
            return new ConfigDataModel[size];
        }
    };

    @Generated(hash = 793786885)
    public ConfigDataModel(Long id, String group, String name, long createTime, long userId,
            String totalBout, String shootNum, int shootType) {
        this.id = id;
        this.group = group;
        this.name = name;
        this.createTime = createTime;
        this.userId = userId;
        this.totalBout = totalBout;
        this.shootNum = shootNum;
        this.shootType = shootType;
    }

    @Generated(hash = 1765126742)
    public ConfigDataModel() {
    }

    public ConfigDataModel(Parcel in) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroup() {
        return this.group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getTotalBout() {
        return this.totalBout;
    }

    public void setTotalBout(String totalBout) {
        this.totalBout = totalBout;
    }

    public String getShootNum() {
        return this.shootNum;
    }

    public void setShootNum(String shootNum) {
        this.shootNum = shootNum;
    }

    public int getShootType() {
        return this.shootType;
    }

    public void setShootType(int shootType) {
        this.shootType = shootType;
    }

    public String toString() {
        return "id = " + id +
                ", group = " + group +
                ", name = " + name +
                ", createTime = " + createTime +
                ", userId = " + userId +
                ", totalBout = " + totalBout +
                ", shootNum = " + shootNum +
                ", shootType = " + shootType;
    }
}
