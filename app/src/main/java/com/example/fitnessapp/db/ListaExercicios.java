package com.example.fitnessapp.db;

import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.fitnessapp.db.classes.Exercicio;
import com.example.fitnessapp.db.classes.ExercicioDao;
import com.example.fitnessapp.db.classes.Usuario;
import com.example.fitnessapp.db.classes.UsuarioConvert;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListaExercicios {
    private Map<Integer, Map<LocalDate, List<Integer>>> listExercicios; // idUsuario, idExercicio

    public ListaExercicios() {
        this.listExercicios = new HashMap<>();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void create(Context context, @NonNull Usuario usuario, char tipo) throws Exception {
        ExercicioDao dao = new ExercicioDao(context);
        UsuarioConvert convert = new UsuarioConvert();


        // Intensidade
        int condicao = Integer.parseInt(String.valueOf(convert.convertCondicao(usuario.getCondicao())));
        char intensidade =
                condicao < 3? '0' : ( condicao < 5 ? '1' : '2' ) ;




        /*
            0 : Físico
            1 : Cognitivo
         */

        List<Exercicio> exercs = dao.buscar(tipo, intensidade, usuario.getDisponibilidade());
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
