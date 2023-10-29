package com.example.fitnessapp.CadastroClasses;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

public class CampoData implements android.app.DatePickerDialog.OnDateSetListener {

    private final TextView dataTexto;


    @SuppressLint("SetTextI18n")
    public CampoData(FragmentManager fragmentManager, TextView dataTexto, ImageButton dataPicker) {
        this.dataTexto = dataTexto;
        dataTexto.setText("20/10/2020");



        dataPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment(CampoData.this);
                datePicker.show(fragmentManager, "date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dataTexto.setText(dateFormat.format(c.getTime()));
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public LocalDate getDate() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(dataTexto.getText().toString(), format);
    }
}
