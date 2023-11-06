package com.example.fitnessapp.ConteudoClasses;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;

import java.io.File;
import java.util.ArrayList;

public class Conteudo_RecyclerViewAdapter extends RecyclerView.Adapter<Conteudo_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<Referencia> referencia;
    final String direct;


    public Conteudo_RecyclerViewAdapter(Context context, ArrayList<Referencia> referencia) {
        this.context = context;
        this.referencia = referencia;
        this.direct = File.separator + "Files" + File.separator;
    }

    @NonNull
    @Override
    public Conteudo_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.conteudo_recyclerview_row, parent, false);
        return new MyViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull Conteudo_RecyclerViewAdapter.MyViewHolder holder, int position) {
        Referencia ref = referencia.get(position);
        Uri path;

        holder.titulo.setText(ref.getTitulo());

        if (TextUtils.isEmpty(ref.getRefABNT())) {
            File file = new File(context.getFilesDir() + direct + ref.getLink());
            path =  FileProvider.getUriForFile(context, "com.example.fitnessapp.provider", file);
            holder.referencia.setText(R.string.placeholderconteudo);
        } else {
            holder.referencia.setText(ref.getRefABNT());
            path = Uri.parse(ref.getLink());
        }

        holder.link = path;
    }

    @Override
    public int getItemCount() {
        return referencia.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, referencia;
        Uri link;

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);

            titulo = itemView.findViewById(R.id.conteudo_rowTitulo);
            referencia = itemView.findViewById(R.id.conteudo_rowReferencia);

            referencia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(link != null) {
                        Intent intent;
                        if(!referencia.getText().toString().equals(context.getString(R.string.placeholderconteudo))) {
                            intent = new Intent(Intent.ACTION_VIEW, link);
                        } else {
                            intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(link, "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }

                        context.startActivity(intent);
                    }
                }
            });
        }
    }
}