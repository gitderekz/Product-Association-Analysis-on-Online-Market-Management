package com.example.maisha_supermarket_management;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewFavourites extends RecyclerView.ViewHolder {

    TextView transaction_date;
    TextView transaction_items;
    TextView transaction_amount;
    View v;


    public ViewFavourites(@NonNull View itemView) {
        super(itemView);
        transaction_date = itemView.findViewById(R.id.transaction_time);
        transaction_items = itemView.findViewById(R.id.transaction_items);
        transaction_amount = itemView.findViewById(R.id.transaction_amount);
        v = itemView;
    }
}