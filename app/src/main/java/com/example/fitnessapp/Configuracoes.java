package com.example.fitnessapp;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fitnessapp.ConfiguracoesLembrete.AlarmReceiver;
import com.example.fitnessapp.db.classes.UsuarioSession;

import java.util.Calendar;

public class Configuracoes extends AppCompatActivity {


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
        Button tst = findViewById(R.id.lembrete_btnAdd);
        tst.setOnClickListener(new View.OnClickListener() {
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
            }
        }, hora, minutos, true);
        time.show();
    }

}