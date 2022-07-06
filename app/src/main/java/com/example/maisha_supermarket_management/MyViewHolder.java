package com.example.maisha_supermarket_management;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView transaction_time;
    TextView transaction_items;
    TextView transaction_amount;
    ImageButton imageButton;
    ImageButton likeButton;
    ImageView imageView;
    TextView textView;
    TextView price;
    TextView count;
    TextView id;
    View v;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        transaction_time = itemView.findViewById(R.id.transaction_time);
        transaction_items = itemView.findViewById(R.id.transaction_items);
        transaction_amount = itemView.findViewById(R.id.transaction_amount);
        imageButton = itemView.findViewById(R.id.cartButton);
        likeButton = itemView.findViewById(R.id.likeButton);
        imageView = itemView.findViewById(R.id.image_single_view);
        textView = itemView.findViewById(R.id.textView_single_view);
        price = itemView.findViewById(R.id.singleView_Price);
        count = itemView.findViewById(R.id.count);
        id = itemView.findViewById(R.id.id);
        v = itemView;
    }
}
