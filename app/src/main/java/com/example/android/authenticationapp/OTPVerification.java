package com.example.android.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class OTPVerification extends AppCompatActivity {
    EditText otp;
    Button verify;
    String verifyBySystem;
    TextView resend;
    private long mtimeleftinmillis=60000;
    private String getVerificationId;
    private FirebaseAuth mAuth;
    PhoneAuthProvider.ForceResendingToken forceResendingToken;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);
        Utils.statusBarcolor(OTPVerification.this,R.color.light);
        otp = findViewById(R.id.editTextPhone);
        verify = findViewById(R.id.verify);
        resend=findViewById(R.id.resendOtp);
        phone= getIntent().getStringExtra("phoneNumber");
        getVerificationId= getIntent().getStringExtra("id");
        mAuth=FirebaseAuth.getInstance();
        Log.d("vedlog","phone number is: "+ phone);
        sendVerificationCodeToUser(phone);



        otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otp.setElevation(10);
            }
        });








        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = otp.getText().toString().trim();
                if(code.isEmpty() || code.length()<6)
                {
                    otp.setError("wrong OTP");
                    otp.requestFocus();
                    return;
                }
                verifyCode(code);
            }
        });


    }

    private void resendCode(String phone, PhoneAuthProvider.ForceResendingToken token) {

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    private void sendVerificationCodeToUser(String phone)
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+91"+phone)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                "91"+phone,60,TimeUnit.SECONDS, (Activity) TaskExecutors.MAIN_THREAD,mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {



        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            String code= credential.getSmsCode();
            if(code!=null)
            {
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.


            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Toast.makeText(OTPVerification.this, "Verifcation Failed", Toast.LENGTH_SHORT).show();
                // Invalid request
            } else if (e instanceof FirebaseTooManyRequestsException) {
                // The SMS quota for the project has been exceeded
                Toast.makeText(OTPVerification.this, "SMS Quota For Today Has Been Exceeded", Toast.LENGTH_SHORT).show();

            }

            // Show a message and update the UI
        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Toast.makeText(OTPVerification.this, "Verification Code Sent", Toast.LENGTH_SHORT).show();


            mtimeleftinmillis=60000;

            resend.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_timer_24,0,0,0);
            new CountDownTimer(mtimeleftinmillis, 1000)
            {

                @Override
                public void onTick(long l) {

                    mtimeleftinmillis=l;
                    // converting millisecond to minutes and seconds

                    int minutes=(int)(mtimeleftinmillis/1000)/60;
                    int seconds=(int)(mtimeleftinmillis/1000)%60;

                    String timeleftformatted= String.format(Locale.getDefault(),"%02d:%02d", minutes,seconds);

                    resend.setText(timeleftformatted);
                }

                @Override
                public void onFinish() {
                    Drawable leftDrawable;
                    resend.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

                    resend.setText("  Resend OTP");
                    resend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            resendCode(phone, forceResendingToken);
                        }
                    });
                }
            }.start();

            // Save verification ID and resending token so we can use them later

            verifyBySystem=verificationId;
        }
    };


    private void verifyCode(String code)
    {
        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(verifyBySystem, code);
        signInByCredentials(phoneAuthCredential);
    }

    private void signInByCredentials(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(OTPVerification.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(OTPVerification.this, "plugged in to nirvana", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(OTPVerification.this, homeactivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    Animatoo.animateShrink(OTPVerification.this);
                }
                else
                {
                    Toast.makeText(OTPVerification.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
    
    


