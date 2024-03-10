package com.example.net_module;

public class Common {


    //手机号的正则  11位手机号
   public static String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

    //所有请求的Base_Url
    public static String API_URL = "http://192.168.1.5:9999/api/";
//    public static String API_URL = "";
    //上传图片的最大大小860kb
    long MAX_UPLOAD_IMAGE_LENGTH = 860 * 1024;

    public static String NOTI_KEY_IS_SOUND="NOTI_KEY_IS_SOUND";
    public static String NOTI_KEY_IS_VIBRATE="NOTI_KEY_IS_VIBRATE";
    public static String NOTI_KEY_IS_LIGTHS="NOTI_KEY_IS_LIGTHS";
}