package com.example.common_module.db.mode;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.common_module.db.EnterInfo;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.io.Serializable;

@Entity
public class EntryModel implements Parcelable {

    @Id(autoincrement = true)
    private Long id = null;
    //帧头 目前固定为[A5]认为是有效byte数组开头 字段为转化十进制数据A5=165
    /**
     * 帧头 目前固定为[A5]认为是有效byte数组开头 字段为转化十进制数据A5=165
     */
    private int head;
    //全部数据的字节长度 06->6个byte为开枪数据->没有坐标 0A->10个byte为瞄准数据->有坐标
    /**
     *全部数据的字节长度 06->6个byte为开枪数据->没有坐标 0A->10个byte为瞄准数据->有坐标
     */
    private int len;
    //根据硬件来确定，目前测试数据是7E=126 代表胸环靶
    /**
     *根据硬件来确定，目前测试数据是7E=126 代表胸环靶
     */
    private int type;
    //设备编号 第几把枪/靶id 0-255
    /**
     * 设备编号 第几把枪/靶id 0-255
     */
    private int deviceID;
    //命令 01->发送坐标
    /**
     *命令 01->发送坐标
     */
    private int cmd;
    //这个不是时间戳 而是-> 距离0点的毫秒数  一天有24小时==1440分钟==86400000豪秒
    /**
     *这个不是时间戳 而是-> 距离0点的毫秒数  一天有24小时==1440分钟==86400000豪秒
     */
    private long time;
    //x坐标 原始double
    /**
     *x坐标 原始double
     */
    private float x;
    //y坐标 原始double
    /**
     *y坐标 原始double
     */
    private float y;

    //只有7E，胸环靶 才有此字段
    /**
     *只有7E，胸环靶 才有此字段
     */
    private int gunId;
    //环数 有x,y 勾股定理计算所得 采取四舍五入
    /**
     * 环数 有x,y 勾股定理计算所得 采取四舍五入
     */
    private float ring;
    //校验 A5+0A+7E+03+01+FF+83+00+64)%FF=17
    /**
     *校验 A5+0A+7E+03+01+FF+83+00+64)%FF=17
     */
    private boolean checkVerify;
    //单发序ID
    /**
     *单发序ID
     */
    private long singleShootId;
    //用户ID
    /**
     *用户ID
     */
    private long userId;

    //参考 #UserModel.userStatus
    /**
     *参考 #UserModel.userStatus
     */
    private int userStatus;
    /**
     *note 删除状态 false 默认不删除  true删除，与历史记录查询时、排名比较时有关联
     */
    private boolean deleteStatus;//note 删除状态 false 默认不删除  true删除，与历史记录查询时、排名比较时有关联
    /**
     *判断脱靶原始数据
     */
    private int gunInAndMiss;//判断脱靶原始数据
    /**
     *是否脱靶  1 脱靶 0上靶
     */
    private boolean miss;//是否脱靶  1 脱靶 0上靶

    //对应TargetPointView#mLineColors
    /**
     *对应TargetPointView#mLineColors
     */
    private int status;


    protected EntryModel(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        head = in.readInt();
        len = in.readInt();
        type = in.readInt();
        deviceID = in.readInt();
        cmd = in.readInt();
        time = in.readLong();
        x = in.readFloat();
        y = in.readFloat();
        gunId = in.readInt();
        ring = in.readFloat();
        checkVerify = in.readByte() != 0;
        singleShootId = in.readLong();
        userId = in.readLong();
        userStatus = in.readInt();
        deleteStatus = in.readByte() != 0;
        gunInAndMiss = in.readInt();
        miss = in.readByte() != 0;
        status = in.readInt();
    }

    @Generated(hash = 1478372614)
    public EntryModel(Long id, int head, int len, int type, int deviceID, int cmd,
                      long time, float x, float y, int gunId, float ring, boolean checkVerify,
                      long singleShootId, long userId, int userStatus, boolean deleteStatus,
                      int gunInAndMiss, boolean miss, int status) {
        this.id = id;
        this.head = head;
        this.len = len;
        this.type = type;
        this.deviceID = deviceID;
        this.cmd = cmd;
        this.time = time;
        this.x = x;
        this.y = y;
        this.gunId = gunId;
        this.ring = ring;
        this.checkVerify = checkVerify;
        this.singleShootId = singleShootId;
        this.userId = userId;
        this.userStatus = userStatus;
        this.deleteStatus = deleteStatus;
        this.gunInAndMiss = gunInAndMiss;
        this.miss = miss;
        this.status = status;
    }

