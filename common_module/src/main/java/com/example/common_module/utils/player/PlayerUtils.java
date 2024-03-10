package com.example.common_module.utils.player;

import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.util.ArrayDeque;
import java.util.Deque;

public class PlayerUtils {

    Deque<String> deque = new ArrayDeque<>();//队列播放

    private PlayerUtils() {

    }

    public static MediaPlayer mediaPlayer;
    private final static PlayerUtils instance = new PlayerUtils();

    public static PlayerUtils getInstance() {
        initMediaPlayer();
        return instance;
    }

    private static void initMediaPlayer() {
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new OnCompletionListener());
    }

    public static void play(AssetManager assetManager, String ringFileName, String wayFileName) {


    }


    static class OnCompletionListener implements MediaPlayer.OnCompletionListener {

        @Override
        public void onCompletion(MediaPlayer mp) {

        }
    }
}
