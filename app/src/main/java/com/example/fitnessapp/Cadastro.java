package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.Cadastro_Classes.CadastroInterface;
import com.example.fitnessapp.Cadastro_Classes.Cadastro_RecyclerViewAdapter;
import com.example.fitnessapp.db.classes.Usuario;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Cadastro extends AppCompatActivity implements CadastroInterface {

    private TextView cadastroDisponibilidadeText;
    private TextView cadastroFreqExerciciosText;


    private EditText cadastroNome;
    private EditText cadastroDatanasc;
    private EditText cadastroEmail;
    private EditText cadastroSenha;
    private RadioGroup cadastroSexo;
    private SeekBar cadastroFreqExercicios;
    private SeekBar cadastroDisponibilidade;

    // Spinners
    // Spinners.Exercicios_Realizados
    private Spinner cadastroExercicioRealizado;
    private Button cadastroExercicioRealizadoBtn;
    private ArrayAdapter<String> cadastroExercicioRealizadoArray;
    private RecyclerView cadastroExercicioRealizadoView;
    private Cadastro_RecyclerViewAdapter cadastroExercicioRealizadoViewAdapter;

    // Spinners.Foco_Treino
    private Spinner cadastroFocoTreino;
    private ArrayAdapter<String> cadastroFocoTreinoArray;
    private Button cadastroFocoTreinoBtn;


    // Spinners.Objetivos
    private Spinner cadastroObjetivo;
    private ArrayAdapter<String> cadastroObjetivoArray;
    private Button cadastroObjetivoBtn;

    // Spinners.Lista
    private ArrayList<String> cadastroExercicioRealizadoItens = new ArrayList<>();
    private ArrayList<String> cadastroFocoTreinoItens = new ArrayList<>();
    private ArrayList<String> cadastroObjetivoItens = new ArrayList<>();

    //

    private Button btnCadastrar;

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

        cadastroExercicioRealizadoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemSelecionado = cadastroExercicioRealizado.getSelectedItem().toString();

                if(!TextUtils.isEmpty(itemSelecionado)) {
                    cadastroExercicioRealizadoItens.add(itemSelecionado);
                    cadastroExercicioRealizadoArray.remove(itemSelecionado);
                    cadastroExercicioRealizadoArray.notifyDataSetChanged();
                }
            }
        });

        cadastroFocoTreinoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemSelecionado = cadastroFocoTreino.getSelectedItem().toString();

                if(!TextUtils.isEmpty(itemSelecionado)) {
                    cadastroFocoTreinoItens.add(itemSelecionado);
                    cadastroFocoTreinoArray.remove(itemSelecionado);
                    cadastroFocoTreinoArray.notifyDataSetChanged(); // Atualiza o Spinner
                }
            }
        });


        cadastroObjetivoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemSelecionado = cadastroObjetivo.getSelectedItem().toString();

                if(!TextUtils.isEmpty(itemSelecionado)) {
                    cadastroObjetivoItens.add(itemSelecionado);
                    cadastroObjetivoArray.remove(itemSelecionado);
                    cadastroObjetivoArray.notifyDataSetChanged(); // Atualiza o Spinner
                }
            }
        });








        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                cadastrar();
            }
        });

        cadastroFreqExercicios.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress >= 4) cadastroFreqExerciciosText.setText(R.string.condicaoAlta);
                else if(progress >= 2) cadastroFreqExerciciosText.setText(R.string.condicaoModerado);
                else cadastroFreqExerciciosText.setText(R.string.condicaoLeve);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        cadastroDisponibilidade.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                cadastroDisponibilidadeText.setText(progress + ":" + "00");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    @SuppressLint("ResourceType")
    private void init() {
        cadastroNome = findViewById(R.id.cadastroNome);
        cadastroDatanasc = findViewById(R.id.cadastroDatanasc);
        cadastroEmail = findViewById(R.id.cadastroEmail);
        cadastroSenha = findViewById(R.id.cadastroSenha);
        cadastroSexo = findViewById(R.id.cadastroSexo);
        cadastroFreqExercicios = findViewById(R.id.sbarFreqExerc);
        cadastroDisponibilidade = findViewById(R.id.sbarDisponibilidade);

        btnCadastrar = findViewById(R.id.cadastroBtn);

        // Spinners
        LinearLayout addSpinner_exercRealizado = findViewById(R.id.cadastroExercsRealizados);
        LinearLayout addSpinner_foco = findViewById(R.id.cadastroFoco);
        LinearLayout addSpinner_objetivo = findViewById(R.id.cadastroObjetivo);

        cadastroExercicioRealizado = addSpinner_exercRealizado.findViewById(R.id.addspinnerID);
        cadastroExercicioRealizadoBtn = addSpinner_exercRealizado.findViewById(R.id.btnAddspinner);
        cadastroExercicioRealizadoArray = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, R.array.ExercRealizado);
        cadastroExercicioRealizadoView = addSpinner_exercRealizado.findViewById(R.id.viewAddspinner);
        cadastroExercicioRealizadoViewAdapter = new Cadastro_RecyclerViewAdapter(this, cadastroExercicioRealizadoArray, this);
        cadastroExercicioRealizadoView.setAdapter(cadastroExercicioRealizadoViewAdapter);
        cadastroExercicioRealizadoView.setLayoutManager(new LinearLayoutManager(this));

        cadastroFocoTreino = addSpinner_foco.findViewById(R.id.addspinnerID);
        cadastroFocoTreinoBtn = addSpinner_foco.findViewById(R.id.btnAddspinner);
        cadastroObjetivoArray = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, R.array.FocoTreino);

        cadastroObjetivo = addSpinner_objetivo.findViewById(R.id.addspinnerID);
        cadastroObjetivoBtn = addSpinner_objetivo.findViewById(R.id.btnAddspinner);
        cadastroObjetivoArray = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, R.array.ObjTreino);

        // Spinners.Adapters
        cadastroExercicioRealizadoArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cadastroExercicioRealizado.setAdapter(cadastroExercicioRealizadoArray);


        //
        cadastroDisponibilidadeText = findViewById(R.id.textHelperDisponibilidade);
        cadastroFreqExerciciosText = findViewById(R.id.textHelperFreqExerc);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void cadastrar() {

        if(!UsuarioSession.getInstance(this).isEmailFree(cadastroEmail.getText().toString())) {
            Toast.makeText(this, "Email já cadastrado", Toast.LENGTH_SHORT).show();
            return;
        }

        EditText confirm = findViewById(R.id.cadastroSenhaConfirm);
        if(!cadastroSenha.getText().toString().equals(confirm.getText().toString())) {
            Toast.makeText(this, "Senha Errada", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            UsuarioSession usuS = UsuarioSession.getInstance(this);
            DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("HH:mm:ss");

            Usuario usuario = new Usuario();
            usuario.setNome(cadastroNome.getText().toString());
            usuario.setEmail(cadastroEmail.getText().toString());
            usuario.setDatanasc(LocalDate.parse(cadastroDatanasc.getText().toString()));
            usuario.setFoco(cadastroFocoTreino.getSelectedItem().toString());
            usuario.setSexo(cadastroSexo.getCheckedRadioButtonId() == R.id.cadastroMasc ? 'M' : 'F');
            usuario.setCondicao(cadastroFreqExerciciosText.getText().toString());
            usuario.setDisponibilidade(LocalTime.parse(cadastroDisponibilidadeText.getText().toString(), timeformat));

            Log.d("Localtime", usuario.getDisponibilidade().toString());
            usuS.cadastrar(usuario, cadastroSenha.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeItemExercRealizado(int position) {
        cadastroExercicioRealizadoArray.remove(cadastroExercicioRealizadoArray.getItem(position));
        cadastroExercicioRealizadoViewAdapter.notifyItemChanged(position);
    }
}
