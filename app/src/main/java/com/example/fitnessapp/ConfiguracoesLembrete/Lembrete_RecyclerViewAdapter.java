package com.example.fitnessapp.ConfiguracoesLembrete;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Lembrete_RecyclerViewAdapter extends RecyclerView.Adapter<Lembrete_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Date> dateLembrete;
    final SimpleDateFormat formatDate;

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public Lembrete_RecyclerViewAdapter(Context context, ArrayList<Date> dateLembrete) {
        formatDate = new SimpleDateFormat("dd/MM/yyyy");
        this.dateLembrete = dateLembrete;
        this.context = context;
    }

    @NonNull
    @Override
    public Lembrete_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.config_lembrete_recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.datetime.setText(formatDate.format(dateLembrete.get(position)));
    }


    @Override
    public int getItemCount() {
        return dateLembrete.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView datetime;
        AppCompatButton btnRemove;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            datetime = itemView.findViewById(R.id.lembrete_row_datetime);
            btnRemove = itemView.findViewById(R.id.lembrete_row_removebtn);
        }
    }
}
