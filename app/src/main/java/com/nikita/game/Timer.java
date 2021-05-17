package com.nikita.game;

import android.os.CountDownTimer;

public class Timer extends CountDownTimer {
    public static final int timerInterval = 600;
    public static final int degradationOfScoreInterval = 5;
    private int counter = 0;
    public Timer(){
        super(Integer.MAX_VALUE, timerInterval);
    }


    @Override
    public void onTick(long millisUntilFinished) {
        GameActivity.getInstant().update();
        if(counter == degradationOfScoreInterval){
            GameActivity.getInstant().minusScoreForClick(1);
            counter=0;
        }
        counter++;
    }

    @Override
    public void onFinish() {

    }
}
