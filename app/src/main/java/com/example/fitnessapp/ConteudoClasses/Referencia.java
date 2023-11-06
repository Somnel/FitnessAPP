package com.example.fitnessapp.ConteudoClasses;

public class Referencia {
    private String titulo;
    private String refABNT;
    private String link;


    public Referencia(String titulo, String refABNT, String link) {
        this.titulo = titulo;
        this.refABNT = refABNT;
        this.link = link;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getLink() {
        return link;
    }

    public String getRefABNT() {
        return refABNT;
    }
}
