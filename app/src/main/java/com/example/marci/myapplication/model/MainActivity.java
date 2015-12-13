package com.example.marci.myapplication.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.marci.myapplication.R;
import com.example.marci.myapplication.datasource.MensagensController;
import com.example.marci.myapplication.others.Mask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ArrayList<String> mensagens;
    ArrayAdapter<String> adapter;
    private ListView listView;
    private Context context;
    private MensagensController mensagensController = new MensagensController();
    private AlertDialog.Builder alert;
    private List<Mensagem> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mensagens = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, mensagens);


        context = this;
        mensagensController = MensagensController.getInstance(context);
        listView = (ListView) findViewById(R.id.listview);


        try {
            lista = MensagensController.getInstance(getApplicationContext()).findAll();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Mensagem mensagem : lista) {
            String nome = mensagem.getMensagem();
            mensagens.add(nome);
        }
        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {

                LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View textEntryView = factory.inflate(R.layout.auxiliar2, null);
                final EditText numero = (EditText) textEntryView.findViewById(R.id.editText);
                numero.addTextChangedListener(Mask.insert("(##)#####-####", numero));

                AlertDialog.Builder enviar = new AlertDialog.Builder(MainActivity.this);
                enviar.setTitle(R.string.enviar);
                enviar.setView(textEntryView);

                enviar.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
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

                enviar.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener() {
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
                editar.setPositiveButton(R.string.editar, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        LayoutInflater factory = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View textEntryView = factory.inflate(R.layout.alert_ultima, null);
                        final EditText edit_mensagem = (EditText) textEntryView.findViewById(R.id.new_text);

                        AlertDialog.Builder ultima = new AlertDialog.Builder(MainActivity.this);
                        ultima.setTitle(R.string.editar_mensagem);
                        ultima.setView(textEntryView);

                        ultima.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                String novo = edit_mensagem.getText().toString();
                                Mensagem mensagem = new Mensagem(position, novo);

                                try {
                                    String atual = parent.getAdapter().getItem(position).toString();
                                    lista = MensagensController.getInstance(getApplicationContext()).findAll();
                                    for (Mensagem sms : lista) {
                                        if (sms.getMensagem().equals(atual)) {
                                            mensagem = sms;
                                        }
                                    }
                                    mensagem.setMensagem(novo);
                                    MensagensController.getInstance(getApplicationContext()).update(mensagem);

                                    mensagens.set(position, novo);
                                    adapter.notifyDataSetChanged();
                                    listView.setAdapter(adapter);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                mensagens.set(position, novo);
                                adapter.notifyDataSetChanged();
                                listView.setAdapter(adapter);

                                Toast.makeText(MainActivity.this, R.string.gravada, Toast.LENGTH_SHORT).show();

                            }

                        });


                        ultima.setNegativeButton(R.string.cancelar, new DialogInterface.OnClickListener()

                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        ultima.create();
                        ultima.show();

                    }
                });

                editar.setNegativeButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String clicked = parent.getAdapter().getItem(position).toString();
                        Mensagem mensagem = new Mensagem(position, clicked);

                        try {

                            lista = MensagensController.getInstance(getApplicationContext()).findAll();
                            for (Mensagem texto : lista) {
                                if (texto.getMensagem().equals(clicked)) {
                                    mensagem = texto;

                                }
                            }

                            MensagensController.getInstance(getApplicationContext()).delete(mensagem);

                            mensagens.remove(clicked);
                            adapter.notifyDataSetChanged();
                            listView.setAdapter(adapter);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                });
                editar.create();
                editar.show();

                return true;
            }
        });

    }

    public void exibeDialogo(String mensagem) {
        alert = new AlertDialog.Builder(context);
        alert.setPositiveButton(R.string.ok, null);
        alert.setMessage(mensagem);
        alert.create().show();

    }

    public void nova(View view) {

        LayoutInflater factory = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        final View textEntryView = factory.inflate(R.layout.auxiliar, null);
        final EditText editMensagem = (EditText) textEntryView.findViewById(R.id.new_edit_text);

        AlertDialog.Builder cadastrar = new AlertDialog.Builder(MainActivity.this);
        cadastrar.setTitle(R.string.new_mensagem);
        cadastrar.setView(textEntryView);

        cadastrar.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String mensagem = editMensagem.getText().toString();
                Mensagem sms = new Mensagem(mensagens.size(), mensagem);

                try {
                    MensagensController.getInstance(getApplicationContext()).insert(sms);
                    mensagens.add(mensagem);
                    adapter.notifyDataSetChanged();
                    listView.setAdapter(adapter);

                } catch (Exception e) {
                    e.printStackTrace();
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


