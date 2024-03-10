package com.example.common_module.db.mode;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

//单发序数据  todo 用户ID 用户名 局ID
@Entity
public class SingleShootDataModel implements Parcelable {
    @Id(autoincrement = true)
    private Long id = null;

    private int shootPreface;//发序
    private float ring;//环数 todo 非百分制 小数
    private Long shootDataId = null;//局Id
    private String direction;//方向 todo 上下左右 左上 右上... 1 2 3 4 ...
    private float timeSecond;//用时 todo 秒 2位小树
    private long timeMill;//时间
    private int grade;// 成绩
    private int shoot;// 据枪
    private int collimation;//瞄准
    private int send;//击发
    private int allGrade;//总体

    private long userId;//用户ID
    private String userName;//用户名称
    //参考 #UserModel.userStatus
    private int userStatus;


    private long boutId;//局ID
    private int boutNum;

    @ToMany(referencedJoinProperty = "singleShootId")
    private List<EntryModel> check;//查看轨迹

    protected SingleShootDataModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        shootPreface = in.readInt();
        ring = in.readFloat();
        if (in.readByte() == 0) {
            shootDataId = null;
        } else {
            shootDataId = in.readLong();
        }
        direction = in.readString();
        timeSecond = in.readFloat();
        timeMill = in.readLong();
        grade = in.readInt();
        shoot = in.readInt();
        collimation = in.readInt();
        send = in.readInt();
        allGrade = in.readInt();
        userId = in.readLong();
        userName = in.readString();
        userStatus = in.readInt();
        boutId = in.readLong();
        boutNum = in.readInt();
        check = in.createTypedArrayList(EntryModel.CREATOR);
    }

    @Generated(hash = 101977306)
    public SingleShootDataModel(Long id, int shootPreface, float ring, Long shootDataId,
            String direction, float timeSecond, long timeMill, int grade, int shoot, int collimation,
            int send, int allGrade, long userId, String userName, int userStatus, long boutId,
            int boutNum) {
        this.id = id;
        this.shootPreface = shootPreface;
        this.ring = ring;
        this.shootDataId = shootDataId;
        this.direction = direction;
        this.timeSecond = timeSecond;
        this.timeMill = timeMill;
        this.grade = grade;
        this.shoot = shoot;
        this.collimation = collimation;
        this.send = send;
        this.allGrade = allGrade;
        this.userId = userId;
        this.userName = userName;
        this.userStatus = userStatus;
        this.boutId = boutId;
        this.boutNum = boutNum;
    }

    @Generated(hash = 1145175996)
    public SingleShootDataModel() {
    }

    public static final Creator<SingleShootDataModel> CREATOR = new Creator<SingleShootDataModel>() {
        @Override
        public SingleShootDataModel createFromParcel(Parcel in) {
            return new SingleShootDataModel(in);
        }

        @Override
        public SingleShootDataModel[] newArray(int size) {
            return new SingleShootDataModel[size];
        }
    };

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1284002165)
    private transient SingleShootDataModelDao myDao;

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
        dest.writeInt(shootPreface);
        dest.writeFloat(ring);
        if (shootDataId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(shootDataId);
        }
        dest.writeString(direction);
        dest.writeFloat(timeSecond);
        dest.writeLong(timeMill);
        dest.writeInt(grade);
        dest.writeInt(shoot);
        dest.writeInt(collimation);
        dest.writeInt(send);
        dest.writeInt(allGrade);
        dest.writeLong(userId);
        dest.writeString(userName);
        dest.writeInt(userStatus);
        dest.writeLong(boutId);
        dest.writeInt(boutNum);
        dest.writeTypedList(check);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getShootPreface() {
        return this.shootPreface;
    }

    public void setShootPreface(int shootPreface) {
        this.shootPreface = shootPreface;
    }

    public float getRing() {
        return this.ring;
    }

    public void setRing(float ring) {
        this.ring = ring;
    }

    public Long getShootDataId() {
        return this.shootDataId;
    }

    public void setShootDataId(Long shootDataId) {
        this.shootDataId = shootDataId;
    }

    public String getDirection() {
        return this.direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public float getTimeSecond() {
        return this.timeSecond;
    }

    public void setTimeSecond(float timeSecond) {
        this.timeSecond = timeSecond;
    }

    public long getTimeMill() {
        return this.timeMill;
    }

    public void setTimeMill(long timeMill) {
        this.timeMill = timeMill;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public int getShoot() {
        return this.shoot;
    }

    public void setShoot(int shoot) {
        this.shoot = shoot;
    }

    public int getCollimation() {
        return this.collimation;
    }

    public void setCollimation(int collimation) {
        this.collimation = collimation;
    }

    public int getSend() {
        return this.send;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public int getAllGrade() {
        return this.allGrade;
    }

    public void setAllGrade(int allGrade) {
        this.allGrade = allGrade;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public long getBoutId() {
        return this.boutId;
    }

    public void setBoutId(long boutId) {
        this.boutId = boutId;
    }

    public int getBoutNum() {
        return this.boutNum;
    }

    public void setBoutNum(int boutNum) {
        this.boutNum = boutNum;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1148854482)
    public List<EntryModel> getCheck() {
        if (check == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            EntryModelDao targetDao = daoSession.getEntryModelDao();
            List<EntryModel> checkNew = targetDao._querySingleShootDataModel_Check(id);
            synchronized (this) {
                if (check == null) {
                    check = checkNew;
                }
            }
        }
        return check;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 781702279)
    public synchronized void resetCheck() {
        check = null;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1699736527)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSingleShootDataModelDao() : null;
    }
}
