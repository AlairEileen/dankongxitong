package yktong.com.godofdog.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import yktong.com.godofdog.bean.tel.PlayerProgressBean;
import yktong.com.godofdog.tool.thread.ThreadPool;

/**
 * Created by vampire on 2017/9/19.
 */

public class PlayerService extends Service {

    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
            // TODO: 2017/9/19  error
            return false;
        });
//        playerProgress();
    }

    private void playVoice(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(mp -> {
                mp.start();
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
