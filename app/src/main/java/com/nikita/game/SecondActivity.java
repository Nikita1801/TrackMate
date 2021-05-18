package com.nikita.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class SecondActivity extends AppCompatActivity{
    private Button startButton;
    private Button logoutButton;
    FirebaseUser user;
    DatabaseReference reference;
    TextView score;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        addListennerOnButton();
        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();
        score =(TextView) findViewById(R.id.totalScore);
        reference.child("Score").child(user.getUid()).child("result").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                score.setText(String.valueOf(snapshot.getValue()));
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Что-то пошло не так", Toast.LENGTH_LONG).show();
            }
        });



    }
    public void addListennerOnButton(){
        startButton = (Button) findViewById(R.id.startbutton);
        logoutButton= (Button) findViewById(R.id.logoutbutton);


        startButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public  void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                        startActivity(intent);
                    }
                }
        );

        logoutButton.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public  void onClick(View v){
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );



    }


    }


