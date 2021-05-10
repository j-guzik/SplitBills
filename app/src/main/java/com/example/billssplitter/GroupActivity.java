package com.example.billssplitter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GroupActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

    private ArrayList<ModelBills> billsLists;
    private AdapterBills adapterBills;

    private String groupId, billId;
    private CollapsingToolbarLayout toolbar;
    private ImageView groupIconIv;
    private TextView groupTitleTv;
    private ImageButton action_add_participant;
    private Button billAddImgBtn;
    private RecyclerView billsRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        toolbar = findViewById(R.id.toolbar);
        groupIconIv = findViewById(R.id.groupIconIv);
        groupTitleTv = findViewById(R.id.groupTitleTv);
        action_add_participant = findViewById(R.id.action_add_participant);
        billAddImgBtn = findViewById(R.id.billAddImgBtn);
        billsRv = findViewById(R.id.billsRv);

        //get id of the group
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId");

        Intent intent1 = getIntent();
        billId = intent.getStringExtra("billId");

        firebaseAuth = FirebaseAuth.getInstance();
        loadGroupInfo();
        loadBillsList();

        action_add_participant.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, GroupParticipantAddActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });


        billAddImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupActivity.this, BillCreateActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
            }
        });

    }

    private void loadBillsList() {
        billsLists = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Groups");
        reference.child(groupId).child("Bills")
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                billsLists.size();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                        ModelBills model = ds.getValue(ModelBills.class);
                        billsLists.add(model);
                    }
                adapterBills = new AdapterBills(GroupActivity.this, billsLists);
                billsRv.setAdapter(adapterBills);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadGroupInfo() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.orderByChild("groupId").equalTo(groupId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){
                            String groupTitle = ""+ds.child("groupTitle").getValue();
                            String groupIcon = ""+ds.child("groupIcon").getValue();
                            String timestamp = ""+ds.child("timestamp").getValue();
                            String createdBy = ""+ds.child("createdBy").getValue();

                            groupTitleTv.setText(groupTitle);

                            try{
                                Picasso.get().load(groupIcon).placeholder(R.drawable.group_default).into(groupIconIv);
                            } catch (Exception e){
                                groupIconIv.setImageResource(R.drawable.group_default);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}