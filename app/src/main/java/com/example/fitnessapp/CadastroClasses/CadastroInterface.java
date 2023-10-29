package com.example.fitnessapp.CadastroClasses;

import java.util.ArrayList;

public interface CadastroInterface {
    void removeItemExercRealizado(int position);
    void updateSpinner();
    String getSelectedItem();
    ArrayList<String> getSelectedItens();
}
