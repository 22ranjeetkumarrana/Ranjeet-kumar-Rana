package com.example.ranjeetkumarrana.newotpfind;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity
{
    private EditText editText1,editText2;
    private Button button1,button2;
    private String codeSent;
    private FirebaseAuth mAuth;
    private RelativeLayout relativeLayout1,relativeLayout2;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialized
        mAuth = FirebaseAuth.getInstance();


        relativeLayout1 = (RelativeLayout)findViewById(R.id.Relative);
        relativeLayout2 = (RelativeLayout)findViewById(R.id.Relative2);
        editText1 = (EditText)findViewById(R.id.Phonenumber);
        editText2 = (EditText)findViewById(R.id.otpText);
        button1 = (Button)findViewById(R.id.OTPButton1);
        button2 = (Button)findViewById(R.id.OTPButton2);
        textView = (TextView)findViewById(R.id.Tv);

        //set Relative layout invisible
        relativeLayout2.setVisibility(relativeLayout1.INVISIBLE);

        //first Button
        button1.setOnClickListener(new View.OnClickListener()
        {
                @Override
                public void onClick(View v)
                {
                    sendVarificationCode();
                    relativeLayout1.setVisibility(relativeLayout1.INVISIBLE);
                    relativeLayout2.setVisibility(relativeLayout1.VISIBLE);
                    textView.setText("Verify");
                }
        });

        //Second Button
        //For SignIn Mathod

        button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                VerifyOTP();
            }
        });
    }

    private void VerifyOTP()
    {
        String code = editText2.getText().toString();
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSent, code);
    }

    //Sign In User Mathod

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if (task.isSuccessful())
                        {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(MainActivity.this,"Successfully registered",Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this,"Failed ",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    //varification mathod

    private void sendVarificationCode()
    {
        String phone = editText1.getText().toString();
        if(phone.isEmpty())
        {
            editText1.setError("Phone number is required");
            editText1.requestFocus();
            return;
        }

        if(phone.length()<10)
        {
            editText1.setError("Valid phone number is required");
            editText1.requestFocus();
            return;
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
        {

        }

        @Override
        public void onVerificationFailed(FirebaseException e)
        {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken)
        {
            super.onCodeSent(s, forceResendingToken);
            codeSent = s;
        }
    };
}
