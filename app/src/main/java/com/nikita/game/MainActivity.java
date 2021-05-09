package com.nikita.game;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.nikita.game.permissions.PermissionsActivity;
import com.nikita.game.permissions.PermissionsChecker;
import com.nikita.game.renderer.RendererFactory;

public class MainActivity extends AppCompatActivity implements Visualizer.OnDataCaptureListener {

    //забрать
    private static final int CAPTURE_SIZE = 256;
    private static final int REQUEST_CODE = 0;
    static final String[] PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};

    //забрать
    private Visualizer visualiser;


    private WaveformView waveformView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        waveformView = (WaveformView) findViewById(R.id.waveform_view);
        RendererFactory rendererFactory = new RendererFactory();
        waveformView.setRenderer(rendererFactory.createSimpleWaveformRenderer(Color.GREEN, Color.DKGRAY));
    }

    //забрать
    @Override
    protected void onResume() {
        super.onResume();
        PermissionsChecker checker = new PermissionsChecker(this);

        if (checker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            startVisualiser();
        }
    }

    //забрать
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    //забрать
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    //забрать
    private void startVisualiser() {
        visualiser = new Visualizer(0);
        visualiser.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, false);
        visualiser.setCaptureSize(CAPTURE_SIZE);
        visualiser.setEnabled(true);
    }


    @Override
    public void onWaveFormDataCapture(Visualizer thisVisualiser, byte[] waveform, int samplingRate) {
        if (waveformView != null) {
            waveformView.setWaveform(waveform);
        }
    }


    @Override
    public void onFftDataCapture(Visualizer thisVisualiser, byte[] fft, int samplingRate) {
        // NO-OP
    }

    //забрать
    @Override
    protected void onPause() {
        if (visualiser != null) {
            visualiser.setEnabled(false);
            visualiser.release();
            visualiser.setDataCaptureListener(null, 0, false, false);
        }
        super.onPause();
    }
}
