package com.e.soilab.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.e.soilab.R;
import com.e.soilab.model.AvaliableModel;

import java.util.List;

public class AvaliableAdapter extends RecyclerView.Adapter<AvaliableAdapter.MyViewHolder>
{
    private Context context;
    private List<AvaliableModel> mList;
    private String avaliable;

    public AvaliableAdapter(Context context, List<AvaliableModel> mList)
    {
        this.context = context;
        this.mList = mList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_avialiable,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position)
    {
        holder.txt_Start.setText(mList.get(position).getTxt_start());
       // holder.txt_Bar.setText(mList.get(position).getTxt_Bar());


        if(mList.get(position).getTxt_Bar().equalsIgnoreCase("AVAILABLE"))
        {
            holder.txt_Bar.setBackgroundColor(ContextCompat.getColor(context,R.color.green));
        }else {
            holder.txt_Bar.setBackgroundColor(ContextCompat.getColor(context,R.color.grey));
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView txt_Start,txt_End,txt_Bar;

        public MyViewHolder(@NonNull View itemView)
        {
            super(itemView);

            txt_Start = itemView.findViewById(R.id.txt_Start);
            txt_Bar = itemView.findViewById(R.id.txt_Bar);
        }
    }
}
