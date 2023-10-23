package com.example.fitnessapp.db;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.fitnessapp.db.classes.Exercicio;
import com.example.fitnessapp.db.classes.ExercicioDao;
import com.example.fitnessapp.db.classes.HistoricoExercicio;
import com.example.fitnessapp.db.classes.Usuario;
import com.example.fitnessapp.db.classes.UsuarioConvert;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ListaExercicios {
    private static Map<Integer, HistoricoExercicio> histExercicios;


    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void create(Context context) throws Exception {
        final LocalDate hoje = LocalDate.now();
        ExercicioDao dao = new ExercicioDao(context);
        UsuarioConvert convert = new UsuarioConvert();
        Usuario usuario = UsuarioSession.getInstance(context).getUsuario();


        // > BUSCAR LISTA NO BANCO
        int condicao = Integer.parseInt(String.valueOf(convert.convertCondicao(usuario.getCondicao())));
        char intensidade = condicao < 3 ? '0' : (condicao < 5 ? '1' : '2');
        List<HistoricoExercicio> histExercs = dao.buscarHistorico(usuario.getID());
        List<Exercicio> lista = dao.buscar(intensidade, usuario.getDisponibilidade());

        // > SEPARAR POR FOCO

        // > SEPARAR POR OBJETIVO

        // > REMOVER INVÁLIDOS
        for (HistoricoExercicio hist : histExercs) {
            for (Exercicio temp : hist.getExercicios()) {
                lista.removeIf(exercicio -> exercicio.equals(temp) && hist.getDate().plusDays(temp.getLimite_semanal()).isAfter(hoje));
            }
        }

        // > CRIAR LISTA
        int disponibilidade = usuario.getDisponibilidade().getMinute();
        List<Exercicio> listaExerciciosUsuario = new ArrayList<>();
        Iterator<Exercicio> exercs = lista.iterator();

        while (exercs.hasNext()) {
            Exercicio exercicio = exercs.next();
            int duracaoExercicio = exercicio.getDuracao().getMinute();

            if (duracaoExercicio <= disponibilidade) {
                listaExerciciosUsuario.add(exercicio);
                disponibilidade -= duracaoExercicio;
                exercs.remove();
            }
        }

        histExercicios.put(usuario.getID(), new HistoricoExercicio(hoje, new ArrayList<>(listaExerciciosUsuario)));
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public static List<Exercicio> getListaExercicios(Context context) throws Exception {
        final LocalDate hoje = LocalDate.now();
        final int idUsuario = UsuarioSession.getInstance(context).getUsuario().getID();

        if(!histExercicios.get(idUsuario).getDate().equals(hoje))
            create(context);

        return Objects.requireNonNull(histExercicios.get(idUsuario)).getExercicios();
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void insertHistorico(Context context) {
        Usuario usuario = UsuarioSession.getInstance(context).getUsuario();
        final int idUsuario = usuario.getID();

        if(histExercicios.get(idUsuario) != null) {
            ExercicioDao dao = new ExercicioDao(context);
            dao.gravarHistorico(idUsuario, histExercicios.get(idUsuario));
        }
    }
}
