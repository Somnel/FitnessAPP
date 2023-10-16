package com.example.fitnessapp.fragmentos;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.fitnessapp.R;
import com.example.fitnessapp.db.classes.Usuario;
import com.example.fitnessapp.db.classes.UsuarioSession;

public class cadastroFragments {

    public static Fragment createPagina1Fragment() {
        return new Pagina1Fragment();
    }

    public static Fragment createPagina2Fragment() {
        return new Pagina2Fragment();
    }

    public static Fragment createPagina3Fragment() {
        return new Pagina3Fragment();
    }

    public static class Pagina1Fragment extends Fragment {
        private EditText nome;
        private Switch sexo;
        private EditText datanasc;
        private EditText email;
        private EditText senha;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.cadastro_fragmento1, container, false);
        }
    }

    public static class Pagina2Fragment extends Fragment {
        private SeekBar usuCondicao;
        private TextView cadastroCondicao;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.cadastro_fragmento2, container, false);

            // init()
            cadastroCondicao = view.findViewById(R.id.cadastro_txtcondicao);
            usuCondicao = view.findViewById(R.id.cadastro_condicao);
            // ---


            usuCondicao.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(progress >= 4) cadastroCondicao.setText("Expert");
                    else if(progress >= 2) cadastroCondicao.setText("Praticante");
                    else cadastroCondicao.setText("Sedentário");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // Enquanto usa
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // Quando para de usar
                }
            });

            return view;
        }
    }

    public static class Pagina3Fragment extends Fragment {
        private Context mContext;

        @Override
        public void onAttach(@NonNull Context context) {
            super.onAttach(context);
            mContext = context;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.cadastro_fragmento3, container, false);

            String senha = "AA"; // TEMP

            Button btnCadastro = view.findViewById(R.id.btn_cadastrar);
            btnCadastro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Usuario usuario = new Usuario();
                    cadastro(usuario, senha);
                }
            });

            return view;
        }
        private void cadastro(Usuario usuario, String senha) {
            UsuarioSession usuarioSession = UsuarioSession.getInstance(mContext);
            if(usuarioSession.isEmailFree(usuario.getEmail())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    usuarioSession.cadastrar(usuario, senha);
                }
            } else {
                return;
            }
        }
    }
}