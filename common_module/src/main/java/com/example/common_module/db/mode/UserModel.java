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
import org.greenrobot.greendao.annotation.Unique;


@Entity
public class UserModel implements  Parcelable {
    @Id(autoincrement = true)
    private Long id = null;
    private String name;
    private long createTime;//创建时间
    private long lastLoginTime;//上次登陆的时间
    private int lastUserNum;//第几号
    @Unique
    private long userId;//用户Id
    private int totalBout;//总局数
    private int curBout;//当前第几局


    private int userStatus;//用户类型 0 默认值 统一射击 1 自由射击  自由射击不参与排名 可以改名字 note 个人理解可以出现同名不同人
    @ToMany(referencedJoinProperty = "userIdKey")
    private List<ShootDataModel> shootData;//射击数据


    protected UserModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        createTime = in.readLong();
        lastLoginTime = in.readLong();
        lastUserNum = in.readInt();
        userId = in.readLong();
        totalBout = in.readInt();
        curBout = in.readInt();
        userStatus = in.readInt();
        shootData = in.createTypedArrayList(ShootDataModel.CREATOR);
    }

    @Generated(hash = 45005139)
    public UserModel(Long id, String name, long createTime, long lastLoginTime,
            int lastUserNum, long userId, int totalBout, int curBout, int userStatus) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
        this.lastLoginTime = lastLoginTime;
        this.lastUserNum = lastUserNum;
        this.userId = userId;
        this.totalBout = totalBout;
        this.curBout = curBout;
        this.userStatus = userStatus;
    }

    @Generated(hash = 782181818)
    public UserModel() {
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1229314203)
    private transient UserModelDao myDao;

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
        dest.writeString(name);
        dest.writeLong(createTime);
        dest.writeLong(lastLoginTime);
        dest.writeInt(lastUserNum);
        dest.writeLong(userId);
        dest.writeInt(totalBout);
        dest.writeInt(curBout);
        dest.writeInt(userStatus);
        dest.writeTypedList(shootData);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public long getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getLastUserNum() {
        return this.lastUserNum;
    }

    public void setLastUserNum(int lastUserNum) {
        this.lastUserNum = lastUserNum;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getTotalBout() {
        return this.totalBout;
    }

    public void setTotalBout(int totalBout) {
        this.totalBout = totalBout;
    }

    public int getCurBout() {
        return this.curBout;
    }

    public void setCurBout(int curBout) {
        this.curBout = curBout;
    }

    public int getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 928859029)
    public List<ShootDataModel> getShootData() {
        if (shootData == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ShootDataModelDao targetDao = daoSession.getShootDataModelDao();
            List<ShootDataModel> shootDataNew = targetDao._queryUserModel_ShootData(id);
            synchronized (this) {
                if (shootData == null) {
                    shootData = shootDataNew;
                }
            }
        }
        return shootData;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 568327352)
    public synchronized void resetShootData() {
        shootData = null;
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

    public String toString() {
        return "id = " + id +
                ", name = " + name +
                ", userId = " + userId +
                ", createTime = " + createTime +
                ", lastLoginTime = " + lastLoginTime +
                ", lastUserNum = " + lastUserNum +
                ", totalBout = " + totalBout +
                ", curBout = " + curBout +
                ", userStatus = " + userStatus;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 359156521)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserModelDao() : null;
    }
}

