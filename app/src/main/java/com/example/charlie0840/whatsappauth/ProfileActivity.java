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

        nameText.setText(currUser.getDisplayName());

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.change_button:
                //TODO: change user name
                break;
            case R.id.logout_button:
                //TODO: logout
                break;
        }
    }
}
