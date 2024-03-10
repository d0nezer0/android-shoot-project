package com.example.common_module.utils;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class AssetFileReader {
    public static List<String> readAssetFile(Context context, String fileName) {
        List<String> lines = new ArrayList<>();
        try {
            // 打开文件流
            InputStream inputStream = context.getAssets().open(fileName);

            // 创建一个 InputStreamReader 和 BufferedReader 来逐行读取文件
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                // 将每行添加到 List 集合中
                lines.add(line);
            }

            // 关闭流
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }
}

