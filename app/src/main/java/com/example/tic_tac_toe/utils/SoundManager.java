package com.example.tic_tac_toe.utils;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.tic_tac_toe.R;

public class SoundManager {

    private final Context context;

    public SoundManager(Context context) {
        this.context = context;
    }

    public void playClick() {
        playSound(R.raw.click);
    }

    public void playWin() {
        playSound(R.raw.win);
    }

    public void playLose() {
        playSound(R.raw.lose);
    }

    public void playDraw() {
        playSound(R.raw.draw);
    }

    private void playSound(int resId) {
        MediaPlayer player = MediaPlayer.create(context, resId);
        if (player != null) {
            player.setOnCompletionListener(MediaPlayer::release);
            player.start();
        }
    }
}
