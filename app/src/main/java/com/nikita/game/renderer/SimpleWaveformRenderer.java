package com.nikita.game.renderer;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import com.nikita.game.GameActivity;
import com.nikita.game.peakdetector.CFG;

import java.util.ArrayList;
import java.util.Arrays;

class SimpleWaveformRenderer implements WaveformRenderer {
    private static final int Y_FACTOR = 0xFF;
    private static final float HALF_FACTOR = 0.5f;

    private final int backgroundColour;
    private final Paint foregroundPaint;
    private final Path waveformPath;
    private float ypast;

    static SimpleWaveformRenderer newInstance( int backgroundColour, int foregroundColour) {
        Paint paint = new Paint();
        paint.setColor(foregroundColour);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        Path waveformPath = new Path();
        return new SimpleWaveformRenderer(backgroundColour, paint, waveformPath);
    }

    SimpleWaveformRenderer( int backgroundColour, Paint foregroundPaint, Path waveformPath) {
        this.backgroundColour = backgroundColour;
        this.foregroundPaint = foregroundPaint;
        this.waveformPath = waveformPath;
    }

    @Override
    public void render(Canvas canvas, byte[] waveform) {
        canvas.drawColor(backgroundColour);
        float width = canvas.getWidth();
        float height = canvas.getHeight();
        waveformPath.reset();
        Log.d("SimpleWaveformRenderer ", (waveform == null) + "");
        if (waveform != null) {
            maxWaveform(waveform);

        } else {
            renderBlank(width, height);
        }
        canvas.drawPath(waveformPath, foregroundPaint);
    }

    private void renderWaveform(byte[] waveform, float width, float height) {
        float xIncrement = width / (float) (waveform.length);
        float yIncrement = height / Y_FACTOR;
        int halfHeight = (int) (height * HALF_FACTOR);
        ArrayList intArray = new ArrayList<Integer>();
        waveformPath.moveTo(0, halfHeight);
        for (int i = 1; i < waveform.length; i++) {
            float yPosition = waveform[i] > 0 ? height - (yIncrement * waveform[i]) : -(yIncrement * waveform[i]);
            waveformPath.lineTo(xIncrement * i, yPosition);
            Log.d("wave", "value: x= "+ xIncrement*i +" ; y= "+ yPosition + ";");
/*            if(yPosition < ypast){
                //вызов метода, нашли ypast как максимум локальный
                Log.d("wave", "found local maks value: x= "+ xIncrement*i +" ; y= "+ yPosition + ";");
            }
            ypast = yPosition;*/

            intArray.add((int) yPosition);
        }
        waveformPath.lineTo(width, halfHeight);

         int peak = CFG.findPeak(CFG.convertIntegers(intArray));// не работает
         Log.d("wave", "peak is find in:" + peak + "; with value: " + intArray.get(peak));
    }

    private void maxWaveform(byte[] waveform){
        int max = 0;
        for(int i = 0; i < waveform.length; i++){
            Log.d("SimpleWaveformRenderer ", max + "outer");
            if (waveform[i] > max){

                max = waveform[i];
                Log.d("SimpleWaveformRenderer ", max + "");
                GameActivity.getInstant().MoveButton();
            }
        }
    }

    private void renderBlank(float width, float height) {
        int y = (int) (height * HALF_FACTOR);
        waveformPath.moveTo(0, y);
        waveformPath.lineTo(width, y);
    }
}
