package com.example.fitnessapp.db;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.fitnessapp.db.classes.Exercicio;
import com.example.fitnessapp.db.classes.ExercicioDao;
import com.example.fitnessapp.db.classes.Usuario;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaExercicios {
    private Map<Integer, Map<LocalDate, List<Integer>>> listExercicios; // idUsuario, idExercicio

    public ListaExercicios() {
        this.listExercicios = new HashMap<>();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void create(Context context, Usuario usuario) {
        ExercicioDao dao = new ExercicioDao(context);


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Integer> getListaExercicios(int idUsuario) {
        Map<LocalDate, List<Integer>> exercs = listExercicios.get(idUsuario);
        LocalDate hoje = LocalDate.now();

        if(exercs.isEmpty() || exercs.equals(null) || !exercs.containsKey(hoje)) {
            return null;
        } else {
            return listExercicios.get(idUsuario).get(hoje);
        }
    }
}
