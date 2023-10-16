package com.example.fitnessapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class sql extends SQLiteOpenHelper {

    private final static String db_nome = "Fitoff";
    private final static int db_versao = 1;

    public sql(Context contexto) {
        super(contexto, db_nome, null, db_versao);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela de Grupo Muscular
        db.execSQL("CREATE TABLE IF NOT EXISTS tbGrupoMuscular("
                +"idGrupoMusc INTEGER PRIMARY KEY AUTOINCREMENT"
                +", Grupo_muscular CHAR(1) NOT NULL" // A, B e C
                +", Musculo VARCHAR(20) NOT NULL)");

        // Tabela de Exercicios
        db.execSQL("CREATE TABLE IF NOT EXISTS tbExercicio("
                +"idExercicio INTEGER PRIMARY KEY AUTOINCREMENT"
                +", exerc_nome VARCHAR(40)  NOT NULL"
                +", exerc_illu VARCHAR(100)  NOT NULL" // Link da illustração
                +", exerc_desc TEXT  NOT NULL" // To-do -> Definir na interface limite de caracteres
                +", exerc_tipo CHAR(1)  NOT NULL" // Tipo 0 : fisico 2 : cognitivo
                +", exerc_duracao TIME  NOT NULL"
                +", exerc_intensidade CHAR(1)  NOT NULL" // 0 : Baixa 1 : Média 2 : Alta
                +", exerc_limite INTEGER NOT NULL DEFAULT 0)");

        // Tabela de Grupo Muscular - Exericio
        db.execSQL("CREATE TABLE IF NOT EXISTS tbExercGPMuscular("
                +"idExercicio INTEGER"
                +", idGrupoMusc INTEGER"
                +", FOREIGN KEY(idExercicio) REFERENCES tbExercicio(idExercicio)"
                +", FOREIGN KEY(idGrupoMusc) REFERENCES tbGrupoMuscular(idGrupoMusc)"
                +", PRIMARY KEY(idExercicio,idGrupoMusc))");

        // Tabela de usuário
        db.execSQL("CREATE TABLE IF NOT EXISTS tbUsuario ("
                +"idUsuario INTEGER PRIMARY KEY AUTOINCREMENT"
                +", usu_nome VARCHAR(70) NOT NULL"
                +", usu_senha VARCHAR(20)  NOT NULL"
                +", usu_email VARCHAR(100) NOT NULL"
                +", usu_datanasc DATE  NOT NULL"
                +", usu_sexo CHAR(1) NOT NULL"
                +", usu_peso INTEGER NOT NULL" // em cm
                +", usu_altura INTEGER NOT NULL"
                +", usu_condicao CHAR(1)  NOT NULL"
                +", usu_initDisponivel TIME NOT NULL"
                +", usu_endDisponivel TIME NOT NULL"
                +", usu_foco INTEGER" // NULL TAVA DANDO ERRO
                + ", FOREIGN KEY(usu_foco) REFERENCES tbGrupoMuscular(idGrupoMusc))");

        // Tabela de Histórico de Exercicios
        db.execSQL("CREATE TABLE IF NOT EXISTS tbUsuarioHistorico("
                +"histIDList INTEGER PRIMARY KEY AUTOINCREMENT"
                +", idUsuario INTEGER NOT NULL"
                +", histData DATE NOT NULL"
                +", FOREIGN KEY (idUsuario) REFERENCES tbUsuario(idUsuario))");

        db.execSQL("CREATE TABLE IF NOT EXISTS tbUsuarioListExercicios("
                +"idHistExercs INTEGER NOT NULL"
                +", idExercicio INTEGER NOT NULL"
                +", PRIMARY KEY(idHistExercs, idExercicio)"
                +", FOREIGN KEY (idHistExercs) REFERENCES tbUsuarioHistorico(histIDList)"
                +", FOREIGN KEY (idExercicio) REFERENCES tbExercicio(idExercicio))");

        // Tabela de Administradores
        db.execSQL("CREATE TABLE IF NOT EXISTS tbAdmin("
                +"idUsuario INTEGER PRIMARY KEY"
                +", adminAutoridade CHAR(1) NOT NULL" // Nivel de autoridade
                +", FOREIGN KEY (idUsuario) REFERENCES tbUsuario(idUsuario))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int db_versao_old, int db_versao_nova) {
        db.execSQL("DROP TABLE IF EXISTS MinhaTabela;");
        onCreate(db);
    }

}
