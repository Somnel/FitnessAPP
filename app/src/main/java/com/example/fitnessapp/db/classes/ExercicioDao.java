package com.example.fitnessapp.db.classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.fitnessapp.db.sql;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ExercicioDao {

    private SQLiteDatabase database;
    private sql dbHelper;
    private String tabela = "tbExercicio";
    private DateTimeFormatter sqlFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ExercicioDao(Context context) {
        dbHelper = new sql(context);
        database = dbHelper.getWritableDatabase();
    }


    @SuppressLint("Range")
    public Exercicio buscar(int idExercicio) {
        Exercicio exerc = new Exercicio();

        try(Cursor verify = database.query(tabela, null, "idExercicio = ?", new String[]{String.valueOf(idExercicio)}, null, null, null)) {
            if(verify.moveToFirst()) {
                exerc.setID(idExercicio);
                exerc.setNome(verify.getString(verify.getColumnIndex("exerc_nome")));
                exerc.setTipo(verify.getString(verify.getColumnIndex("exerc_tipo")).charAt(0));
                exerc.setDescricao(verify.getString(verify.getColumnIndex("exerc_desc")));
                exerc.setIllustracao(verify.getString(verify.getColumnIndex("exerc_illu")));
                exerc.setIntensidade(verify.getString(verify.getColumnIndex("exerc_intensidade")).charAt(0));
                exerc.setLimite_semanal(verify.getInt(verify.getColumnIndex("exerc_limite")));

                exerc.setDuracao(LocalTime.parse(verify.getString(verify.getColumnIndex("exerc_duracao")), sqlFormat));
            }
        }



        return exerc;
    }


    @SuppressLint("Range")
    public List<Exercicio> buscar(char tipo, char intensidade, LocalTime duracao_limite) {

        ArrayList<Exercicio> listExercs = new ArrayList<>();
        String selecao = "exerc_intensidade = ? AND exerc_duracao < ?";
        String[] selecaoArgs = {String.valueOf(intensidade), duracao_limite.format(sqlFormat)};

        try(Cursor verify = database.query(tabela, null, selecao, selecaoArgs, null, null, null)) {
            database.beginTransaction();

            while (verify.moveToNext()) {
                Exercicio exerc = new Exercicio();
                    exerc.setID(verify.getInt(verify.getColumnIndex("idExercicio")));
                    exerc.setNome(verify.getString(verify.getColumnIndex("exerc_nome")));
                    exerc.setTipo(verify.getString(verify.getColumnIndex("exerc_tipo")).charAt(0));
                    exerc.setDescricao(verify.getString(verify.getColumnIndex("exerc_desc")));
                    exerc.setIllustracao(verify.getString(verify.getColumnIndex("exerc_illu")));
                    exerc.setIntensidade(verify.getString(verify.getColumnIndex("exerc_intensidade")).charAt(0));
                    exerc.setLimite_semanal(verify.getInt(verify.getColumnIndex("exerc_limite")));
                    exerc.setDuracao(LocalTime.parse(verify.getString(verify.getColumnIndex("exerc_duracao")), sqlFormat));

                listExercs.add(exerc);
            }

            database.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }

        return listExercs;
    }


    @SuppressLint("Range")
    private List<Integer> buscar_gpmuscular(@NonNull String[] nomesMusculos) {
        ArrayList<Integer> ids = new ArrayList<>();
        try(Cursor verify = database.query("tbGrupoMuscular", new String[]{"idGrupoMusc"}, "Musculo = ?", nomesMusculos, null, null, null)){
           while(verify.moveToNext()) {
                ids.add(verify.getInt(verify.getColumnIndex("idGrupoMusc")));
           }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    public void adicionar(@NonNull Exercicio exerc) {
        List<Integer> ids_gpMuscular = buscar_gpmuscular(exerc.getMusculos().toArray(new String[0]));

        if(ids_gpMuscular == null || ids_gpMuscular.isEmpty()) {
            Log.d("Exceção", "Grupo muscular invalido");
            return;
        }

        try{
            database.beginTransaction();

            ContentValues values = new ContentValues();
                values.put("exerc_nome", exerc.getNome());
                values.put("exerc_illu", exerc.getIllustracao());
                values.put("exerc_desc", exerc.getDescricao());
                values.put("exerc_tipo", String.valueOf(exerc.getTipo()));
                values.put("exerc_duracao", exerc.getDuracao().format(sqlFormat));
                values.put("exerc_intensidade", String.valueOf(exerc.getIntensidade()));
                values.put("exerc_limite", exerc.getLimite_semanal());

            long rs = database.insert(tabela, null, values);
            for(int id : ids_gpMuscular) {
                ContentValues values2 = new ContentValues();
                    values2.put("idExercicio", (int) rs);
                    values2.put("idGrupoMusc", id);

                database.insert("tbGrupoMuscular", null, values2);
            }

            database.endTransaction();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }


    public void alterar(Exercicio exerc) {
        try{
            ContentValues values = new ContentValues();
                values.put("exerc_nome", exerc.getNome());
                values.put("exerc_illu", exerc.getIllustracao());
                values.put("exerc_desc", exerc.getDescricao());
                values.put("exerc_tipo", String.valueOf(exerc.getTipo()));
                values.put("exerc_duracao", exerc.getDuracao().format(sqlFormat));
                values.put("exerc_intensidade", String.valueOf(exerc.getIntensidade()));
                values.put("exerc_limite", exerc.getLimite_semanal());

            database.update(tabela, values,"idExercicio = ?", new String[]{String.valueOf(exerc.getID())});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean remover(int idExercicio) {
        try{
            int rs = database.delete(tabela, "idExercicio = ?", new String[]{String.valueOf(idExercicio)});
            return rs > 0;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
