package com.example.common_module.common;

public class Constant {
    public static final float RADIUS = 160.0F;

    //串口 波特率
    public static final String SPORT_NAME = "/dev/ttyS3";
    public static final int BAUD_RATE = 9600;

    /**
     * SP Key
     */
    public static final String CUR_BOUT = "cur_bout";//当前局 每次进入查询 之前是否为中断 正常结束 比如4局 当前为3 则查询当前用户的最近3局
    public static final String CUR_FAXU = "cur_faxu";//当前发序
    public static final String TOTAL_BOUT = "total_bout";
    public static final String BULLET_COUNT = "bullet_count";//每一发序个数
    public static final String IS_FINISH = "is_finish";
    public static final String HOST = "host";
    public static final String PORT = "port";
    public static final String USER_NAME = "user_name";
    public static final String USER_ID = "user_id";
    public static final String DEVICE_NUM = "device_num";
    public static final String INIT_MODE_DATA = "init_mode_data";
    public static final String IS_NEED_WAY = "is_need_way";//def false
    public static final String CUSTOM_USER_ID = "custom_user_id";//自由射击 标识  最多1000 todo 后期肯定有问题 但端要求这么设计
    public static final String CUSTOM_BOUT_ID = "custom_bout_id";//bout_id

}
