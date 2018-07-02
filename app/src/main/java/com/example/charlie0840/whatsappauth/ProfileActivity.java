package com.example.charlie0840.whatsappauth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseUser currUser;

    private FirebaseAuth mAuth;

    private Button logoutBtn, changeBtn, cowBtn;

    private EditText nameText;

    private TextView welcomeText;

    private FirebaseDatabase database;

    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();

        myRef = database.getReference();

        if(mAuth != null) {
            currUser = mAuth.getCurrentUser();

            Toast.makeText(this, "Hello " + currUser.getUid(), Toast.LENGTH_LONG).show();
        }

        nameText = findViewById(R.id.user_name_field);

        welcomeText = findViewById(R.id.welcome_field);

        cowBtn = findViewById(R.id.cow_manage_button);
        changeBtn = findViewById(R.id.change_button);
        logoutBtn = findViewById(R.id.logout_button);

        cowBtn.setOnClickListener(this);
        changeBtn.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);

        if(currUser == null)
            logOut();
        else {
            if(currUser.getDisplayName() == null)
                nameText.setText(R.string.default_name);
            else
                nameText.setText(currUser.getDisplayName());
            insertUser();
        }



    }

    public void insertUser() {
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                boolean hasFound = false;
                for(DataSnapshot child: children) {
                    User user = child.getValue(User.class);

                    if(user.userID.equals(currUser.getUid())) {
                        hasFound = true;
                        break;
                    }
                }
                if(!hasFound) {
                    User user = new User(currUser.getUid(), currUser.getPhoneNumber(), new ArrayList<String>());
                    myRef.child("Users").push().setValue(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
            case R.id.cow_manage_button:
                Intent intent = new Intent(this, ManageActivity.class);
                intent.putExtra("userID", currUser.getUid());
                startActivity(intent);
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
