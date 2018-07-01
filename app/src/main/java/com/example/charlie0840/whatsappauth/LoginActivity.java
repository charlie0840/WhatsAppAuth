package com.example.charlie0840.whatsappauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private String phoneNum, mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private EditText phoneInput, pwdInput;

    private Button loginBtn, sendPwdBtn, resendBtn;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        phoneInput = findViewById(R.id.phone_input_field);
        pwdInput = findViewById(R.id.password_field);

        phoneNum = phoneInput.getText().toString();

        loginBtn = findViewById(R.id.login_button);
        sendPwdBtn = findViewById(R.id.send_pwd_button);
        resendBtn = findViewById(R.id.resend_button);

        loginBtn.setOnClickListener(this);
        sendPwdBtn.setOnClickListener(this);
        resendBtn.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                //do nothing, no need for auto fill
                //Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_LONG).show();
                //logInWithCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(), "fail", Toast.LENGTH_LONG).show();
                if(e == null) {
                    return;
                }
                if(e instanceof FirebaseAuthInvalidCredentialsException) {
                    Toast.makeText(getApplicationContext(), "Invalid phone number", Toast.LENGTH_LONG).show();
                }
                else if(e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(getApplicationContext(), "Too many requests, please wait", Toast.LENGTH_LONG).show();
                }
                else
                    System.out.println("error is " + e.getMessage().toString());
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {

                Toast.makeText(getApplicationContext(), "sent", Toast.LENGTH_LONG).show();
                mVerificationId = verificationId;
                mResendToken = token;
                System.out.println("mVerification " + mVerificationId);
            }
        };

    }

    @Override
    public void onClick(View v) {

    }
}
