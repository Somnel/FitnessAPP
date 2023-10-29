package com.example.fitnessapp.db.classes;

import androidx.annotation.NonNull;

public class UsuarioConvert {



    // OBJETIVO
    public char convertObjetivo(@NonNull String objetivo) throws Exception {

        switch (objetivo) {
            case "Prevenção de Doenças":
                return '0';
            case "Ganho de Massa Magra":
                return '1';
            case "Perda de gordura":
                return '2';
            case "Fortalecimento":
                return '3';
            default:
                if(objetivo.length() == 1 && Character.isDigit(objetivo.charAt(0))) {
                    for(int i = 0; i < 4; i++)
                        if(objetivo.equals(String.valueOf(i)))
                            return objetivo.charAt(0);
                }
                throw new Exception("Objetivo Inválido: " + objetivo);
        }
    }

    public String convertObjetivo(char objetivo) throws Exception {
        switch (objetivo) {
            case '0':
                return "Prevenção de Doenças";
            case '1':
                return "Ganho de Massa Magra";
            case '2':
                return "Perda de gordura";
            case '3':
                return "Fortalecimento";
            default:
                throw new Exception("Objetivo Inválido: " + objetivo);
        }
    }



    public Character getFoco(String foco) {
        switch (foco) {
            case "Superiores": return 'A';
            case "Inferiores": return 'B';
            default: return null;
        }
    }

    // FOCO
    public Character convertFoco(String foco) throws Exception {
        if(foco == null) return null;
        switch(foco) {
            case "Superiores": return '0';
            case "Inferiores": return '1';
            default:
                if(foco.length() == 1 && Character.isDigit(foco.charAt(0))) {
                    if(foco.equals("0") || foco.equals("1"))
                        return foco.charAt(0);
                }
                throw new Exception("Foco de Usuário Invalido");
        }
    }

    public String convertFoco(char foco) throws Exception {
        switch(foco) {
            case '0':
                return "Superiores";
            case '1':
                return "Inferiores";
            case '2':
                return "Ambos";
            default:
                throw new Exception("Foco de Usuário Inválido: " + foco);
        }
    }




    // Exercicios Realizados
    public char convertExercsRealizado(String exerc) throws Exception {
        switch (exerc) {
            case "Treino de Força":
                return '0';
            case "Resistência":
                return '1';
            case "Equilíbrio":
                return '2';
            case "Flexibilidade":
                return '3';
            case "Corridas":
                return '4';
            case "Pular Corda":
                return '5';
            default:
                if(exerc.length() == 1 && Character.isDigit(exerc.charAt(0))) {
                    for(int i = 0; i < 5; i++) {
                        if(exerc.equals(String.valueOf(i)))
                            return exerc.charAt(0);
                    }
                }
                throw new Exception("Exercio Invalido : " + exerc);
        }
    }

    public String convertExercsRealizado(char exerc) throws Exception {
        switch (exerc) {
            case '0':
                return "Treino de Força";
            case '1':
                return "Resistência";
            case '2':
                return "Equilíbrio";
            case '3':
                return "Flexibilidade";
            case '4':
                return "Corridas";
            case '5':
                return "Pular Corda";
            default:
                throw new Exception("Exercício Inválido: " + exerc);
        }
    }



    // CONDIÇÃO
    public String convertCondicao(@NonNull char condicao) throws Exception {
        switch(condicao) {
            case '0': return "Sedentário";
            case '1': return "Praticante";
            case '2': return "Expert";
            default:
                throw  new Exception("Condição invalida");
        }
    }


    public char convertCondicao(String condicao) throws Exception {
        switch (condicao.trim()) {
            case "Sedentário":
                return '0';
            case "Praticante":
                return '1';
            case "Expert":
                return '2';
            default:
                throw new Exception("Condição Inválida: " + condicao);
        }
    }
}
