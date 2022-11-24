package com.example.android.authenticationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;

public class phoneauthentication extends AppCompatActivity {
        EditText phoneNumber;
        Button done;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phoneauthentication);

        Utils.statusBarcolor(phoneauthentication.this,R.color.light);
        phoneNumber=findViewById(R.id.editTextPhone);
        done=findViewById(R.id.signup);

        Log.d("vedlog", "phonenumber "+ phone);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone= phoneNumber.getText().toString();
                if (phone.isEmpty())
                {
                    phoneNumber.setError("enter the phone number first");
                }
                else if(phoneNumber.length()!=10)
                {
                    phoneNumber.setError("invalid phone number");
                }
                else
                {
                    Intent intent=new Intent(phoneauthentication.this, OTPVerification.class);
                    intent.putExtra("phoneNumber", phone );
                    startActivity(intent);
                    Animatoo.animateZoom(phoneauthentication.this);
                }
            }
        });



    }



}