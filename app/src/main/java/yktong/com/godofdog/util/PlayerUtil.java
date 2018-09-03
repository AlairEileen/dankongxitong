package yktong.com.godofdog.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import yktong.com.godofdog.bean.tel.PlayerProgressBean;
import yktong.com.godofdog.tool.thread.ThreadPool;

/**
 * Created by vampire on 2017/9/5.
 */

public class PlayerUtil {
    private MediaPlayer mediaPlayer;
    private static PlayerUtil instence;
    private String temp = "";
    private boolean isPause;

    private PlayerUtil() {
        playerProgress();
    }

    public static PlayerUtil getInstense() {
        if (instence == null) {
            synchronized (PlayerUtil.class) {
                if (instence == null) {
                    instence = new PlayerUtil();
                }
            }
        }
        return instence;
    }

    //初始化播放器
    public void initMediaPlayer() {
        if (mediaPlayer != null) {

            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        mediaPlayer = new MediaPlayer();
    }

    // 销毁播放素材
    public void destoryPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    //暂停播放
    public void pausePalyer() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }
    }

    public void stopPlayer() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public MediaPlayer playVoice(String fileName) {
        boolean finish = false;
        try {
            if (mediaPlayer == null) {
                initMediaPlayer();
            }
            if (mediaPlayer != null) {
            } else {
                temp = fileName;
                initMediaPlayer();
                play(fileName);
            }
            if (mediaPlayer.isPlaying()) {
                if (temp.equals(fileName)) {
                    Log.d("PlayerUtil", "暂停");
                    //切换到暂停
                    isPause = true;
                    pausePalyer();
                } else {
                    Log.d("PlayerUtil", "切换");
                    //播放其他
                    destoryPlayer();
                    initMediaPlayer();
                    temp = fileName;
                    play(fileName);
                }
            } else {
                if (temp.equals(fileName) && isPause) {
                    Log.d("PlayerUtil", "播放");
                    mediaPlayer.start();
                } else {
                    Log.d("PlayerUtil", "播放新文件");
                    temp = fileName;
                    initMediaPlayer();
                    play(fileName);
                }
            }
            Log.d("PlayerUtil", fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mediaPlayer;
    }

    private void play(String fileName) throws IOException {
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(fileName);
        mediaPlayer.setLooping(false);
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(mp -> mp.start());
    }

    public void seekTo(int progress) {
        int i = mediaPlayer.getDuration() * progress / 100;
        mediaPlayer.seekTo(i);
        if (!mediaPlayer.isPlaying()) {
            pausePalyer();
        }
    }

    private void playerProgress() {
        ThreadPool.thredP.execute(() -> {
            for (; ; ) {
                try {
                    Thread.sleep(100);
                    if (null != mediaPlayer && mediaPlayer.isPlaying()) {
                        int seek = mediaPlayer.getCurrentPosition();
                        int percent = (seek * 100 / mediaPlayer.getDuration());
                        EventBus.getDefault().post(new PlayerProgressBean(percent));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }catch (IllegalStateException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
