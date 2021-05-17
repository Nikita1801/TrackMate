package com.nikita.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity{
    private Button startButton;
    private Button logoutButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_activity);
        addListennerOnButton();
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


