package com.example.marci.myapplication.datasource;

import android.content.Context;

import com.example.marci.myapplication.dao.MensagensDAO;
import com.example.marci.myapplication.model.Mensagem;

import java.util.List;

/**
 * Created by marci on 12/12/2015.
 */
public class MensagensController {
    private static MensagensDAO mensagensDAO;
    private static MensagensController instance;

    public static MensagensController getInstance(Context context) {
        if (instance == null) {
            instance = new MensagensController();
            mensagensDAO = new MensagensDAO(context);
        }
        return instance;
    }

    public void insert(Mensagem mensagem) throws Exception {
        mensagensDAO.insert(mensagem);
    }

    public void update(Mensagem mensagem) throws Exception {
        mensagensDAO.update(mensagem);
    }

    public List<Mensagem> findAll() throws Exception {
        return mensagensDAO.findAll();
    }

    public void delete(Mensagem mensagem) throws Exception {
        mensagensDAO.delete(mensagem);
    }


}