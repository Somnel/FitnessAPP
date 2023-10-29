package com.example.fitnessapp.ConfiguracoesLembrete;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;


public class Lembrete_RecyclerViewAdapter extends RecyclerView.Adapter<Lembrete_RecyclerViewAdapter.MyViewHolder> {

    Context context;

    public Lembrete_RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Lembrete_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.config_lembrete_recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView datetime;
        Button btnRemove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            datetime = itemView.findViewById(R.id.lembrete_row_datetime);
            btnRemove = itemView.findViewById(R.id.lembrete_row_removebtn);

            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}
