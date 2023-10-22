package com.example.fitnessapp.Cadastro_Classes;

import java.util.ArrayList;

public interface CadastroInterface {
    void removeItemExercRealizado(int position);
    void updateSpinner();
    String getSelectedItem();
    ArrayList<String> getSelectedItens();
}
