package com.example.charlie0840.whatsappauth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class ManageActivity extends AppCompatActivity {

    final List<String> cowList = new ArrayList<>();

    final List<User> userStore = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        Intent intent = getIntent();

        String userID = intent.getStringExtra("userID");

        getUser();

        getCows();

    }



    private void getCows() {

    }
}
