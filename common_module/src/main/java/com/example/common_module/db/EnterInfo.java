package com.example.common_module.db;

public class EnterInfo {

    public static final int ST_WAIT_HEAD = 0xA5;//A5
    public static final int ST_WAIT_LEN_0A = 0x0A;//0A-06
    public static final int ST_WAIT_LEN_06 = 0x06;//0A-06
    public static final int ST_WAIT_TYPE_7E = 0x7E;//胸怀靶 Note 这个需要固定 新增后在这里增加

    public  enum CMD_TYPE {
        SHOOT,AIM,UNKNOWN
    }

}
