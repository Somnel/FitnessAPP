package com.example.fitnessapp.db.classes;

import java.time.LocalDate;
import java.util.ArrayList;

public class HistoricoExercicio  {
    private int ID;
    private final LocalDate date;
    private ArrayList<Exercicio> exercicios;


    public HistoricoExercicio(LocalDate date, ArrayList<Exercicio> exercicios) {
        this.date = date;
        this.exercicios = exercicios;
    }

    public HistoricoExercicio(int id, LocalDate date, ArrayList<Exercicio> exercicios) {
        this.ID = id;
        this.date = date;
        this.exercicios = exercicios;
    }

    public int getID() {
        return ID;
    }

    public LocalDate getDate() {
        return date;
    }

    public ArrayList<Exercicio> getExercicios() {
        return exercicios;
    }

    public void setExercicios(ArrayList<Exercicio> exercicios) {
        this.exercicios = exercicios;
    }
}
