package com.example.common_module.db.mode;

import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class ShootDataModel implements Parcelable {
    @Id(autoincrement = true)
    private Long id = null;
    private int shootNum;//人形靶左上角靶号
    private int shootMileType;//100m->1 50m->2 25m->3 距离靶
    @Unique
    private long boutId;//局ID
    private int boutNum;//局序号

    private int currentSeq;//当前发序 todo 暂不存储 非必需
    private float currentRing;//当前环数 todo 暂不存储 非必需
    private float totalRing;// 总环数 全部结束后统计


    private int totalGrade;// 总成绩
    private int totalShoot;// 总据枪
    private int totalCollimation;//总瞄准
    private int totalSend;//总击发
    private int totalAll;//总体
    private long userId;//用户ID
    private Long userIdKey = null;
    private String userName;//用户名称
    //note 参考 #UserModel.userStatus
    private int userStatus;
    //note 不是数据生成时间 按照后台要求 是创建时间也就是第一束激光照射时间
    private long createTime;
    private boolean deleteStatus;//note 删除状态 false 默认不删除  true删除，与历史记录查询时、排名比较时有关联
    @ToMany(referencedJoinProperty = "shootDataId")
    private List<SingleShootDataModel> data;//单个发序的数据

    protected ShootDataModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        shootNum = in.readInt();
        shootMileType = in.readInt();
        boutId = in.readLong();
        boutNum = in.readInt();
        currentSeq = in.readInt();
        currentRing = in.readFloat();
        totalRing = in.readFloat();
        totalGrade = in.readInt();
        totalShoot = in.readInt();
        totalCollimation = in.readInt();
        totalSend = in.readInt();
        totalAll = in.readInt();
        userId = in.readLong();
        if (in.readByte() == 0) {
            userIdKey = null;
        } else {
            userIdKey = in.readLong();
        }
        userName = in.readString();
        userStatus = in.readInt();
        createTime = in.readLong();
        deleteStatus = in.readByte() != 0;
        data = in.createTypedArrayList(SingleShootDataModel.CREATOR);
    }

    @Generated(hash = 599745143)
    public ShootDataModel(Long id, int shootNum, int shootMileType, long boutId, int boutNum,
            int currentSeq, float currentRing, float totalRing, int totalGrade,
            int totalShoot, int totalCollimation, int totalSend, int totalAll, long userId,
            Long userIdKey, String userName, int userStatus, long createTime,
            boolean deleteStatus) {
        this.id = id;
        this.shootNum = shootNum;
        this.shootMileType = shootMileType;
        this.boutId = boutId;
        this.boutNum = boutNum;
        this.currentSeq = currentSeq;
        this.currentRing = currentRing;
        this.totalRing = totalRing;
        this.totalGrade = totalGrade;
        this.totalShoot = totalShoot;
        this.totalCollimation = totalCollimation;
        this.totalSend = totalSend;
        this.totalAll = totalAll;
        this.userId = userId;
        this.userIdKey = userIdKey;
        this.userName = userName;
        this.userStatus = userStatus;
        this.createTime = createTime;
        this.deleteStatus = deleteStatus;
    }

    @Generated(hash = 1105917424)
    public ShootDataModel() {
    }

    public static final Creator<ShootDataModel> CREATOR = new Creator<ShootDataModel>() {
        @Override
        public ShootDataModel createFromParcel(Parcel in) {
            return new ShootDataModel(in);
        }

        @Override
        public ShootDataModel[] newArray(int size) {
            return new ShootDataModel[size];
        }
    };
    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    /** Used for active entity operations. */
    @Generated(hash = 1869347319)
    private transient ShootDataModelDao myDao;

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
        dest.writeInt(shootNum);
        dest.writeInt(shootMileType);
        dest.writeLong(boutId);
        dest.writeInt(boutNum);
        dest.writeInt(currentSeq);
        dest.writeFloat(currentRing);
        dest.writeFloat(totalRing);
        dest.writeInt(totalGrade);
        dest.writeInt(totalShoot);
        dest.writeInt(totalCollimation);
        dest.writeInt(totalSend);
        dest.writeInt(totalAll);
        dest.writeLong(userId);
        if (userIdKey == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(userIdKey);
        }
        dest.writeString(userName);
        dest.writeInt(userStatus);
        dest.writeLong(createTime);
        dest.writeByte((byte) (deleteStatus ? 1 : 0));
        dest.writeTypedList(data);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getShootNum() {
        return this.shootNum;
    }

    public void setShootNum(int shootNum) {
        this.shootNum = shootNum;
    }

    public int getShootMileType() {
        return this.shootMileType;
    }

    public void setShootMileType(int shootMileType) {
        this.shootMileType = shootMileType;
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

    public int getCurrentSeq() {
        return this.currentSeq;
    }

    public void setCurrentSeq(int currentSeq) {
        this.currentSeq = currentSeq;
    }

    public float getCurrentRing() {
        return this.currentRing;
    }

    public void setCurrentRing(float currentRing) {
        this.currentRing = currentRing;
    }

    public float getTotalRing() {
        return this.totalRing;
    }

    public void setTotalRing(float totalRing) {
        this.totalRing = totalRing;
    }

    public int getTotalGrade() {
        return this.totalGrade;
    }

    public void setTotalGrade(int totalGrade) {
        this.totalGrade = totalGrade;
    }

    public int getTotalShoot() {
        return this.totalShoot;
    }

    public void setTotalShoot(int totalShoot) {
        this.totalShoot = totalShoot;
    }

    public int getTotalCollimation() {
        return this.totalCollimation;
    }

    public void setTotalCollimation(int totalCollimation) {
        this.totalCollimation = totalCollimation;
    }

    public int getTotalSend() {
        return this.totalSend;
    }

    public void setTotalSend(int totalSend) {
        this.totalSend = totalSend;
    }

    public int getTotalAll() {
        return this.totalAll;
    }

    public void setTotalAll(int totalAll) {
        this.totalAll = totalAll;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public Long getUserIdKey() {
        return this.userIdKey;
    }

    public void setUserIdKey(Long userIdKey) {
        this.userIdKey = userIdKey;
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

    public long getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public boolean getDeleteStatus() {
        return this.deleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1324684408)
    public List<SingleShootDataModel> getData() {
        if (data == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SingleShootDataModelDao targetDao = daoSession.getSingleShootDataModelDao();
            List<SingleShootDataModel> dataNew = targetDao._queryShootDataModel_Data(id);
            synchronized (this) {
                if (data == null) {
                    data = dataNew;
                }
            }
        }
        return data;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1283600904)
    public synchronized void resetData() {
        data = null;
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
    @Generated(hash = 69438521)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getShootDataModelDao() : null;
    }
}
