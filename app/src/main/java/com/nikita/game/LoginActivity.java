package com.nikita.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;


public class LoginActivity extends AppCompatActivity implements  View.OnClickListener{
    private EditText ETEmailAddress;
    private EditText ETPassword;
    private Button signIn;
    private Button registration;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull @org.jetbrains.annotations.NotNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
            }
        };

        ETEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        ETPassword = (EditText) findViewById(R.id.editTextTextPassword);
        signIn = (Button) findViewById(R.id.login);
        registration = (Button) findViewById(R.id.registration);
        signIn.setOnClickListener(this);
        registration.setOnClickListener(this);

    }

    public void onClick(View view){
        if(view.getId() == R.id.login){
            signing(ETEmailAddress.getText().toString(), ETPassword.getText().toString());
        }
        else if(view.getId() == R.id.registration){
            registration(ETEmailAddress.getText().toString(), ETPassword.getText().toString());
        }

    }

    public void signing(String email, String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @org.jetbrains.annotations.NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(), "Авторизация успешна", Toast.LENGTH_LONG).show();

                   Intent intent = new Intent(getApplicationContext(), SecondActivity.class);
                   startActivity(intent);

                }else{
                    Toast.makeText(getApplicationContext(), "Авторизация провалена", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void registration(String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Регистрация успешна", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(LoginActivity.this, "Регистрация провалена", Toast.LENGTH_LONG).show();

            }
        });
    }

}
