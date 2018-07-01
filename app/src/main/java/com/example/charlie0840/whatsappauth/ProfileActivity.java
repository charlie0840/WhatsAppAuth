package com.example.charlie0840.whatsappauth;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser currUser;

    private FirebaseAuth mAuth;

    private Button logoutBtn, changeBtn;

    private EditText nameText;

    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth != null) {
            currUser = mAuth.getCurrentUser();

            Toast.makeText(this, "Hello " + currUser.getUid(), Toast.LENGTH_LONG).show();
        }

        nameText = findViewById(R.id.user_name_field);

        welcomeText = findViewById(R.id.welcome_field);

        changeBtn = findViewById(R.id.change_button);
        logoutBtn = findViewById(R.id.logout_button);

        changeBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);

        if(currUser == null)
            logOut();
        else
            nameText.setText(currUser.getDisplayName());

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.change_button:
                String currName = nameText.getText().toString();
                changeUserName(currName);
                break;
            case R.id.logout_button:
                logOut();
                break;
        }
    }

    private void changeUserName(String currName) {
        if(currUser == null)
            return;
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setDisplayName(currName).build();
        currUser.updateProfile(profileChangeRequest);
    }

    private void logOut() {
        logoutBtn.setVisibility(View.GONE);
        changeBtn.setVisibility(View.GONE);
        nameText.setVisibility(View.GONE);
        welcomeText.setText(R.string.logged_out);
        if(mAuth == null)
            return;
        mAuth.signOut();
    }
}
