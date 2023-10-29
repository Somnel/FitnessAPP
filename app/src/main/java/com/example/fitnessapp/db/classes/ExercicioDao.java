package com.example.fitnessapp.db.classes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.fitnessapp.db.sql;

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
                exerc.setCronometrado(!verify.getString(verify.getColumnIndex("exerc_iscronometrado")).equals("0"));

                try(Cursor verifyGPMuscular = database.query(gpmuscTabela, null, "idExercicio = ?", idExerc, null, null, null)){
                    if(verifyGPMuscular.moveToFirst()) {
                        ArrayList<GrupoMuscular> gpMusc = new ArrayList<>();

                        while (verifyGPMuscular.moveToNext()) {
                            int idGpMusc = verifyGPMuscular.getInt( verifyGPMuscular.getColumnIndex("idGrupoMusc"));
                            String[] idGPMuscular = new String[]{
                                    String.valueOf(idGpMusc)
                            };


                            try(Cursor verifyGPMuscular_Nome = database.query("tbGrupoMuscular", null, "idGrupoMusc = ?", idGPMuscular, null, null, null)) {
                                GrupoMuscular gp = new GrupoMuscular();

                                gp.setMusculo( verifyGPMuscular_Nome.getString( verifyGPMuscular_Nome.getColumnIndex("Musculo") ) );
                                gp.setID(idGpMusc);

                                gp.setGrupo( verifyGPMuscular_Nome.getString( verifyGPMuscular_Nome.getColumnIndex("Grupo_muscular") ).charAt(0) );

                                gpMusc.add(gp);
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
    public List<Exercicio> buscar(char intensidade, LocalTime duracao_limite, String foco, ArrayList<String> objetivo)  {

        ArrayList<Exercicio> retorno = new ArrayList<>();
        UsuarioConvert convert = new UsuarioConvert();

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT tbExercicio.* FROM tbExercicio ");
        queryBuilder.append("INNER JOIN tbExercGPMuscular ON tbExercicio.idExercicio = tbExercGPMuscular.idExercicio ");
        queryBuilder.append("INNER JOIN tbGrupoMuscular ON tbExercGPMuscular.idGrupoMusc = tbGrupoMuscular.idGrupoMusc ");
        queryBuilder.append("WHERE tbExercicio.exerc_intensidade = ?");



        ArrayList<String> sqlQueryArgs = new ArrayList<>();
        sqlQueryArgs.add(String.valueOf(intensidade));

        // Duracao - SE NULL
        if(duracao_limite != null) {
            String duracaoSQL = "AND tbExercicio.exerc_duracao < ? ";
            sqlQueryArgs.add(duracao_limite.format(sqlFormat));
            queryBuilder.append(duracaoSQL);
        }

        // Foco - SE NULL
        if(!TextUtils.isEmpty(foco)) {
            String focoConvertido = String.valueOf( convert.getFoco( foco ) );
            String focoSQL = (!TextUtils.isEmpty(focoConvertido) ?  " AND tbGrupoMuscular.Grupo_muscular = ?" : "");

            sqlQueryArgs.add(focoConvertido);
            queryBuilder.append(focoSQL);
        }

        String sqlQuery = queryBuilder.toString();

        try (Cursor query = database.rawQuery(sqlQuery, sqlQueryArgs.toArray(new String[0]))) {
            while (query.moveToNext()) {
                retorno.add( buscar( query.getInt( query.getColumnIndex( "idExercicio" ) ) ) );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retorno;
    }




    @SuppressLint("Range")
    public void adicionar(Context context, @NonNull Exercicio exerc, Bitmap imgExerc) {

        try{
            database.beginTransaction();

            ContentValues values = new ContentValues();
                values.put("exerc_nome", exerc.getNome());
                values.put("exerc_desc", exerc.getDescricao());
                values.put("exerc_tipo", String.valueOf(exerc.getTipo()));
                values.put("exerc_duracao", exerc.getDuracao().format(sqlFormat));
                values.put("exerc_intensidade", String.valueOf(exerc.getIntensidade()));
                values.put("exerc_limite", exerc.getLimite_semanal());


            if(!exerc.getIllustracao().isEmpty()) {
                String imgRef = exerc.getNome().toLowerCase().trim();
                if(ExecicioIlustracaoDao.salvarIlustracao(context, imgExerc, imgRef)) {
                    values.put("exerc_illu", imgRef);
                }
            }

            long rs = database.insert(tabela, null, values);

            // > Grupo Muscular
            if(!exerc.getMusculos().isEmpty()) {
                final String tabelaGPMuscExecs = "tbExercGPMuscular";
                final String tabelaGPMusc = "tbGrupoMuscular";

                for(GrupoMuscular musculo : exerc.getMusculos()) {
                    long idGpMusc;

                    try(Cursor verifyMusculo = database.query(tabelaGPMusc, new String[]{"idGrupoMusc"}, "Musculo = ?", new String[]{musculo.getMusculo()}, null, null, null)) {
                        if(verifyMusculo.moveToFirst()) {
                            idGpMusc = verifyMusculo.getInt( verifyMusculo.getColumnIndex("idGrupoMusc") );
                        } else {
                            ContentValues musculoValues = new ContentValues();
                            musculoValues.put("Musculo", musculo.getMusculo());
                            musculoValues.put("Grupo_muscular", String.valueOf(musculo.getGrupo()));
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



    public boolean remover(int idExercicio) {
        database.beginTransaction();
        try{
            int res = database.delete(tabela, "idExercicio = ?", new String[]{String.valueOf(idExercicio)});
            if(res > 0) {
                database.setTransactionSuccessful();
                return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        return false;
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
    public List<HistoricoExercicio> buscarHistorico(int idUsuario, int reduzSemanas) {
        final LocalDate lcdate = LocalDate.now();
        ArrayList<HistoricoExercicio> historicoExercicios = new ArrayList<>();

        final String[] args = {
                String.valueOf(idUsuario),
                String.valueOf(LocalDate.parse(String.valueOf(lcdate.minusWeeks(reduzSemanas)), DateTimeFormatter.ofPattern("yyyy-MM-dd")))
        };

        database.beginTransaction();
        try(Cursor verify = database.query(tabela, null, "idUsuario = ? AND histData < ?", args, null, null, null)) {


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
            database.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.endTransaction();
        }
        
        return historicoExercicios;
    }

    @SuppressLint("Range")
    public Character buscarMusculoFoco(String foco) {
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
