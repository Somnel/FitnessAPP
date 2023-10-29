package com.example.fitnessapp.CadastroClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;

import java.util.ArrayList;

public class Cadastro_RecyclerViewAdapter extends RecyclerView.Adapter<Cadastro_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> listItens;
    private final CadastroInterface itensInterface;

    public Cadastro_RecyclerViewAdapter(Context context, @NonNull ArrayList<String> listItensAdapter, CadastroInterface itensInterface) {
        this.context = context;
        this.listItens = listItensAdapter;
        this.itensInterface = itensInterface;
    }

    @NonNull
    @Override
    public Cadastro_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cadastro_addspinner_row, parent, false);
        return new MyViewHolder(view, itensInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull Cadastro_RecyclerViewAdapter.MyViewHolder holder, int position) {
        holder.rowText.setText(listItens.get(position));
    }

    @Override
    public int getItemCount() {
        return listItens == null ? 0 : listItens.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rowText;
        Button btnDeleteRow;

        public MyViewHolder(@NonNull View itemView, CadastroInterface itensInterface) {
            super(itemView);

            rowText = itemView.findViewById(R.id.rowText);
            btnDeleteRow = itemView.findViewById(R.id.rowBtn);

            btnDeleteRow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(itensInterface != null) {
                        int pos = getAdapterPosition();

                        if(pos != RecyclerView.NO_POSITION) {
                            itensInterface.removeItemExercRealizado(pos);
                        }
                    }
                }
            });
        }
    }
}
