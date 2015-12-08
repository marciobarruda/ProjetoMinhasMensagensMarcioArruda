package com.example.marci.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    Button newMensage;
    ArrayList<String> mensagens = new ArrayList<String>();
    TextView text_mensage;
    String texto;
    Cursor c;
    ArrayAdapter<String> adapter;
    SQLiteDatabase db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        listView = (ListView) findViewById(R.id.listview);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mensagens);
        db = new DataBaseHandler(MainActivity.this).getWritableDatabase();
        c = db.query("mensages", new String[]{"_id", "mensage"}, null, null, null, null, null);
        c.moveToFirst();

        while (c.moveToNext()) {
            String id = c.getString(0);
            texto = c.getString(1);
            mensagens.add(texto);
        }
        //c.close();

        listView.setAdapter(adapter);

        text_mensage = (TextView) findViewById(R.id.text_mensage);

        newMensage = (Button) findViewById(R.id.new_mensage);
        newMensage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View textEntryView = factory.inflate(R.layout.auxiliar, null);
                final EditText editText = (EditText) textEntryView.findViewById(R.id.new_edit_text);

                AlertDialog.Builder alerta = new AlertDialog.Builder(MainActivity.this);
                alerta.setTitle("Nova Mensagem");
                alerta.setView(textEntryView);

                alerta.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        texto = editText.getText().toString();
                        ContentValues values = new ContentValues();
                        values.put("mensage", texto);
                        db.insert("mensages", null, values);
                        mensagens.add(texto);

                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);

                        Toast.makeText(MainActivity.this, "A MENSAGEM: " + editText.getText().toString() + " FOI GRAVADA COM SUCESSO!", Toast.LENGTH_SHORT).show();
                    }
                });

                alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                alerta.create();
                alerta.show();

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {

                LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View textEntryView = factory.inflate(R.layout.auxiliar2, null);
                final EditText numero = (EditText) textEntryView.findViewById(R.id.editText);

                AlertDialog.Builder enviar = new AlertDialog.Builder(MainActivity.this);
                enviar.setTitle("Enviar Mensagem");
                enviar.setView(textEntryView);

                enviar.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String telefone = numero.getText().toString();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        String content = listView.getItemAtPosition(position).toString();
                        intent.setType("vnd.android-dir/mms-sms");
                        intent.putExtra("address", telefone);
                        intent.putExtra("sms_body", content);
                        startActivity(intent);
                    }
                });

                enviar.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
                enviar.create();
                enviar.show();

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {


            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, final long id) {

                AlertDialog.Builder editar = new AlertDialog.Builder(MainActivity.this);
                editar.setPositiveButton("Editar", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View textEntryView = factory.inflate(R.layout.alert_ultima, null);
                        final EditText edit_mensagem = (EditText) textEntryView.findViewById(R.id.new_text);

                        AlertDialog.Builder ultima = new AlertDialog.Builder(MainActivity.this);
                        ultima.setTitle("Editar Mensagem");
                        ultima.setView(textEntryView);

                        ultima.setPositiveButton("Ok", new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                String click = parent.getAdapter().getItem(position).toString();
                                String novo;

                                novo = edit_mensagem.getText().toString();
                                ContentValues valores = new ContentValues();
                                valores.put("mensage", novo);
                                db.update("mensages", valores, "mensage=?", new String[]{click});

                                mensagens.set(position, novo);
                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);

                                Toast.makeText(MainActivity.this, "A MENSAGEM FOI GRAVADA COM SUCESSO!", Toast.LENGTH_SHORT).show();

                            }

                        });


                        ultima.setNegativeButton("Cancelar", new DialogInterface.OnClickListener()

                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ultima.create();
                        ultima.show();

                    }
                });

                editar.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String clicked = parent.getAdapter().getItem(position).toString();

                        db.delete("mensages", "mensage=?", new String[]{clicked});

                        mensagens.remove(position);

                        adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);



                      /*  AlertDialog.Builder teste = new AlertDialog.Builder(MainActivity.this);
                        LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View textEntryView = factory.inflate(R.layout.edit_alert, null);
                        final Button editar = (Button) textEntryView.findViewById(R.id.button_editar);
                        final Button delete = (Button) textEntryView.findViewById(R.id.button_deletar);
                        teste.setView(textEntryView);
                        teste.create();
                        teste.show();*/

                    }
                });
                editar.create();
                editar.show();

                return true;
            }
        });
    }

}


