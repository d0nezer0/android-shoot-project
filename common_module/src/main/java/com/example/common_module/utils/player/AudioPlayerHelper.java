package com.example.common_module.utils.player;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.PlaybackParams;


import com.blankj.utilcode.utils.LogUtils;
import com.blankj.utilcode.utils.StringUtils;
import com.example.common_module.common.Constant;
import com.example.common_module.utils.SPUtils;

import java.text.DecimalFormat;
import java.util.Arrays;

public class AudioPlayerHelper {
    private Context context;
    private MediaPlayer mediaPlayer;
    private int currentTrackIndex = 0;
    private String[] audioFiles;
    private boolean isPaused = false;
    private boolean isReleased = false;
    private OnCompletionListener onCompletionListener;
    private DecimalFormat df = new DecimalFormat("#.#");

    public AudioPlayerHelper(Context context) {
        this.context = context;
        initializeMediaPlayer();
    }

    public AudioPlayerHelper()
    {

    }

    private void initializeMediaPlayer() {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            if (onCompletionListener != null) {
                onCompletionListener.onTrackComplete(currentTrackIndex);
            }
            playNextTrack();
        });
    }

    public void setOnCompletionListener(OnCompletionListener listener) {
        this.onCompletionListener = listener;
    }

    /**
     * 播放音频处理
     * @param ringName 环数
     * @param wayName 方向
     * @param isGun92 是否92式手枪
     */
    public void play(String ringName, String wayName, boolean isGun92) {
        this.audioFiles = convertFileName(ringName, wayName, isGun92);
        currentTrackIndex = 0;

        if (mediaPlayer != null && !isReleased) {
            if (mediaPlayer.isPlaying())
                stop();
            if (isPaused) {
                mediaPlayer.start();
                isPaused = false;
            } else {
                playTrack(currentTrackIndex);

            }
        } else {
            playTrack(currentTrackIndex);
        }
    }

    public void play(String ring, boolean isGun92) {
        String nameRing = convertFileNameRing(ring);
        String gunSound = "raw_boom";
        if (!isGun92)
            gunSound = "raw_boom_95";
        this.audioFiles = new String[]{gunSound, nameRing};

        currentTrackIndex = 0;
        if (mediaPlayer != null && !isReleased) {
            if (mediaPlayer.isPlaying())
                stop();
            if (isPaused) {
                mediaPlayer.start();
                isPaused = false;
            } else {
                playTrack(currentTrackIndex);

            }
        } else {
            playTrack(currentTrackIndex);
        }
    }

    public void pause() {
        if (mediaPlayer != null && !isReleased && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPaused = true;
        }
    }

    public void stop() {
        if (mediaPlayer != null && !isReleased) {
            mediaPlayer.stop();
            isPaused = false;
        }
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
            isReleased = true;
        }
    }

    private void playTrack(int trackIndex) {
        if (mediaPlayer != null && !isReleased) {
            if (trackIndex >= 0 && trackIndex < audioFiles.length) {
                mediaPlayer.reset();
                int resId = context.getResources().getIdentifier(audioFiles[trackIndex], "raw", context.getPackageName());
                if (resId != 0) {
                    mediaPlayer = MediaPlayer.create(context, resId);
                    mediaPlayer.setOnCompletionListener(mp -> {
                        if (onCompletionListener != null) {
                            onCompletionListener.onTrackComplete(currentTrackIndex);
                        }
                        playNextTrack();
                    });
                    mediaPlayer.start();
//                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                        PlaybackParams playbackParams=mediaPlayer.getPlaybackParams();
//                        playbackParams.setSpeed(2.0f);
//                        mediaPlayer.setPlaybackParams(playbackParams);
//                    }
                    currentTrackIndex = trackIndex;
                }
            }
        }
    }

    private void playNextTrack() {
        int nextTrackIndex = currentTrackIndex + 1;
        if (nextTrackIndex < audioFiles.length) {
            playTrack(nextTrackIndex);
        }
    }

    public interface OnCompletionListener {
        void onTrackComplete(int trackIndex);
    }

    public String[] convertFileName(String ringName, String wayName, boolean isGun92) {
        /*if (ringName.length() == 1)
            ringName = ringName + "_0";
        if (ringName.length() > 3)
            ringName = ringName.substring(0, 4);
        if (!ringName.contains(".") && !ringName.contains("_"))
            throw new IllegalArgumentException("环数必须包含小数点: error ringName" + ringName);*/

        try{
            ringName = df.format(Float.parseFloat(ringName));
        }catch (Exception e){
            ringName = "raw_tuoba";
        }
        if(ringName.equals("11")){
            ringName = "raw_10_10";
        }else if (ringName.equals("0")) {
            ringName = "raw_tuoba";
        }else if (ringName.length() == 1 || ringName.equals("10")) {
            ringName = "raw_" + ringName + "_0";
        }else {
        ringName = "raw_" + ringName.replace(".", "_");
        }


        String[] files = new String[3];
        if (isGun92)
            files[0] = "raw_boom";
        else
            files[0] = "raw_boom_95";

        String way = "tuoba";
        if (wayName.equals("右上")) {
            way = "right_up";
        } else if (wayName.equals("左上")) {
            way = "left_up";
        } else if (wayName.equals("左下")) {
            way = "left_down";
        } else if (wayName.equals("右下")) {
            way = "right_down";
        } else if (wayName.equals("正上")) {
            way = "up";
        } else if (wayName.equals("正下")) {
            way = "down";
        } else if (wayName.equals("正右")) {
            way = "right";
        } else if (wayName.equals("正左")) {
            way = "left";
        } else if (wayName.equals("正中")) {
            way = "center";
        } else if (wayName.equals("脱靶")) {
            way = "tuoba";
        }
        if (SPUtils.getInstance((Application) context.getApplicationContext()).getBoolean(Constant.IS_NEED_WAY)) {//单人模式时IS_NEED_WAY为true
            //files[1] = "raw_" + ringName.replace(".", "_");
            files[1] = ringName;
            if (way.equals("tuoba") || ringName.equals("raw_10_10")){
                files[2] = "raw_ull"; //此处改成一个不存在的文件名，0环已经报过脱靶了，不用再报了。 如果是10。10环播报"正中靶心"，方向也不用播报了。
            }else {
                files[2] = "raw_" + way;
            }
            return files;
        } else {
            //files[1] = "raw_" + ringName.replace(".", "_");
            files[1] = ringName;
            return Arrays.copyOf(files, 2);
        }

    }

    public  String convertFileNameRing(String ringName) {
        try{
            ringName = df.format(Float.parseFloat(ringName));
        }catch (Exception e){
            return "raw_tuoba";
        }
        if (ringName.equals("0")) {
            return "raw_tuoba";
        }
        if (ringName.length() == 1 || ringName.equals("10")) {
            return "raw_" + ringName + "_0";
        }
        return "raw_" + ringName.replace(".", "_");
    }
}

