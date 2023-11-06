package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.ConfiguracoesLembrete.AlarmReceiver;
import com.example.fitnessapp.ConfiguracoesLembrete.Lembrete_RecyclerViewAdapter;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Configuracoes extends AppCompatActivity {

    ArrayList<Date> dateLembrete = new ArrayList<>();
    RecyclerView recyclerViewLembrete;


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, Principal.class);
        startActivity(intent);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        if(!UsuarioSession.getInstance(this).isLogged()) { UsuarioSession.logOut(this); }

        init();
    }


    private void init() {
        recyclerViewLembrete = findViewById(R.id.lembrete_recyclerview);
        recyclerViewLembrete.setLayoutManager(new LinearLayoutManager(this));

        Button addLembrete = findViewById(R.id.lembrete_btnAdd);
        addLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLembrete();
            }
        });
    }


    private void setLembrete() {
        final Calendar hoje = Calendar.getInstance();
        final int hora = hoje.get(Calendar.HOUR_OF_DAY);
        final int minutos = hoje.get(Calendar.MINUTE);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Context context = (Context) getApplicationContext();

        TimePickerDialog time = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("UnspecifiedImmutableFlag")
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Intent alarmIntent = new Intent(context, AlarmReceiver.class);


                PendingIntent pendingIntent;
                Log.d("VERSÃO", String.valueOf(android.os.Build.VERSION_CODES.S));
                if(android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.S) {
                    pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);
                } else {
                    pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
                }


                Calendar alarmTime = Calendar.getInstance();
                alarmTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                alarmTime.set(Calendar.MINUTE, minute);
                long timeInMilliseconds = alarmTime.getTimeInMillis();

                alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMilliseconds, pendingIntent);
                /*if(timeInMilliseconds != 0 )
                    addDateLembrete_row( new Date(timeInMilliseconds) ); */
            }
        }, hora, minutos, true);
        time.show();


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addDateLembrete_row(Date date) {
        dateLembrete.add(date);
        Lembrete_RecyclerViewAdapter adapter = new Lembrete_RecyclerViewAdapter(this, dateLembrete);
        recyclerViewLembrete.setAdapter(adapter);
    }

}