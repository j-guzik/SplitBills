package com.example.billssplitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DebtorsAddActivity extends AppCompatActivity {

    private RecyclerView participantsRv;

    private ActionBar actionBar;
    private FirebaseAuth firebaseAuth;
    private String groupId, billId, paidBy, myRole;
    private Button addDebtorsBtn;

    private ArrayList<ModelUsers> userList;
    private AdapterDebtorsAdd adapterDebtorsAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debtors_add);

        actionBar = getSupportActionBar();
        actionBar.setTitle("Add Debtors");


        firebaseAuth = FirebaseAuth.getInstance();

        participantsRv = findViewById(R.id.participantsRv);
        addDebtorsBtn = findViewById(R.id.addDebtorsBtn);

        groupId = getIntent().getStringExtra("groupId");
        billId = getIntent().getStringExtra("billId");
        paidBy = getIntent().getStringExtra("paidBy");

        loadParticipants();

        //handle click event
        addDebtorsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DebtorsAddActivity.this, GroupActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });
    }

    private void loadParticipants() {
        userList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String uid = ""+ds.child("uid").getValue();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

                    ref.orderByChild("uid").equalTo(uid).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds: dataSnapshot.getChildren()) {
                                ModelUsers modelUsers = ds.getValue(ModelUsers.class);
                                if (!paidBy.equals(modelUsers.getEmail())) {
                                    userList.add(modelUsers);
                                }
                            }
                            adapterDebtorsAdd = new AdapterDebtorsAdd(DebtorsAddActivity.this, userList, groupId, myRole, billId);
                            participantsRv.setAdapter(adapterDebtorsAdd);
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


}