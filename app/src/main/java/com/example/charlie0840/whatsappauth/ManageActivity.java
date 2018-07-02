package com.example.charlie0840.whatsappauth;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener{

    final List<Cow> cowList = new ArrayList<>();
    final List<String> cowStore = new ArrayList<>();
    final List<User> userStore = new ArrayList<>();
    final List<Production> productList = new ArrayList<>();
    private List<String> cowNameList = new ArrayList<>();
    private List<String> productNameList = new ArrayList<>();

    private FirebaseDatabase database;

    private DatabaseReference myRef;

    private int pos;
    private String userID, currCowID;
    private HashSet<String> cowSet;

    private User currUser;

    private CowCustomList adapter, productAdapter;

    private Button addCowBtn, addProductBtn;
    private ListView cowListView, productListView;
    private EditText cowNameText, productMornText, productEveText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        cowListView = findViewById(R.id.cow_list);
        productListView = findViewById(R.id.production_list);

        addCowBtn = findViewById(R.id.add_cow_button);
        addProductBtn = findViewById(R.id.add_production_button);

        addProductBtn.setEnabled(false);

        cowNameText = findViewById(R.id.cow_name_field);
        productEveText = findViewById(R.id.evening_product_field);
        productMornText = findViewById(R.id.morning_product_field);

        addCowBtn.setOnClickListener(this);
        addProductBtn.setOnClickListener(this);

        cowSet = new HashSet<>();

        productAdapter = new CowCustomList(ManageActivity.this, productNameList);
        productListView.setAdapter(productAdapter);



        adapter = new CowCustomList(ManageActivity.this, cowNameList);
        cowListView.setAdapter(adapter);
        cowListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int firstLoc = cowListView.getFirstVisiblePosition();
                int lastLoc = cowListView.getLastVisiblePosition();
                if(pos >= firstLoc && pos <= lastLoc) {
                    cowListView.getChildAt(pos - firstLoc).setBackgroundColor(Color.parseColor("#57DF89"));
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {}
        });

        cowListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int firstLoc = cowListView.getFirstVisiblePosition();
                int lastLoc = cowListView.getLastVisiblePosition();
                int numOfVisibleView = lastLoc - firstLoc;
                for(int i = 0; i <= numOfVisibleView; i++) {
                    cowListView.getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff"));
                }
                cowListView.getChildAt(position - firstLoc).setBackgroundColor(Color.parseColor("#57DF89"));
                pos = position;
                getTheCow(cowNameList.get(position));
            }
        });

        getUser();

    }

    private void fillProduction(final String cowName) {
        final String cowID = cowStore.get(0);
        myRef.child("Productions").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                productList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child: children) {
                    Production product = child.getValue(Production.class);
                    if(product.cowID.equals(cowID)) {
                        productList.add(product);
                    }
                }
                productNameList.clear();
                System.out.println("set to 0 for productnameList");
                for(Production product: productList) {
                    String temp = Integer.toString(product.amount) + " " + product.date;
                    productNameList.add(temp);
                }
                System.out.println("productnameList with size " + productNameList.size());
                productAdapter.notifyDataSetChanged();
                addProductBtn.setEnabled(true);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void getUser() {
        userStore.clear();
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child: children) {
                    User user = child.getValue(User.class);
                    if(user.userID.equals(userID)) {
                        userStore.add(user);
                        break;
                    }
                }
                currUser = userStore.get(0);
                getCows();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getTheCow(final String cowName) {
        cowStore.clear();
        myRef.child("Cows").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child: children) {
                    Cow cow = child.getValue(Cow.class);
                    if(cow.name.equals(cowName) && cow.ownerID.equals(userID)) {
                        cowStore.add(child.getKey());
                        break;
                    }
                }
                currCowID = cowStore.get(0);
                fillProduction(cowNameList.get(pos));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getCows() {
        cowSet.clear();
        myRef.child("Cows").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cowList.clear();
                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
                for(DataSnapshot child: children) {
                    Cow cow = child.getValue(Cow.class);
                    if(cow.ownerID.equals(userID)) {
                        cowList.add(cow);
                    }
                }


                cowNameList.clear();

                for(Cow cow: cowList) {
                    String temp = cow.name;
                    cowNameList.add(temp);
                    cowSet.add(temp);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    private void insertCow() {
        String name = cowNameText.getText().toString();
        if(cowSet.contains(name)) {
            Toast.makeText(this, "name already existed", Toast.LENGTH_LONG).show();
            return;
        }

        Cow cow = new Cow(userID, name, new ArrayList<String>());

        myRef.child("Cows").push().setValue(cow);

        cowNameList.add(name);
        adapter.notifyDataSetChanged();
        getTheCow(name);
        cowListView.clearChoices();
        adapter.notifyDataSetChanged();
    }

    private void insertProduction() {
        int val1 = 0, val2 = 0;
        String s1 = productMornText.getText().toString();
        String s2 = productEveText.getText().toString();
        if(s1.equals("") || s2.equals("")) {
            Toast.makeText(this, "Please enter the amount, 0 if none", Toast.LENGTH_LONG).show();
            return;
        }
        if(!s1.equals(""))
            val1 = Integer.parseInt(productMornText.getText().toString());
        if(!s2.equals(""))
            val2 = Integer.parseInt(productEveText.getText().toString());

        int total = val1 + val2;

        Date date = new Date();

        Production production = new Production(currCowID, total, date);

        myRef.child("Productions").push().setValue(production);

        productNameList.add(total + date.toString());
        productAdapter.notifyDataSetChanged();

//        final List<String> list = new ArrayList<>();
//
//        myRef.child("Cows").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Iterable<DataSnapshot> children = dataSnapshot.getChildren();
//                for(DataSnapshot child: children) {
//                    Cow cow = child.getValue(Cow.class);
//                    if(cow.name.equals(cowName) && cow.ownerID.equals(userID)) {
//                        list.addAll(cow.productions);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//        DatabaseReference ref = myRef.child("Cows").child(currCowID);
//        list.add()
//        Map<String, Object> newListMap = new HashMap<>();

    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add_cow_button:
                insertCow();
                break;
            case R.id.add_production_button:
                insertProduction();
                break;
        }
    }
}
