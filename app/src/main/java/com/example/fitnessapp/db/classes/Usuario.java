package com.example.fitnessapp.db.classes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Usuario {

    private int ID;
    private String nome;
    private String email;
    private LocalDate datanasc;
    private char sexo;
    private String condicao;
    private LocalTime disponibilidade;
    private ArrayList<String> foco = new ArrayList<>();
    private ArrayList<String> exercsRealizados = new ArrayList<>();
    private ArrayList<String> objetivos = new ArrayList<>();

    public Usuario() {}

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getDatanasc() {
        return datanasc;
    }

    public void setDatanasc(LocalDate datanasc) {
        this.datanasc = datanasc;
    }

    public char getSexo() {
        return sexo;
    }

    public void setSexo(char sexo) {
        this.sexo = sexo;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public LocalTime getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(LocalTime disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public ArrayList<String> getFoco() { return foco; }

    public void setFoco(ArrayList<String> foco) { this.foco = foco; }

    public ArrayList<String> getExercsRealizados() { return exercsRealizados; }

    public void setExercsRealizados(ArrayList<String> exercsRealizados) {this.exercsRealizados = exercsRealizados; }

    public ArrayList<String> getObjetivos() {return objetivos;}

    public void setObjetivos(ArrayList<String> objetivos) {this.objetivos = objetivos; }
}
