package com.example.marci.myapplication;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

    }
}
