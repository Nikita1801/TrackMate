package com.nikita.game;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nikita.game.permissions.PermissionsActivity;
import com.nikita.game.permissions.PermissionsChecker;

import org.jetbrains.annotations.NotNull;

public class GameActivity extends Activity implements Visualizer.OnDataCaptureListener {

    final static String ACTIVITY_TAG = "GameActivity";
    public final int timerInterval = 800;
    private boolean sound;
    public int Score = 0;
    FirebaseUser user;
    DatabaseReference reference;
    private  int remoteScore;

    //from main
    private static final int CAPTURE_SIZE = 256;
    private static final int REQUEST_CODE = 0;
    static final String[] PERMISSIONS = new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.MODIFY_AUDIO_SETTINGS};

    //from main
    private Visualizer visualiser;


    private int viewWidth;
    private int viewHeight;

    Button btn;
    Timer t;
    ImageButton imageButton;
    TextView scoreTextView;


    static GameActivity instant;
    public static GameActivity getInstant(){
        return instant;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        t = new Timer();
        t.start();
        instant = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        imageButton = (ImageButton) findViewById(R.id.imageButton2);

        scoreTextView = findViewById(R.id.scoreView);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();



        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constr);
//        btn = new Button(this);
//        btn.setText("Point");
//        btn.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT));
//        btn.setId(View.generateViewId());
//        constraintLayout.addView(btn);
//        int x = 100;
//        int y = 505;
//        ConstraintSet set = new ConstraintSet();
//        set.clone(constraintLayout);
//        set.connect(btn.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, x);
//        set.connect(btn.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, y);
//        set.applyTo(constraintLayout);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ChangeButtonPosition(v);
//            }
//        });


        reference.child("Score").child(user.getUid()).child("result").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                remoteScore = Integer.parseInt(String.valueOf(snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeButtonPosition(v);
                addScoreForClick(20);
                refreshScore(Score);
                t.resetCounter();

            }
        });



    }

    private void refreshScore(int score){
        scoreTextView.setText("Score: " + score);
    }

    private void addScoreForClick(int score) {
        Score += score;
        Log.d(ACTIVITY_TAG, "score now is " + Score );
    }

    public void minusScoreForClick(int score) {
        Score -= score;
        if(Score <0){
            Score = 0;
        }
        Log.d(ACTIVITY_TAG, "score now is " + Score );
        refreshScore(Score);
    }

    private void ChangeButtonPosition (View button){
        ConstraintLayout constraintLayout = (ConstraintLayout)findViewById(R.id.constr);
        ConstraintSet set = new ConstraintSet();
        set.clone(constraintLayout);

        int  x = (int) (constraintLayout.getWidth()*Math.random());
        int y = (int) (constraintLayout.getHeight()*Math.random());

        if(x <= 30){
            x =  2* button.getWidth();
        } else if(x >= constraintLayout.getWidth() - 30){
            x = constraintLayout.getWidth() - 2 * button.getWidth();
        }



        if(y <= 30){
            y = 2* button.getHeight();
        } else if(y >= constraintLayout.getHeight() - 30){
            y = constraintLayout.getHeight() - 2* button.getHeight();
        }

        set.connect(button.getId(), ConstraintSet.LEFT, constraintLayout.getId(), ConstraintSet.LEFT, x);
        set.connect(button.getId(), ConstraintSet.TOP, constraintLayout.getId(), ConstraintSet.TOP, y);
        set.applyTo(constraintLayout);
    }
    public  void MoveButton(){
        if (imageButton != null ){
            ChangeButtonPosition(imageButton);
        }
    }

    @Override
    public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
        maxWaveform(waveform);
    }

    @Override
    public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {

    }



    // everything below from main

    //from main
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

    //from main
    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    //from main
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    //from main
    private void startVisualiser() {
        visualiser = new Visualizer(0);
        visualiser.setDataCaptureListener(this, Visualizer.getMaxCaptureRate(), true, false);
        visualiser.setCaptureSize(CAPTURE_SIZE);
        visualiser.setEnabled(true);
    }

    //from main
    @Override
    protected void onPause() {
        super.onPause();
        if (visualiser != null) {
            visualiser.setEnabled(false);
            visualiser.release();
            visualiser.setDataCaptureListener(null, 0, false, false);
        }




    }

    private void maxWaveform(byte[] waveform){
        sound = false;
        int max = 0;
        for(int i = 0; i < waveform.length; i++){
//            Log.d(ACTIVITY_TAG, max + " outer");
            if (waveform[i] > max){

                max = waveform[i];
 //               Log.d(ACTIVITY_TAG, max + " ");
                sound = true;
  //              GameActivity.getInstant().MoveButton();
            }
        }
    }
    protected void update(){
        if (sound){
            GameActivity.getInstant().MoveButton();
        }
    }


    @Override
    protected void onDestroy() {

        if (Score > remoteScore){
            reference.child("Score").child(user.getUid()).child("result").setValue(Score).addOnCompleteListener(new OnCompleteListener<Void>() {

                @Override
                public void onComplete(@NonNull @NotNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(GameActivity.this, "Рекорд сохранен", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(GameActivity.this, "Что-то пошло не так", Toast.LENGTH_LONG).show();
                }
            });
        }

        super.onDestroy();
    }
}


