package com.example.fitnessapp.CadastroClasses;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fitnessapp.R;

import java.util.ArrayList;

public class AddSpinner implements CadastroInterface {

    Context context;
    private final Spinner spin;
    private final Button spinBtn;
    private final Cadastro_RecyclerViewAdapter viewAdapter;
    private ArrayList<String> listItens;
    private final ArrayList<String> listItensSelecionados;


    public AddSpinner(Context context, Spinner spin, Button spinBtn, RecyclerView view, ArrayList<String> listItens) {
        this.context = context;
        this.spin = spin;
        this.spinBtn = spinBtn;
        this.listItens = listItens;


        this.listItensSelecionados = new ArrayList<>();
        this.viewAdapter = new Cadastro_RecyclerViewAdapter(context, listItensSelecionados, this);

        view.setAdapter(viewAdapter);
        view.setLayoutManager(new LinearLayoutManager(context));

        updateSpinner();


        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(spin.getCount() != 0) {
                    String itemSelecionado = spin.getSelectedItem().toString();

                    if(!TextUtils.isEmpty(itemSelecionado)) {
                        listItensSelecionados.add(itemSelecionado);
                        updateSpinner();
                    }
                }
            }
        });
    }

    @Override
    public void removeItemExercRealizado(int position) {
        listItensSelecionados.remove(position);
        viewAdapter.notifyItemRemoved(position);
        updateSpinner();
    }

    @Override
    public void updateSpinner() {
        try{
            // -> Itens
            ArrayList<String> itensAdapter = new ArrayList<>(listItens);
            for(String item : listItensSelecionados) itensAdapter.remove(item);

            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, itensAdapter);
            spin.setAdapter(adapter);

            viewAdapter.notifyItemInserted(listItensSelecionados.size());
            // -> Spinner
            if(spin.getAdapter().getCount() != 0) {
                spin.setEnabled(false);
                spin.setBackgroundResource(R.drawable.addspinner_field);
                spinBtn.setBackgroundResource(R.drawable.addspinner_button);
            } else {
                spin.setEnabled(true);
                spin.setBackgroundResource(R.drawable.addspinner_field_disabled);
                spinBtn.setBackgroundResource(R.drawable.addspinner_button_disabled);
            }

        } catch (Exception e) {
            Log.e("{Exceção em" + spin.toString(), String.valueOf(e));
        }
    }

    @Override
    public ArrayList<String> getSelectedItens() {
        return listItensSelecionados;
    }

    public boolean reachSizeLista() {
        return listItensSelecionados.equals(listItens);
    }


    @Override
    public String getSelectedItem() {
        return spin.getSelectedItem().toString();
    }
}
