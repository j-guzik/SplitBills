package com.example.billssplitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.blogspot.atifsoftwares.circularimageview.CircularImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

public class BillInfoActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    //actionbar
    private ActionBar actionBar;

    private String billId, groupId, cost, paidBy;

    private TextView titleInfoBillTV, costTV, whoPaidTV;
    private CircularImageView receiptImgV;
    private RecyclerView debtorsRV;
    private ImageButton paidOffBtn;

    private ArrayList<ModelUsers> userList;
    private AdapterDebtors adapterDebtors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_info);

        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setTitle(null);

        //init views
        titleInfoBillTV = findViewById(R.id.titleInfoBillTV);
        receiptImgV = findViewById(R.id.receiptImgV);
        costTV = findViewById(R.id.costTV);
        whoPaidTV = findViewById(R.id.whoPaidTV);
        debtorsRV = findViewById(R.id.debtorsRV);

        //get bill id
        Intent intent = getIntent();
        billId = intent.getStringExtra("billId");
        paidBy = intent.getStringExtra("paidBy");
        groupId = intent.getStringExtra("groupId");
        cost = intent.getStringExtra("cost");


        firebaseAuth = FirebaseAuth.getInstance();
        loadBillInfo();
        loadDebtors();


    }

    private void loadBillInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Bills").orderByChild("billId").equalTo(billId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            String titleInfo = ""+ds.child("billTitle").getValue();
                            String cost = ""+ds.child("billCost").getValue();
                            String paidBy = ""+ds.child("paidBy").getValue();
                            String billPhoto = ""+ds.child("billPhoto").getValue();

                            titleInfoBillTV.setText(titleInfo);
                            costTV.setText(cost+"$");
                            whoPaidTV.setText(paidBy);

                            try{
                                Picasso.get().load(billPhoto).placeholder(R.drawable.addbill).into(receiptImgV);
                            } catch (Exception e){
                                receiptImgV.setImageResource(R.drawable.addbill);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadDebtors() {
        userList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Bills").child(billId).child("Debtors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String uid = ""+ds.child("uid").getValue();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

                    ref.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()){
                                ModelUsers modelUsers = ds.getValue(ModelUsers.class);
                                userList.add(modelUsers);
                            }
                           Float size = new Float(userList.size()+1);
                            Float costBigDec = new Float(cost);
                            Float costPerPerson = costBigDec/size;
                            Float roundedPerPerson = (float) (Math.round(costPerPerson * 100.0) / 100.0);

                            adapterDebtors = new AdapterDebtors(BillInfoActivity.this, userList, groupId, cost, billId, roundedPerPerson.toString());
                            debtorsRV.setAdapter(adapterDebtors);
//               participantsTv.setText("Participants ("+userList.size()+")");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}