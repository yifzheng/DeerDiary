package com.example.deerdiary;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView dateTime;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        title=itemView.findViewById(R.id.titleView);
        dateTime=itemView.findViewById(R.id.datetimeView);
    }
}
