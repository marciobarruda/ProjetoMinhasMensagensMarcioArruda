package com.example.marci.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marci on 12/12/2015.
 */

public class UserDAO extends DataBaseLogin {
    private final String TABLE = "usuario";

    public UserDAO(Context context) {
        super(context);
    }

    public void insert(User usuario) throws Exception {
        ContentValues values = new ContentValues();

        values.put("usuario", usuario.getUsuario());
        values.put("senha", usuario.getSenha());

        getDatabase().insert(TABLE, null, values);
    }

    public void update(User usuario) throws Exception {
        ContentValues values = new ContentValues();

        values.put("usuario", usuario.getUsuario());
        values.put("senha", usuario.getSenha());

        getDatabase().update(TABLE, values, "id = ?", new String[] { "" + usuario.getId() });
    }

    public User findById(Integer id) {

        String sql = "SELECT * FROM " + TABLE + " WHERE id = ?";
        String[] selectionArgs = new String[] { "" + id };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        return montaUsuario(cursor);
    }

    public List<User> findAll() throws Exception {
        List<User> retorno = new ArrayList<User>();
        String sql = "SELECT * FROM " + TABLE;
        Cursor cursor = getDatabase().rawQuery(sql, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            retorno.add(montaUsuario(cursor));
            cursor.moveToNext();
        }
        return retorno;
    }

    public User montaUsuario(Cursor cursor) {
        if (cursor.getCount() == 0) {
            return null;
        }
        Integer id = cursor.getInt(cursor.getColumnIndex("id"));
        String usuario = cursor.getString(cursor.getColumnIndex("usuario"));
        String senha = cursor.getString(cursor.getColumnIndex("senha"));

        return new User(id, usuario, senha);

    }

    public User findByLogin(String usuario, String senha) {
        String sql = "SELECT * FROM " + TABLE + " WHERE usuario = ? AND senha = ?";
        String[] selectionArgs = new String[] { usuario, senha };
        Cursor cursor = getDatabase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        return montaUsuario(cursor);
    }

}
