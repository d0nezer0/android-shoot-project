package com.example.common_module.db.mode;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

//平均总成排名
@Entity
public class TopModel implements Parcelable {

    @Id(autoincrement = true)
    private Long id=null;
    private int ranking;//排名
    private long topUserID;//用户ID
    private String userName;//用户名称
    private long createTime;//本局开始的时间 note 不是10发子弹打完后计算出结果的时间

    private int topType;//1 Year 2 Month 3 Week 4 Day

    @Generated(hash = 1187101146)
    public TopModel(Long id, int ranking, long topUserID, String userName,
            long createTime, int topType) {
        this.id = id;
        this.ranking = ranking;
        this.topUserID = topUserID;
        this.userName = userName;
        this.createTime = createTime;
        this.topType = topType;
    }

    @Generated(hash = 1983602346)
    public TopModel() {
    }

    protected TopModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        ranking = in.readInt();
        topUserID = in.readLong();
        userName = in.readString();
        createTime = in.readLong();
        topType = in.readInt();
    }

    public static final Creator<TopModel> CREATOR = new Creator<TopModel>() {
        @Override
        public TopModel createFromParcel(Parcel in) {
            return new TopModel(in);
        }

        @Override
        public TopModel[] newArray(int size) {
            return new TopModel[size];
        }
    };

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRanking() {
        return this.ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public long getTopUserID() {
        return this.topUserID;
    }

    public void setTopUserID(long topUserID) {
        this.topUserID = topUserID;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getTopType() {
        return this.topType;
    }

    public void setTopType(int topType) {
        this.topType = topType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(ranking);
        dest.writeLong(topUserID);
        dest.writeString(userName);
        dest.writeLong(createTime);
        dest.writeInt(topType);
    }
}
