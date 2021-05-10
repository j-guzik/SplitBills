package com.example.billssplitter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterBills  extends RecyclerView.Adapter<AdapterBills.HolderBills>{

    private Context context;
    private ArrayList<ModelBills> billsList;

    public AdapterBills(Context context, ArrayList<ModelBills> billsList) {
        this.context = context;
        this.billsList = billsList;
    }

    @NonNull
    @Override
    public HolderBills onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_bills_lists, parent, false);
        return new HolderBills(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBills holder, int position) {
        //get data
        ModelBills model = billsList.get(position);
        String billId = model.getBillId();
        String billTitle = model.getBillTitle();
        String billCost = model.getBillCost();

        //set data
        holder.billTitleTv.setText(billTitle);
        holder.billCostTv.setText(billCost);

        //handle group click
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open group
            }
        });
    }


    @Override
    public int getItemCount() {
        return billsList.size();
    }


    class HolderBills extends RecyclerView.ViewHolder{
        private ImageView billIconIv;
        private TextView billTitleTv, billCostTv;

        public HolderBills(@NonNull View itemView) {
            super(itemView);
            billIconIv = itemView.findViewById(R.id.billIconIv);
            billTitleTv = itemView.findViewById(R.id.billTitleTv);
            billCostTv = itemView.findViewById(R.id.billCostTv);
        }
    }
}