    /*
    Long id, int head, int len, int type, int deviceID, int cmd,
                      long time, float x, float y, int gunId, float ring, boolean checkVerify,
                      long singleShootId, long userId, int userStatus, boolean deleteStatus,
                      int gunInAndMiss, boolean miss, int status
     */
    @Override
    public String toString() {
        return "EntryModel{" +
                "id=" + id +
                ", head=" + head +
                ", len=" + len +
                ", type=" + type +
                ", deviceID=" + deviceID +
                ", cmd=" + cmd +
                ", time=" + time +
                ", x=" + x +
                ", y=" + y +
                ", gunId=" + gunId +
                ", ring=" + ring +
                ", checkVerify=" + checkVerify +
                ", singleShootId=" + singleShootId +
                ", userId=" + userId +
                ", userStatus=" + userStatus +
                ", deleteStatus=" + deleteStatus +
                ", gunInAndMiss=" + gunInAndMiss +
                ", miss=" + miss +
                ", status=" + status +
                '}';
    }


    @Generated(hash = 735698284)
    public EntryModel() {
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeInt(head);
        dest.writeInt(len);
        dest.writeInt(type);
        dest.writeInt(deviceID);
        dest.writeInt(cmd);
        dest.writeLong(time);
        dest.writeFloat(x);
        dest.writeFloat(y);
        dest.writeInt(gunId);
        dest.writeFloat(ring);
        dest.writeByte((byte) (checkVerify ? 1 : 0));
        dest.writeLong(singleShootId);
        dest.writeLong(userId);
        dest.writeInt(userStatus);
        dest.writeByte((byte) (deleteStatus ? 1 : 0));
        dest.writeInt(gunInAndMiss);
        dest.writeByte((byte) (miss ? 1 : 0));
        dest.writeInt(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<EntryModel> CREATOR = new Creator<EntryModel>() {
        @Override
        public EntryModel createFromParcel(Parcel in) {
            return new EntryModel(in);
        }

        @Override
        public EntryModel[] newArray(int size) {
            return new EntryModel[size];
        }
    };

    /**
     * 获取数据类型
     */
//    public EnterInfo.CMD_TYPE getCmdType() {
//        if (type == 0x7E && cmd == 0x01) {//瞄准
//            return EnterInfo.CMD_TYPE.AIM;
//        } else if (type == 0x01) {//开枪
//            return EnterInfo.CMD_TYPE.SHOOT;
//        } else {//未知
//            return EnterInfo.CMD_TYPE.UNKNOWN;
//        }
//    }
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getHead() {
        return this.head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getLen() {
        return this.len;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDeviceID() {
        return this.deviceID;
    }

    public void setDeviceID(int deviceID) {
        this.deviceID = deviceID;
    }

    public int getCmd() {
        return this.cmd;
    }

    public void setCmd(int cmd) {
        this.cmd = cmd;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getGunId() {
        return this.gunId;
    }

    public void setGunId(int gunId) {
        this.gunId = (gunId & 0x0F);
    }

    public float getRing() {
        return this.ring;
    }

    public void setRing(float ring) {
        this.ring = ring;
    }

    public boolean getCheckVerify() {
        return this.checkVerify;
    }

    public void setCheckVerify(boolean checkVerify) {
        this.checkVerify = checkVerify;
    }

    public long getSingleShootId() {
        return this.singleShootId;
    }

    public void setSingleShootId(long singleShootId) {
        this.singleShootId = singleShootId;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getUserStatus() {
        return this.userStatus;
    }

    public void setUserStatus(int userStatus) {
        this.userStatus = userStatus;
    }

    public boolean getDeleteStatus() {
        return this.deleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    public int getGunInAndMiss() {
        return this.gunInAndMiss;
    }

    public void setGunInAndMiss(int gunInAndMiss) {
        this.gunInAndMiss = gunInAndMiss;
    }

    public boolean getMiss() {
        return this.miss;
    }

    public void setMiss(boolean miss) {
        this.miss = miss;
    }

    public enum Status {
        START(Color.GREEN), NORMAL(Color.RED), SHOOT_AFTER(Color.YELLOW);

        private int color;

        Status(int color) {
            this.color = color;
        }

        public int getColor() {
            return color;
        }
    }

    public EnterInfo.CMD_TYPE getCmdType() {
        if ((gunInAndMiss & 0x80) == 0x80)  // 0X80 --> 1000 0000
            return EnterInfo.CMD_TYPE.SHOOT;
        else if ((gunInAndMiss & 0x80) == 0)
            return EnterInfo.CMD_TYPE.AIM;
        else //未知
            return EnterInfo.CMD_TYPE.UNKNOWN;
    }

    public int getStatus() {
        return this.status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
