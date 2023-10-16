package com.example.fitnessapp.db.classes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Usuario {

    private int ID;
    private String nome;
    private String email;
    private LocalDate datanasc;
    private String sexo;
    private int altura; // Em cm
    private double peso;
    private String condicao;
    private LocalTime[] disponibilidade = new LocalTime[2];
    private String foco;

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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public int getAltura() {
        return altura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public LocalTime[] getDisponibilidade() {
        return disponibilidade;
    }

    public void setDisponibilidade(LocalTime[] disponibilidade) {
        this.disponibilidade = disponibilidade;
    }

    public double getPeso() { return peso; }

    public void setPeso(double peso) { this.peso = peso; }

    public String getFoco() { return foco; }

    public void setFoco(String foco) { this.foco = foco; }
}
