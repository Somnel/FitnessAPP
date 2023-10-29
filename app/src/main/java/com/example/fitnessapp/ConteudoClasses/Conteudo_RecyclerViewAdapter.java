package com.example.fitnessapp.ConteudoClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.db.classes.Exercicio;

import java.util.ArrayList;

public class Conteudo_RecyclerViewAdapter extends RecyclerView.Adapter<Conteudo_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Exercicio> exercicios;


    public Conteudo_RecyclerViewAdapter(Context context, ArrayList<Exercicio> exercicios) {
        this.context = context;
        this.exercicios = exercicios;
    }

    @NonNull
    @Override
    public Conteudo_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.conteudo_recyclerview_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Conteudo_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.tituloExerc.setText(exercicios.get(position).getNome());
        holder.subtituloExerc.setText( exercicios.get(position).getTipo() == '0' ? "Treino Fisico" : "Atividade Mental");
        holder.descExerc.setText( exercicios.get(position).getDescricao() );
    }

    @Override
    public int getItemCount() {
        return exercicios.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tituloExerc, subtituloExerc, descExerc;
        ImageView illustracao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tituloExerc = itemView.findViewById(R.id.conteudo_rowTitulo);
            subtituloExerc = itemView.findViewById(R.id.conteudo_rowSubtitulo);
            descExerc = itemView.findViewById(R.id.conteudo_rowTexto);
            illustracao = itemView.findViewById(R.id.conteudo_rowIlustracao);
        }
    }
}
