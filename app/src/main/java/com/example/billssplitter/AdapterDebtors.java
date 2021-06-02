package com.example.billssplitter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterDebtors extends RecyclerView.Adapter<AdapterDebtors.HolderDebtors> {

    private Context context;
    private ArrayList<ModelUsers> usersList;
    private String groupId, billId, cost, costPerson; //creator, participant

    public AdapterDebtors(Context context, ArrayList<ModelUsers> usersList, String groupId, String cost, String billId, String costPerson) {
        this.context = context;
        this.usersList = usersList;
        this.groupId = groupId;
        this.cost = cost;
        this.billId = billId;
        this.costPerson = costPerson;
    }

    @NonNull
    @Override
    public HolderDebtors onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.row_debtors, parent, false);

        return new HolderDebtors(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderDebtors holder, int position) {
        //get data
        ModelUsers modelUsers = usersList.get(position);
        String name = modelUsers.getName();
        String email = modelUsers.getEmail();
        String image = modelUsers.getImage();
        String uid = modelUsers.getUid();
        //set data
        holder.nameTv.setText(name);
        holder.emailTv.setText(email);
        holder.personDebt.setText(costPerson+"$");
        try{
            Picasso.get().load(image).placeholder(R.drawable.default_pic).into(holder.avatarIv);
        }
        catch (Exception e){
            holder.avatarIv.setImageResource(R.drawable.default_pic);
        }

        checkIsDebtor(modelUsers, holder);


        //handle click
        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
                ref.child(groupId).child("Bills").child(billId).child("Debtors").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    //user exists
                                    String paidOff = "" + dataSnapshot.child("paidOff").getValue();
                                    if (paidOff.equals("true")) {

                                    }
                                    else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("Has the debt been paid off?")
                                                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        //remove user
                                                        removeDebt(modelUsers);
                                                    }
                                                })
                                                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                }
                            }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });


    }

    private void checkIsDebtor(ModelUsers modelUsers, HolderDebtors holder) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Bills").child(billId).child("Debtors").child(modelUsers.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String paidOff = "" + dataSnapshot.child("paidOff").getValue();
                        if (paidOff.equals("true")) {
                            holder.paidOff.setImageResource(R.drawable.ic_price_check);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


    private void removeDebt(ModelUsers modelUsers) {
        //add that user in Group>groupId>Participants
        HashMap<String, Object> result = new HashMap<>();
        result.put("paidOff", "true");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Bills").child(billId).child("Debtors").child(modelUsers.getUid()).updateChildren(result)
                .addOnSuccessListener(new OnSuccessListener<Void >() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //added successfully
                        Toast.makeText(context, "Debt paid off...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //failed adding user in group
                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class HolderDebtors extends RecyclerView.ViewHolder{
        private ImageView avatarIv, paidOff;
        private TextView nameTv, emailTv, personDebt;

        public HolderDebtors(@NonNull View itemView){
            super(itemView);

            avatarIv = itemView.findViewById(R.id.avatarIv);
            nameTv = itemView.findViewById(R.id.nameTv);
            emailTv = itemView.findViewById(R.id.emailTv);
            personDebt = itemView.findViewById(R.id.personDebt);
            paidOff = itemView.findViewById(R.id.paidOff);
        }

    }
}
