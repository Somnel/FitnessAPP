package com.example.fitnessapp.db.classes;


import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Exercicio {
    private int ID;
    private String nome;
    private String illustracao;
    private String descricao;
    private char tipo;
    private boolean cronometrado;
    private LocalTime duracao;
    private char intensidade;
    private int limite_semanal;
    private ArrayList<GrupoMuscular> musculos;
    private ArrayList<String> classificao;


    public Exercicio() {}

    public boolean isCronometrado() {
        return cronometrado;
    }

    public void setCronometrado(boolean cronometrado) {
        this.cronometrado = cronometrado;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getIllustracao() {
        return illustracao;
    }

    public void setIllustracao(String illustracao) {
        this.illustracao = illustracao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public char getTipo() {
        return tipo;
    }

    public void setTipo(char tipo) {
        this.tipo = tipo;
    }

    public LocalTime getDuracao() {
        return duracao;
    }

    public void setDuracao(LocalTime duracao) {
        this.duracao = duracao;
    }

    public char getIntensidade() {
        return intensidade;
    }

    public void setIntensidade(char intensidade) {
        this.intensidade = intensidade;
    }

    public int getLimite_semanal() {
        return limite_semanal;
    }

    public void setLimite_semanal(int limite_semanal) {
        this.limite_semanal = limite_semanal;
    }

    public List<GrupoMuscular> getMusculos() { return musculos; }

    public void setMusculos(ArrayList<GrupoMuscular> musculos) { this.musculos = musculos; }

    public ArrayList<String> getClassificao() {
        return classificao;
    }

    public void setClassificao(ArrayList<String> classificao) {
        this.classificao = classificao;
    }
}
