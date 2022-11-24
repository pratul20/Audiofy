package com.example.android.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class forgot_password extends AppCompatActivity {

    private Button sendMail;
    private EditText mail;
    private String email;
    private TextView backtologin;
    private FirebaseAuth mAuth;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth=FirebaseAuth.getInstance();
        backtologin=findViewById(R.id.tologinpage);
        mail=findViewById(R.id.email);
        sendMail=findViewById(R.id.getMail);
        Utils.statusBarcolor(forgot_password.this,R.color.light);

        backtologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(forgot_password.this,MainActivity.class));
                Animatoo.animateZoom(forgot_password.this);
            }
        });

        sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validatePassword();
            }
        });
    }

    private void validatePassword() {

        email=mail.getText().toString();
        if(email.isEmpty())
        mail.setError("required field");
        else if(!email.matches(emailPattern))
        {
            mail.setError("please enter correct email");
        }
        else
        {
            sendResetPassMail();
        }
    }

    private void sendResetPassMail() {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(forgot_password.this, "please check your mail inbox", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(forgot_password.this, MainActivity.class));
                    Animatoo.animateZoom(forgot_password.this);
                    finish();
                }
                else
                {
                    Toast.makeText(forgot_password.this, "sorry, unable to send the reset mail", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}