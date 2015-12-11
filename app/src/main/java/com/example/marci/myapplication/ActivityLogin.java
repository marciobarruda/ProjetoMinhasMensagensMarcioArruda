package com.example.marci.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.LayoutInflaterFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class ActivityLogin extends Activity {

    ListView listLogin;
    String login;
    Button newUser;
    ArrayList<String> loginArrayList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    SQLiteDatabase db;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        listLogin = (ListView) findViewById(R.id.listaLogin);
        adapter = new ArrayAdapter<String>(ActivityLogin.this, android.R.layout.simple_list_item_1, android.R.id.text1, loginArrayList);

        db = new DataBaseLogin(ActivityLogin.this).getWritableDatabase();
        cursor = db.query("loginTable", new String[]{"_id", "username", "password"}, null, null, null, null, null);
        cursor.moveToFirst();

        while (cursor.moveToNext()) {
           // String id = cursor.getString(0);
            login = cursor.getString(1);
            loginArrayList.add(login);
        }

        listLogin.setAdapter(adapter);

        newUser = (Button) findViewById(R.id.new_user);

        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View textEntryView = factory.inflate(R.layout.layout_login_aux, null);
                final EditText editUser = (EditText) textEntryView.findViewById(R.id.editUser);
                final EditText editSenha = (EditText) textEntryView.findViewById(R.id.editSenha);
                final EditText editConfSenha = (EditText) textEntryView.findViewById(R.id.editSenha2);

                AlertDialog.Builder cadastrar = new AlertDialog.Builder(ActivityLogin.this);
                cadastrar.setTitle("Cadastrar novo usuário");
                cadastrar.setView(textEntryView);

                cadastrar.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String user = editUser.getText().toString();
                        String senha = editSenha.getText().toString();
                        String confSenha = editConfSenha.getText().toString();

                        ContentValues valor = new ContentValues();
                        if (senha.equals(confSenha)) {
                            valor.put("username", user);
                            valor.put("password", confSenha);
                            db.insert("loginTable", null, valor);

                            loginArrayList.add(user);
                            adapter.notifyDataSetChanged();
                            listLogin.setAdapter(adapter);

                        } else {
                            Toast.makeText(ActivityLogin.this, "As senhas não conferem. Login não cadastrado", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                cadastrar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                cadastrar.create();
                cadastrar.show();
            }
        });


        listLogin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View textEntryView = factory.inflate(R.layout.logar, null);
                final EditText userLogar = (EditText) textEntryView.findViewById(R.id.userLogar);
                final EditText editSenhaLogar = (EditText) textEntryView.findViewById(R.id.editSenhaLogar);

                AlertDialog.Builder login = new AlertDialog.Builder(ActivityLogin.this);

                login.setTitle("Login");
                login.setView(textEntryView);

                login.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String usuarioLogar = userLogar.getText().toString();
                        usuarioLogar = parent.getAdapter().getItem(position).toString();

                        String senhaLogar = editSenhaLogar.getText().toString();

                        db = new DataBaseLogin(ActivityLogin.this).getWritableDatabase();
                        Cursor cu = db.query("loginTable", null, null, null, null, null, null);

                        while (cu.moveToNext()) {

                            if (cu.getCount() > 0) {
                                if (usuarioLogar.equals(cu.getString(1)) && senhaLogar.equals(cu.getString(2))) {

                                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(i);

                                }
                            }
                        }
                    }

                });

                login.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()

                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }

                );
                login.create();
                login.show();
            }
        });


    }

}
