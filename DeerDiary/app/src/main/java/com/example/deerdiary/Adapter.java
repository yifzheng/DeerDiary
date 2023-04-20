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
    private final RecycleViewInterface recycleViewInterface;

    public Adapter(Context context, ArrayList<DiaryEntry> entries, RecycleViewInterface recycleViewInterface) {
        this.context = context;
        this.entries = entries;
        this.recycleViewInterface = recycleViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.diary_view,parent,false),recycleViewInterface);
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
