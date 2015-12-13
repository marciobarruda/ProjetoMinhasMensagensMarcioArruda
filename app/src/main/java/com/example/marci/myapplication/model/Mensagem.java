package com.example.marci.myapplication.model;

/**
 * Created by marci on 12/12/2015.
 */
public class Mensagem {

    private Integer id;
    private String mensagem;


    public Mensagem(Integer id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}