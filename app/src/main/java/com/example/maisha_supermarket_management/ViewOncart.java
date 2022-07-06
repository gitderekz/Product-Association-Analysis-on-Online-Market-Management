package com.example.maisha_supermarket_management;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewOncart extends RecyclerView.ViewHolder {

    TextView transaction_time;
    TextView transaction_items;
    TextView transaction_itemIds;
    View v;


    public ViewOncart(@NonNull View itemView) {
        super(itemView);
        transaction_time = itemView.findViewById(R.id.transaction_time_cart);
        transaction_items = itemView.findViewById(R.id.transaction_items_cart);
        transaction_itemIds = itemView.findViewById(R.id.transaction_ids_cart);
        v = itemView;
    }
}
