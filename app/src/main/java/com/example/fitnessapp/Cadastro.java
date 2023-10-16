package com.example.fitnessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.fitnessapp.MainActivity;
import com.example.fitnessapp.R;
import com.example.fitnessapp.db.classes.UsuarioSession;
import com.example.fitnessapp.fragmentos.*;

public class Cadastro extends AppCompatActivity {

    private ViewPager2 stepView;
    private StepperPagerAdapter pagerAdapter;
    private Button next;
    private Button prev;

    private int step;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        init();

        if(UsuarioSession.getInstance(this).isLogged()) {
            Intent intent = new Intent(this, Principal.class);
            startActivity(intent);
            finish();
        }
        stepView.setAdapter(pagerAdapter);
        updateView();

        // Botão de Anterior
        prev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(prev.isEnabled()) {
                    step--;
                    updateView();
                }
            }
        });

        // Botão de Próximo
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(next.isEnabled()) {
                    step++;
                    updateView();
                }
            }
        });
    }

    private void init() {
        stepView = findViewById(R.id.cadastro_viewpager);
        pagerAdapter = new StepperPagerAdapter(getSupportFragmentManager(), getLifecycle());
        next = findViewById(R.id.btn_proxima);
        prev = findViewById(R.id.btn_anterior);
        step = 1;
    }


    // Stepper
    public void updateView() {
        // Prev
        if(step <= 1) prev.setEnabled(false);
        else prev.setEnabled(true);

        // Next
        if(step >= 3) next.setEnabled(false);
        else next.setEnabled(true);

        stepView.setCurrentItem(step - 1);
    }

    private class StepperPagerAdapter extends FragmentStateAdapter {
        StepperPagerAdapter(FragmentManager fragmentManager, Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return new cadastroFragments.Pagina1Fragment();
                case 1:
                    return new cadastroFragments.Pagina2Fragment();
                case 2:
                    return new cadastroFragments.Pagina3Fragment();
                default:
                    return null;
            }
        }

        @Override
        public int getItemCount() { return 3; }
    }
}
