package com.example.charlie0840.whatsappauth;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.concurrent.TimeUnit;

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
                logInWithCredential(phoneAuthCredential);
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

        loginBtn.setEnabled(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                String code = pwdInput.getText().toString();
                if(TextUtils.isEmpty(code)) {
                    pwdInput.setError("password needed");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.send_pwd_button:
                loginBtn.setEnabled(true);
                if(!validatePhoneNumber()) {
                    return;
                }
                resendBtn.setVisibility(View.VISIBLE);
                phoneNum = phoneInput.getText().toString();
                startPhoneNumberVerification(phoneNum);
                break;
            case R.id.resend_button:
                if(phoneNum.length() == 0)
                    return;
                resendVerificationCode(phoneNum, mResendToken);
                break;
        }
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks);

    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        logInWithCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, 60, TimeUnit.SECONDS, this, mCallbacks, token);
    }

    /*
     * log into account using the sent credential, change the new user's displayname to "new user"
     *
     */
    private void logInWithCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {

                            String name = getString(R.string.default_name);
                            FirebaseUser user = task.getResult().getUser();
                            if(user.getDisplayName() == null) {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                if (user != null)
                                    user.updateProfile(profileUpdates);
                                System.out.println("name is " + user.getDisplayName() + "!");
                            }
                            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                            startActivity(intent);
                        }
                        else {
                            if(task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Toast.makeText(getApplicationContext(), "Wrong credential", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
    }

    /*
     * check if the phone number is valid(format: +1 xxx xxx xxxx)
     */
    private boolean validatePhoneNumber() {
        String phoneNumber = phoneInput.getText().toString();
        if(TextUtils.isEmpty(phoneNumber)) {
            phoneInput.setText("Invalid format");
            return false;
        }
        return true;
    }
}
