package com.example.common_module.utils;

import android.content.res.AssetManager;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    /**
     * //        byte[] bytes = FileUtils.readBinaryFile(getApplicationContext(), "xy.bin");
     * //        if (bytes != null) {
     * //            // 处理读取到的字节数组
     * //            String hexString = FileUtils.bytesToHexString(bytes);
     * //            Log.e(TAG, hexString);
     * //        } else {
     * //            // 处理读取失败的情况
     * //        }
     *
     * @param context
     * @param fileName
     * @return
     */
    public static byte[] readBinaryFile(Context context, String fileName) {
        AssetManager assetManager = context.getAssets();
        try {
            InputStream inputStream = assetManager.open(fileName);
            int fileSize = inputStream.available();
            byte[] buffer = new byte[fileSize];
            inputStream.read(buffer);
            inputStream.close();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = String.format("%02X", b);
            sb.append(hex).append(" ");
        }
        return sb.toString();
    }

    public static List<byte[]> readBinaryFileToGroups(Context context, String fileName, String tag) {
        List<byte[]> groups = new ArrayList<>();

        try {
            AssetManager assetManager = context.getAssets();
            InputStream inputStream = assetManager.open(fileName); // 替换为实际的二进制文件名

            byte[] buffer = new byte[11]; // 暂定最大组长度为10，可以根据实际情况调整

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    if (buffer[i] == (byte) 0xA5) {
                        if (i < bytesRead - 1) {
                            byte nextByte = buffer[i + 1];
                            if (nextByte == (byte) 0x0B) {
                                if (i + 11 <= bytesRead) {
                                    byte[] group = new byte[11];
                                    System.arraycopy(buffer, i, group, 0, 11);
                                    groups.add(group);
                                }
                            } else if (nextByte == (byte) 0x06) {
                                if (i + 11 <= bytesRead) {
                                    byte[] group = new byte[11];
                                    System.arraycopy(buffer, i, group, 0, 11);
                                    groups.add(group);
                                }
                            }
                        }
                    }
                }
            }

            // 打印归类的组
            for (byte[] group : groups) {
                printByteArray(tag, group);
            }

            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return groups;
    }

    public static void printByteArray(String tag, byte[] bytes) {
        for (byte b : bytes) {
            Log.e(tag, String.format("%02X ", b));
        }
        Log.e(tag, "--------------");
    }
}