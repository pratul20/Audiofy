package com.example.android.authenticationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class register extends AppCompatActivity {
    TextView alreadyhavingaccount;
    EditText inputemail;
    EditText inputpassword;
    EditText confirmpassword;
    Button signup;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    private static final String TAG = register.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Utils.statusBarcolor(register.this,R.color.light);

        alreadyhavingaccount = findViewById(R.id.alreadyhaveanaccount);
        inputemail=findViewById(R.id.email);
        inputpassword=findViewById(R.id.password);
        confirmpassword=findViewById(R.id.confirm_password);
        signup=findViewById(R.id.registerbtn);
        mAuth=FirebaseAuth.getInstance();
        mUser= mAuth.getCurrentUser();
        progressDialog=new ProgressDialog(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        alreadyhavingaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(register.this, MainActivity.class));
                Animatoo.animateZoom(register.this);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isInternetAvailable(register.this))
                    authenticate();
            }
        });
    }
    private void authenticate() {
        String email=inputemail.getText().toString();
        String password=inputpassword.getText().toString();
        String confirmation= confirmpassword.getText().toString();

        if(!email.matches(emailPattern))
        {
            inputemail.setError("please enter correct email");
        }
        else if(password.isEmpty() || password.length()<6)
        {
            inputpassword.setError("password must contain atleast 6 characters");
        }
        else if(!password.equals(confirmation))
        {
            confirmpassword.setError("password doesn't match");
        }
        else
        {
            progressDialog.setMessage("please wait while we plug you into nirvana....");
            progressDialog.setTitle("Sign Up");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        progressDialog.dismiss();
                        sendUserToNextActivity();
                        Toast.makeText(register.this, "Signed Up Successfully",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        progressDialog.dismiss();
                        Toast.makeText(register.this, ""+task.getException(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToNextActivity() {
        Intent intent=new Intent(register.this, homeactivity.class);

        //This will stop to come back to this activity when user successfully signs up
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        Animatoo.animateShrink(register.this);
    }
    public boolean isInternetAvailable(Context context)
    {
        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null)
        {
            Toast.makeText(register.this, "no internet connection",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            if(info.isConnected())
            {
                Toast.makeText(register.this, "connection established",Toast.LENGTH_SHORT).show();
                return true;
            }
            else
            {
                Toast.makeText(register.this, "internet connection",Toast.LENGTH_SHORT).show();
                return true;
            }

        }
    }
}