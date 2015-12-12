package com.example.marci.myapplication;

import android.content.Context;

import java.util.List;

/**
 * Created by marci on 12/12/2015.
 */
public class UserController {
    private static UserDAO userDAO;
    private static UserController instance;

    public static UserController getInstance(Context context) {
        if (instance == null) {
            instance = new UserController();
            userDAO = new UserDAO(context);
        }
        return instance;
    }

    public void insert(User user) throws Exception {
        userDAO.insert(user);
    }

    public void update(User user) throws Exception {
        userDAO.update(user);
    }

    public List<User> findAll() throws Exception {
        return userDAO.findAll();
    }

    public boolean validaLogin(String usuario, String senha) throws Exception {
        User user = userDAO.findByLogin(usuario, senha);
        if (user == null || user.getUsuario() == null || user.getSenha() == null) {
            return false;
        }
        String informado = usuario + senha;
        String esperado = user.getUsuario() + user.getSenha();
        if (informado.equals(esperado)) {
            return true;
        }
        return false;

    }

}