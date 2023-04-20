package com.example.deerdiary;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView title;
    TextView dateTime;

    public MyViewHolder(@NonNull View itemView, RecycleViewInterface recycleViewInterface) {
        super(itemView);
        title=itemView.findViewById(R.id.titleView);
        dateTime=itemView.findViewById(R.id.datetimeView);

        itemView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                if(recycleViewInterface != null){
                    int pos = getAdapterPosition();

                    if(pos != RecyclerView.NO_POSITION){
                        recycleViewInterface.onItemClick(pos);
                    }
                }
            }
        });
    }
}
