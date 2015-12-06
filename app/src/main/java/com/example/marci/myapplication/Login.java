package com.example.marci.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends Activity {

    public static final String KEY_INFO_LOGIN = "KEY_INFO_LOGIN";

    private EditText editTextLogin;
    private EditText editTextSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextLogin = (EditText) findViewById(R.id.editTextLogin);
        editTextSenha = (EditText) findViewById(R.id.editTextSenha);
    }

    public void onClickEntrar(View view) {

        Bundle bundleLogin = new Bundle();
        String login = editTextLogin.getText().toString();
        bundleLogin.putString(KEY_INFO_LOGIN, login);
        Intent logar = new Intent(this, MainActivity.class);
        logar.putExtras(bundleLogin);
        startActivity(logar);
    }

}
