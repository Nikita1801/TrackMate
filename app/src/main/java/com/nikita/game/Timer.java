package com.nikita.game;

import android.os.CountDownTimer;

public class Timer extends CountDownTimer {
    public static final int timerInterval = 600;
    public Timer(){
        super(Integer.MAX_VALUE, timerInterval);
    }


    @Override
    public void onTick(long millisUntilFinished) {
        GameActivity.getInstant().update();
    }

    @Override
    public void onFinish() {

    }
}
