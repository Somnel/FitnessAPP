package com.example.fitnessapp.db.classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.db.sql;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class UsuarioSession {
    private static UsuarioSession instance;
    private final SQLiteDatabase database;
    private final sql dbHelper;
    private static Usuario usuario;
    private final String tabela = "tbUsuario";

    private UsuarioSession(Context context) {
        dbHelper = new sql(context);
        database = dbHelper.getWritableDatabase();
    }

    public static synchronized UsuarioSession getInstance(Context context) {
        if (instance == null) {
            instance = new UsuarioSession(context);
        }
        return instance;
    }

    public boolean isLogged() {
        return usuario != null && usuario.getID() != 0;
    }

    public Usuario getUsuario() {
        return isLogged() ? usuario : null;
    }


    public static void logOut(Activity activity) {
        usuario = new Usuario();
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }

    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean iniciar(String email, String senha) {

        // Query
        String selecao = "usu_email = ? AND usu_senha = ?";
        String[] selecaoArgs = {email, senha};

        try(Cursor verify = database.query(tabela, null, selecao, selecaoArgs, null, null, null)) {
            if(verify.moveToFirst()) {
                database.beginTransaction();
                UsuarioConvert convert = new UsuarioConvert();

                // Definição do usuário
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");

                Usuario usuario = new Usuario();
                    usuario.setID(verify.getInt(verify.getColumnIndex("idUsuario")));
                    usuario.setNome(verify.getString(verify.getColumnIndex("usu_nome")));
                    usuario.setEmail(verify.getString(verify.getColumnIndex("usu_email")));
                    usuario.setSexo(verify.getString(verify.getColumnIndex("usu_sexo")).charAt(0));
                    // Com conversões
                    usuario.setDisponibilidade(LocalTime.parse(verify.getString(verify.getColumnIndex("usu_tempoDisponivel")), timeFormat));
                    usuario.setCondicao( convert.convertCondicao(verify.getString(verify.getColumnIndex("usu_condicao")).charAt(0)) );



                usuario.setDatanasc(LocalDate.parse( verify.getString(verify.getColumnIndex("usu_datanasc")), format));

                // Multivalorados
                String[] idStr = {String.valueOf(usuario.getID())};
                String selecao2 = "idUsuario = ?";

                // > Foco
                try (Cursor verify2 = database.query("tbUsuarioFoco", null, selecao2 , idStr, null, null, null)) {
                    if(verify2.moveToFirst()) {
                        ArrayList<String> focos = new ArrayList<>();

                        while(verify2.moveToNext()) {
                            focos.add( convert.convertFoco(verify2.getString(verify2.getColumnIndex("focoTipo")).charAt(0)));
                        }

                        usuario.setFoco(focos);
                    }
                }

                // > Exercicio Realizado
                try (Cursor verify2 = database.query("tbUsuarioExercsRealizado", null, selecao2 , idStr, null, null, null)) {
                    if(verify2.moveToFirst()) {
                        ArrayList<String> exercs = new ArrayList<>();

                        while (verify2.moveToNext()) {
                            exercs.add( convert.convertExercsRealizado( verify2.getString(verify2.getColumnIndex("exercsrealizadoUsuario")).charAt(0) ) );
                        }

                        usuario.setExercsRealizados(exercs);
                    }
                }

                // > Objetivo
                try (Cursor verify2 = database.query("tbUsuarioObjetivo", null, selecao2 , idStr, null, null, null)) {
                    if(verify2.moveToFirst()) {
                        ArrayList<String> objetivos = new ArrayList<>();

                        while (verify2.moveToNext()) {
                            objetivos.add( convert.convertObjetivo( verify2.getString( verify2.getColumnIndex( "objUsuario" ) ).charAt(0) ) );
                        }

                        usuario.setObjetivos(objetivos);
                    }
                }


                UsuarioSession.usuario = usuario;
                database.setTransactionSuccessful();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            database.endTransaction();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean isEmailFree(@NonNull String email) { // Procura se o email já esta cadastrado
        try(Cursor verify = database.query(tabela, null, "usu_email = ?", new String[]{email}, null, null, null)) {
            return !verify.moveToFirst();
        } catch (Exception e) {
            Log.e("isEmailFree [" + LocalDateTime.now() + "]", Log.getStackTraceString(e));
            return false;
        }
    }

    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean cadastrar(@NonNull Usuario usuario, @NonNull String senha) {
        try {
            database.beginTransaction();
            UsuarioConvert convert = new UsuarioConvert();

            ContentValues values = new ContentValues();
                // Dados de segurança e identificação
                values.put("usu_nome", usuario.getNome());
                values.put("usu_email", usuario.getEmail());
                values.put("usu_senha", senha);
                values.put("usu_datanasc", usuario.getDatanasc().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                values.put("usu_sexo", String.valueOf(usuario.getSexo()));

                // Dados de restrição
                values.put("usu_condicao", String.valueOf( convert.convertCondicao(usuario.getCondicao())));
                values.put("usu_tempoDisponivel", usuario.getDisponibilidade().format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            final long ID = database.insert(tabela, null, values);

            // Dados multivalorados

            // > EXERCICIOS REALIZADOS
            if(!usuario.getExercsRealizados().isEmpty()) {
                for(String exerc : usuario.getExercsRealizados()) {
                    ContentValues values2 = new ContentValues();
                        values2.put("idUsuario", ID);
                        values2.put("exercsrealizadoUsuario", String.valueOf( convert.convertExercsRealizado(exerc) ));

                    database.insert("tbUsuarioExercsRealizado", null, values2);
                }
            }


            // > FOCO
            if(!usuario.getFoco().isEmpty()) {
                if(usuario.getFoco().size() == 2) {
                    ContentValues values2 = new ContentValues();
                    values2.put("idUsuario", ID);
                    values2.put("focoTipo", "2");

                    database.insert("tbUsuarioFoco", null, values2);
                } else {
                    for(String foco : usuario.getFoco()) {
                        ContentValues values2 = new ContentValues();
                        values2.put("idUsuario", ID);
                        values2.put("focoTipo", String.valueOf( convert.convertFoco(foco) ));

                        database.insert("tbUsuarioFoco", null, values2);
                    }
                }
            }


            // > OBJETIVOS
            if(!usuario.getObjetivos().isEmpty()){
                for(String objetivo : usuario.getObjetivos()) {
                    ContentValues values2 = new ContentValues();
                        values2.put("idUsuario", ID);
                        values2.put("objUsuario", String.valueOf( convert.convertObjetivo( objetivo ) ));

                    database.insert("tbUsuarioObjetivo", null, values2);
                }
            }



            database.insert(tabela, null, values);
            UsuarioSession.usuario = usuario;

            database.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            database.endTransaction();
        }
    }


}
