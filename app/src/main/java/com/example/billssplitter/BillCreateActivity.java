package com.example.billssplitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BillCreateActivity extends AppCompatActivity {

    //actionbar
    private ActionBar actionBar;

    //firebase auth
    private FirebaseAuth firebaseAuth;

    //UI views
    private EditText billTitleEt, costTitleEt;
    private Button addBillBtn;

    private String groupId;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_create);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(null);

        //init UI views
        billTitleEt = findViewById(R.id.billTitleEt);
        costTitleEt = findViewById(R.id.costTitleEt);
        addBillBtn = findViewById(R.id.addBillBtn);

        //get id of the group
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        firebaseAuth = FirebaseAuth.getInstance();
        checkUser();

        //handle click event
        addBillBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCreatingBill();
            }
        });

    }

    private void startCreatingBill() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Dodawanie rachunku.....");

        //input title
        String billTitle = billTitleEt.getText().toString().trim();
        String billCost = costTitleEt.getText().toString().trim();
        //validation
        if(TextUtils.isEmpty(billTitle)){
            Toast.makeText(this, "Please enter bill name...", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(billCost)){
            Toast.makeText(this, "Cost is required...", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        String b_timestamp = ""+System.currentTimeMillis();
        createBill(""+b_timestamp,
                    ""+billTitle,
                ""+billCost);
    }

    private void createBill(String b_timestamp, String billTitle, String billCost) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("billId", ""+b_timestamp);
        hashMap.put("billTitle", ""+billTitle);
        hashMap.put("billCost", ""+billCost);

        //create bill
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Bills").child(b_timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added successfully
                        Toast.makeText(BillCreateActivity.this, "Added successfully...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding user in group
                        Toast.makeText(BillCreateActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


    }

    private void checkUser() {
    }
}