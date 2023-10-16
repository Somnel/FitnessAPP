package com.example.fitnessapp.db.classes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.db.sql;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class UsuarioSession {
    private static UsuarioSession instance;
    private SQLiteDatabase database;
    private sql dbHelper;
    private static Usuario usuario;
    private String tabela = "tbUsuario";

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
        return usuario != null;
    }

    public Usuario getUsuario() {
        if(!isLogged()) return null;
        else return usuario;
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
                // Definição do usuário
                Usuario usuario = new Usuario();
                    usuario.setID(verify.getInt(verify.getColumnIndex("idUsuario")));
                    usuario.setNome(verify.getString(verify.getColumnIndex("usu_nome")));
                    usuario.setEmail(verify.getString(verify.getColumnIndex("usu_email")));
                    usuario.setSexo(verify.getString(verify.getColumnIndex("usu_sexo")));
                    usuario.setAltura(verify.getInt(verify.getColumnIndex("usu_altura")));
                    usuario.setPeso(verify.getDouble(verify.getColumnIndex("usu_peso")));
                    usuario.setCondicao(verify.getString(verify.getColumnIndex("usu_condicao")));

                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                usuario.setDatanasc(LocalDate.parse( verify.getString(verify.getColumnIndex("usu_datanasc")), format));

                // Incializa a instância
                this.usuario = usuario;
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean isEmailFree(String email) { // Procura se o email já esta cadastrado
        try(Cursor verify = database.query(tabela, null, "usu_email = ?", new String[]{email}, null, null, null)) {
            if(verify.moveToFirst()) return false;
            else return true;
        }
    }

    @SuppressLint("Range")
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void cadastrar(@NonNull Usuario usuario, @NonNull String senha) {
        try {
            database.beginTransaction();

            LocalTime[] time = usuario.getDisponibilidade();

            ContentValues values = new ContentValues();
                // Dados de segurança e identificação
                values.put("usu_nome", usuario.getNome());
                values.put("usu_email", usuario.getEmail());
                values.put("usu_senha", senha); // <- Existe somente para cadastro e login
                values.put("usu_datanasc", usuario.getDatanasc().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                values.put("usu_sexo", String.valueOf(usuario.getSexo().charAt(0)));
                // Dados para calculo de exercicios
                values.put("usu_altura", usuario.getAltura());
                values.put("usu_peso", usuario.getPeso());
                // Dados de restrição
                values.put("usu_condicao", String.valueOf( convertCondicao(usuario.getCondicao()).charAt(0) ));
                values.put("usu_initDisponivel", time[0].format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                values.put("usu_endDisponivel", time[1].format(DateTimeFormatter.ofPattern("HH:mm:ss")));

            if(usuario.getFoco() != null && !usuario.getFoco().isEmpty()) {
                String[] values2 = {usuario.getFoco()};
                Cursor verify = database.query("tbGrupoMuscular", new String[]{"idGrupoMusc"}, "Musculo = ?", values2, null, null, null);

                values.put("usu_foco", verify.getInt(verify.getColumnIndex("idGrupoMusc")));
                verify.close();
            }

            database.insert(tabela, null, values);
            this.usuario = usuario;

            database.setTransactionSuccessful();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            database.setTransactionSuccessful();
        }
    }

    private String convertCondicao(@NonNull String condicao) throws Exception {
        switch(condicao.trim()) {
            case "Sedentário": return "0";
            case "Praticante": return "1";
            case "Expert": return "2";
            default:
                if(! (condicao.equals("0") || condicao.equals("1") || condicao.equals("2")) )
                    throw  new Exception("Condição invalida");
                else return condicao;
        }
    }

    private String converCondicao(char condicao) throws Exception{
        switch (condicao) {
            case '0' : return "Sedentário";
            case '1' : return "Praticante";
            case '2' : return "Expert";
            default: throw new Exception("Condição invalida");
        }
    }

    private String convertSexo(@NonNull String sexo) {
        switch(sexo.toUpperCase().trim()) {
            case "F": return "Mulher";
            case "M": return "Homem";
            default: return "Sexo invalido";
        }
    }
}
