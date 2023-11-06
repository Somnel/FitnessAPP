package com.example.fitnessapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.ConteudoClasses.Conteudo_RecyclerViewAdapter;
import com.example.fitnessapp.ConteudoClasses.Referencia;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.util.ArrayList;

public class Conteudo extends AppCompatActivity {


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
        finish();
    }





    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conteudo);
        if(!UsuarioSession.getInstance(this).isLogged()) { UsuarioSession.logOut(this); }

        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void init() {
        ArrayList<Referencia> refs = new ArrayList<>();
        refs = setReferencias();




        Conteudo_RecyclerViewAdapter adapter = new Conteudo_RecyclerViewAdapter(this, refs);
        RecyclerView recyclerView = findViewById(R.id.conteudo_recyclerview);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private ArrayList<Referencia> setReferencias() {
        ArrayList<Referencia> temp = new ArrayList<>();


        // > 1
        temp.add(new Referencia(
                "EFFECTS OF RESISTANCE EXERCISE TRAINING ON COGNITIVE FUNCTION AND PHYSICAL PERFORMANCE IN COGNITIVE FRAILTY: A RANDOMIZED CONTROLLED TRIAL",
                "Yoon, D. H., Lee, J.-Y., & Song, W. (2018). Effects of Resistance Exercise Training on Cognitive Function and Physical Performance in Cognitive Frailty: A Randomized Controlled Trial. The Journal of Nutrition, Health & Aging. doi:10.1007/s12603-018-1090-9",
                "https://sci-hub.se/10.1007/s12603-018-1090-9"));

        // > 2
        temp.add(new Referencia(
                "Predictors of maintaining cognitive function in older adults",
                "Yaffe K, Fiocco AJ, Lindquist K, Vittinghoff E, Simonsick EM, Newman AB, Satterfield S, Rosano C, Rubin SM, Ayonayon HN, Harris TB; Health ABC Study. Predictors of maintaining cognitive function in older adults: the Health ABC study. Neurology. 2009 Jun 9;72(23):2029-35. doi: 10.1212/WNL.0b013e3181a92c36. PMID: 19506226; PMCID: PMC2692177.",
                "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC2692177/"));

        // > 3
        temp.add(new Referencia(
                "Differential Effects of Physical Exercise, Cognitive Training, and Mindfulness Practice on Serum BDNF Levels in Healthy Older Adults: A Randomized Controlled Intervention Study",
                "Ledreux, A., Hkansson, K., Carlsson, R., Kidane, M., Columbo, L., Terjestam, Y., … Mohammed, A. K. H. (2019). Differential Effects of Physical Exercise, Cognitive Training, and Mindfulness Practice on Serum BDNF Levels in Healthy Older Adults: A Randomized Controlled Intervention Study. Journal of Alzheimer’s Disease, 1–17. doi:10.3233/jad-190756",
                "https://www.ncbi.nlm.nih.gov/pmc/articles/PMC2692177/"));

        // > 4
        temp.add(new Referencia(
                "Aerobic fitness and multidomain cognitive function in advanced age",
                "Netz, Y., Dwolatzky, T., Zinker, Y., Argov, E., & Agmon, R. (2010). Aerobic fitness and multidomain cognitive function in advanced age. International Psychogeriatrics, 23(01), 114–124. doi:10.1017/s1041610210000797",
                "https://sci-hub.se/10.1017/s1041610210000797"));

        // > 5
        temp.add(new Referencia(
                "Desenvolvimento de um programa de treino cognitivo para idosos",
                null,
                "cog_231005_164754.pdf"
        ));

        // > 6
        temp.add(new Referencia(
                "Study protocol of the Intense Physical Activity and Cognition study: The effect of high-intensity exercise training on cognitive function in older adults",
                null,
                "Uysal-2023-Aerobic-exercise-and.pdf"
        ));

        // > 7
        temp.add(new Referencia(
                "Effects of simultaneously performed cognitive and physical training in older adults",
                null,
                "Theill-2013-Effects-of-simultane.pdf"
        ));

        // > 8
        temp.add(new Referencia(
                "Dance training is superior to repetitive physical exercise in inducing brain plasticity in the elderly",
                null,
                "Rehfeld-2018-Dance-training-is-su.pdf"
        ));

        // > 9
        temp.add(new Referencia(
                "Combined use of smartphone and smartband technology in the improvement of lifestyles in the adult population over 65 years: study protocol for a randomized clinical trial (EVIDENT-Age study)",
                null,
                "Recio-Rodríguez-2019-Combined-use-of-smar.pdf"
        ));

        // > 11
        temp.add(new Referencia(
                "Efect of simultaneous exercise and cognitive training on executive functions, barorefex sensitivity, and pre‑frontal cortex oxygenation in healthy older adults: a pilot study",
                null,
                "Pellegrini-Laplagne-2023-Effect-of-simultaneo.pdf"
        ));

        // > 12
        temp.add(new Referencia(
                "Effects of simultaneous cognitive and aerobic exercise training on dual-task walking performance in healthy older adults: results from a pilot randomized controlled trial",
                null,
                "Raichlen-2020-Effects-of-simultane.pdf"
        ));

        // > 13
        temp.add(new Referencia(
                "The Effect of Aerobic Exercise on White Matter Hyperintensity Progression May Vary by Sex*",
                null,
                "Dao-2019-The-Effect-of-Aerobi.pdf"
        ));

        // > 14
        temp.add(new Referencia(
                "Benefits of regular aerobic exercise for executive functioning in healthy populations",
                null,
                "Guiney-2013-Benefits-of-regular_230612_171101.pdf"
        ));


        // > 15
        temp.add(new Referencia(
                "Efects of 5 Years Aerobic Exercise on Cognition in Older Adults: The Generation 100 Study: A Randomized Controlled Trial",
                null,
                "Zotcheva-2022-Effects-of-5-Years-A_230607_080037.pdf"
        ));


        return temp;
    }
}