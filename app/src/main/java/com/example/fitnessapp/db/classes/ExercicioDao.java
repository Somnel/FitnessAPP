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

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ExercicioDao {

    private final SQLiteDatabase database;
    private final sql dbHelper;
    private final String tabela = "tbExercicio";
    private final DateTimeFormatter sqlFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ExercicioDao(Context context) {
        dbHelper = new sql(context);
        database = dbHelper.getWritableDatabase();
    }


    @SuppressLint("Range")
    public Exercicio buscar(int idExercicio) {
        Exercicio exerc = new Exercicio();

        final String gpmuscTabela = "tbExercGPMuscular";
        final String classTabela = "tbExercClassificacao";
        final String[] idExerc = new String[]{String.valueOf(idExercicio)};

        try(Cursor verify = database.query(tabela, null, "idExercicio = ?", idExerc, null, null, null)) {
            if(verify.moveToFirst()) {
                database.beginTransaction();

                exerc.setID(idExercicio);
                exerc.setNome(verify.getString(verify.getColumnIndex("exerc_nome")));
                exerc.setIllustracao(verify.getString(verify.getColumnIndex("exerc_illu")));
                exerc.setDescricao(verify.getString(verify.getColumnIndex("exerc_desc")));
                exerc.setTipo(verify.getString(verify.getColumnIndex("exerc_tipo")).charAt(0));
                exerc.setDuracao(LocalTime.parse(verify.getString(verify.getColumnIndex("exerc_duracao")), sqlFormat));
                exerc.setIntensidade(verify.getString(verify.getColumnIndex("exerc_intensidade")).charAt(0));
                exerc.setLimite_semanal(verify.getInt(verify.getColumnIndex("exerc_limite")));

                try(Cursor verifyGPMuscular = database.query(gpmuscTabela, null, "idExercicio = ?", idExerc, null, null, null)){
                    if(verifyGPMuscular.moveToFirst()) {
                        ArrayList<String> gpMusc = new ArrayList<>();

                        while (verifyGPMuscular.moveToNext()) {

                            String[] idGPMuscular = new String[]{verifyGPMuscular.getString( verifyGPMuscular.getColumnIndex("idGrupoMusc") )};
                            try(Cursor verifyGPMuscular_Nome = database.query("tbGrupoMuscular", null, "idGrupoMusc = ?", idGPMuscular, null, null, null)) {
                                gpMusc.add( verifyGPMuscular_Nome.getString( verifyGPMuscular_Nome.getColumnIndex("Musculo") ) );
                            }

                        }

                        exerc.setMusculos(gpMusc);
                    }
                }

                try(Cursor verifyClassificao = database.query(classTabela, null, "exercclassIDExerc = ?", idExerc, null, null, null)) {
                    if(verifyClassificao.moveToFirst()) {
                        ArrayList<String> classificacao = new ArrayList<>();

                        while(verifyClassificao.moveToNext()) {
                            classificacao.add( verifyClassificao.getString( verifyClassificao.getColumnIndex( "exercClassificao" ) ) );
                        }

                        exerc.setClassificao(classificacao);
                    }
                }

                database.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }

        return exerc;
    }

    @SuppressLint("Range")
    public List<Exercicio> buscar(char intensidade, LocalTime duracao_limite) {

        ArrayList<Exercicio> listExercs = new ArrayList<>();
        String selecao = "exerc_intensidade = ? AND exerc_duracao < ?";
        String[] selecaoArgs = {String.valueOf(intensidade), duracao_limite.format(sqlFormat)};

        try(Cursor verify = database.query(tabela, null, selecao, selecaoArgs, null, null, null)) {

            while (verify.moveToNext()) {
                listExercs.add( buscar( verify.getInt( verify.getColumnIndex("idExercicio") ) ) );
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    @SuppressLint("Range")
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

            // > Grupo Muscular
            if(!exerc.getMusculos().isEmpty()) {
                final String tabelaGPMuscExecs = "tbExercGPMuscular";
                final String tabelaGPMusc = "tbGrupoMuscular";

                for(String musculo : exerc.getMusculos()) {
                    long idGpMusc;

                    try(Cursor verifyMusculo = database.query(tabelaGPMusc, new String[]{"idGrupoMusc"}, "Musculo = ?", new String[]{musculo}, null, null, null)) {
                        if(verifyMusculo.moveToFirst()) {
                            idGpMusc = verifyMusculo.getInt( verifyMusculo.getColumnIndex("idGrupoMusc") );
                        } else {
                            ContentValues musculoValues = new ContentValues();
                            musculoValues.put("Grupo_muscular", musculo);
                            idGpMusc = database.insert(tabelaGPMusc, null, musculoValues);
                        }
                    }

                    ContentValues musculoValues = new ContentValues();
                    musculoValues.put("idExercicio", rs);
                    musculoValues.put("idGrupoMusc", idGpMusc);

                    database.insert(tabelaGPMuscExecs, null, musculoValues);
                }
            }

            // > Classificação
            if(!exerc.getClassificao().isEmpty()) {
                for(String classificacao : exerc.getClassificao()) {
                    ContentValues classificaoValues = new ContentValues();
                        classificaoValues.put("exercclassIDExerc", rs);
                        classificaoValues.put("exercClassificao", classificacao);

                    database.insert("tbExercClassificacao", null, classificaoValues);
                }
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


    public void gravarHistorico(int idUsuario, HistoricoExercicio historicoExercicio) {
        final String tabelaPrincipal = "tbUsuarioHistorico";
        final String tabelaLista = "tbUsuarioListExercicios";
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try{
            database.beginTransaction();

            ContentValues valuesPrincipal = new ContentValues();
            valuesPrincipal.put("idUsuario", idUsuario);
            valuesPrincipal.put("histData", historicoExercicio.getDate().format(formatter));

            final long ID = database.insert(tabelaPrincipal, null, valuesPrincipal);

            for(Exercicio exercicio : historicoExercicio.getExercicios()) {
                ContentValues valuesLista = new ContentValues();
                    valuesLista.put("idHistExercs", ID);
                    valuesLista.put("idExercicio", exercicio.getID());
                database.insert(tabelaLista, null, valuesLista);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
    }

    @SuppressLint("Range")
    public List<HistoricoExercicio> buscarHistorico(int idUsuario) {
        final LocalDate lcdate = LocalDate.now();
        ArrayList<HistoricoExercicio> historicoExercicios = new ArrayList<>();

        final String[] args = {
                String.valueOf(idUsuario),
                String.valueOf(LocalDate.parse(String.valueOf(lcdate.minusWeeks(1)), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        };

        try(Cursor verify = database.query(tabela, null, "idUsuario = ? AND histData < ?", args, null, null, null)) {
            database.beginTransaction();

            if(verify.moveToFirst()) {
                // > IDS do Histórico
                ArrayList<Integer> histID = new ArrayList<>();

                while(verify.moveToNext())
                    histID.add(verify.getInt(verify.getColumnIndex("histIDList")));

                // > Lista de Exercios POR IDS
                for(Integer histperID : histID) {
                    // > Cria histórico de exercicios
                    try(Cursor histQuery = database.query("tbUsuarioListExercicios", new String[]{"idExercicio"}, "idHistExercs = ?", new String[]{String.valueOf(histperID)}, null, null, null)){
                        ArrayList<Exercicio> exercicios = new ArrayList<>();
                        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        LocalDate date = LocalDate.parse( (verify.getString(verify.getColumnIndex("histData"))), dateFormat);

                        while (histQuery.moveToNext()) {
                            int idExerc = histQuery.getInt(histQuery.getColumnIndex("idExercicio"));
                            exercicios.add(buscar(idExerc));
                        }

                        historicoExercicios.add(new HistoricoExercicio(histperID, date, exercicios));
                    }
                    // ---
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
            return historicoExercicios;
        }
    }

    @SuppressLint("Range")
    public char buscarMusculoFoco(String foco) {
        final String tabela = "tbGrupoMuscular";
        final String[] column = {"Grupo_muscular"};
        String[] musc = {foco};
        try (Cursor verify = database.query(tabela, column, "Musculo = ?", musc, null, null, null)) {
            if(verify.moveToFirst()) {
                return verify.getString( verify.getColumnIndex( "Grupo_muscular" ) ).charAt(0);
            } else {
                return ' ';
            }
        }
    }
}
