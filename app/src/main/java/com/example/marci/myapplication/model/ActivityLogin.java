package com.example.marci.myapplication.model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.example.marci.myapplication.R;
import com.example.marci.myapplication.datasource.UserController;


public class ActivityLogin extends Activity {


    private EditText editUsuario, editSenha;
    private Context context;
    private UserController userController;
    private AlertDialog.Builder alert;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;
        userController = UserController.getInstance(context);
        editUsuario = (EditText) findViewById(R.id.editUsuario);
        editSenha = (EditText) findViewById(R.id.editSenha);
    }

    public void exibeDialogo(String mensagem) {
        alert = new AlertDialog.Builder(context);
        alert.setPositiveButton(R.string.ok, null);
        alert.setMessage(mensagem);
        alert.create().show();
    }

    public void validar(View view) throws Exception {
        String usuario = editUsuario.getText().toString();
        String senha = editSenha.getText().toString();


        try {
            boolean isValid = userController.validaLogin(usuario, senha);
            if (isValid) {
                Intent i = new Intent(ActivityLogin.this, MainActivity.class);
                startActivity(i);
                finish();
            } else {
                exibeDialogo(String.valueOf(R.string.verifique));
            }
        } catch (Exception e) {
            exibeDialogo(String.valueOf(R.string.validando));
            e.printStackTrace();
        }
    }

    public void cadastrar(View view) {

        LayoutInflater factory = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View textEntryView = factory.inflate(R.layout.layout_login_aux, null);
        final EditText editUser = (EditText) textEntryView.findViewById(R.id.editUsuario);
        final EditText editSenha = (EditText) textEntryView.findViewById(R.id.editPassword);
        final EditText editConfSenha = (EditText) textEntryView.findViewById(R.id.editConfPassword);

        AlertDialog.Builder cadastrar = new AlertDialog.Builder(ActivityLogin.this);
        cadastrar.setTitle(R.string.cadastrar_login);
        cadastrar.setView(textEntryView);

        cadastrar.setPositiveButton(R.string.cadastrar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = editUser.getText().toString();
                String senha = editSenha.getText().toString();
                String confSenha = editConfSenha.getText().toString();


                if (confSenha.equals(senha)) {
                    User usuario = new User(1, user, confSenha);
                    try {
                        UserController.getInstance(getApplicationContext()).insert(usuario);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    exibeDialogo(String.valueOf(R.string.senhas_invalidas));
                }

            }
        });

        cadastrar.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        cadastrar.create();
        cadastrar.show();

    }
}

