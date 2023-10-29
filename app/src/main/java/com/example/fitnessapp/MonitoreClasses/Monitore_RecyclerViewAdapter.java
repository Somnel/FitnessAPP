package com.example.fitnessapp.MonitoreClasses;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;
import com.example.fitnessapp.db.classes.Exercicio;
import com.example.fitnessapp.db.classes.HistoricoExercicio;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

public class Monitore_RecyclerViewAdapter extends RecyclerView.Adapter<Monitore_RecyclerViewAdapter.MyViewHolder>{

    Context context;
    Map<Exercicio, LocalDate> historico;

    public Monitore_RecyclerViewAdapter(Context context, List<HistoricoExercicio> historico) {
        this.context = context;

        for(HistoricoExercicio hist : historico) {
            LocalDate tempDate = hist.getDate();
            for(Exercicio exerc : hist.getExercicios()) {
                this.historico.put(exerc, tempDate);
            }
        }
    }

    @NonNull
    @Override
    public Monitore_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.historico_recyclerview_row, parent, false);
        return new Monitore_RecyclerViewAdapter.MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull Monitore_RecyclerViewAdapter.MyViewHolder holder, int position) {
        Exercicio exercicio = (Exercicio) historico.keySet().toArray()[position];
        LocalDate exercDate = historico.get(exercicio);
        DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        holder.rowText_exercicio.setText(exercicio.getNome());
        holder.rowText_date.setText(exercDate.format(formatDate));
    }

    @Override
    public int getItemCount() {
        return historico.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView rowText_exercicio;
        TextView rowText_date;

        public MyViewHolder(@NonNull View itensInterface) {
            super(itensInterface);

            rowText_exercicio = itensInterface.findViewById(R.id.Hist_rowText);
            rowText_date = itensInterface.findViewById(R.id.Hist_rowDate);
        }
    }
}
