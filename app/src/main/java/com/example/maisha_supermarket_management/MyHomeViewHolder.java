package com.example.maisha_supermarket_management;

import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyHomeViewHolder extends RecyclerView.ViewHolder {
    GridLayout grid;
//    ImageView imageView;
    ImageView imageView_one;
//    ImageView imageView_two;
//    ImageView imageView_three;
    TextView textView;
    View v;

    public MyHomeViewHolder(@NonNull View itemView) {
        super(itemView);
        grid = itemView.findViewById(R.id.grid);
//        imageView = itemView.findViewById(R.id.home_single_view);
        imageView_one = itemView.findViewById(R.id.home_single_view_child_one);
//        imageView_two = itemView.findViewById(R.id.home_single_view_child_two);
//        imageView_three = itemView.findViewById(R.id.home_single_view_child_three);
        textView = itemView.findViewById(R.id.homeImageName);
        v=itemView;
    }
}
