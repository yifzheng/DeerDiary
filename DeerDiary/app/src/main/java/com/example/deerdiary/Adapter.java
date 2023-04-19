package com.example.deerdiary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<MyViewHolder> {

    Context context;
    ArrayList<DiaryEntry> entries;

    public Adapter(Context context, ArrayList<DiaryEntry> entries) {
        this.context = context;
        this.entries = entries;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.diary_view,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(entries.get(position).getTitle());
        holder.dateTime.setText(entries.get(position).getDateTime());
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }
}
