package com.example.projetedtmail.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.example.projetedtmail.beans.User;

import java.util.ArrayList;

public class UserDAO extends DAOBase {
    public static final String TABLE_USER = "table_user";
    public static final String COLONNE_USERNAME = "USERNAME";
    public static final int COLONNE_USERNAME_ID = 0;
    public static final String COLONNE_FIRSTNAME = "FIRSTNAME";
    public static final int COLONNE_FIRSTNAME_ID = 1;
    public static final String COLONNE_LASTNAME = "LASTNAME";
    public static final int COLONNE_LASTNAME_ID = 2;
    public static final String COLONNE_PASSWORD = "PASSWORD";
    public static final int COLONNE_PASSWORD_ID = 3;

    private static final String REQUETE_CREATION_BD = "create table " + TABLE_USER + " ("
            + COLONNE_USERNAME + " primary key text not null, "
            + COLONNE_FIRSTNAME + " text not null, "
            + COLONNE_LASTNAME + " text not null, "
            + COLONNE_PASSWORD + " text not null " + ");";

    public static final String REQUETE_DROP_BD = "DROP TABLE IF EXISTS " + TABLE_USER + ";";

    public UserDAO(Context pContext) {
        super(pContext);
    }


    public void ajouter (User user){
        ContentValues value = new ContentValues();
        value.put(UserDAO.COLONNE_USERNAME, user.getUsername());
        value.put(UserDAO.COLONNE_FIRSTNAME, user.getFirstname());
        value.put(UserDAO.COLONNE_LASTNAME, user.getLastname());
        value.put(UserDAO.COLONNE_PASSWORD, user.getPassword());
        mDb.insert(UserDAO.TABLE_USER, null, value);

    }

    public void supprimer (User user){
        mDb.delete(TABLE_USER, COLONNE_USERNAME + " = ?", new String[] {String.valueOf(user.getUsername())});
    }

    public void modifier (User user){
        ContentValues value = new ContentValues();
        value.put(COLONNE_USERNAME, user.getUsername());
        value.put(UserDAO.COLONNE_FIRSTNAME, user.getFirstname());
        value.put(UserDAO.COLONNE_LASTNAME, user.getLastname());
        value.put(UserDAO.COLONNE_PASSWORD, user.getPassword());
        mDb.update(TABLE_USER, value, COLONNE_USERNAME  + " = ?", new String[] {String.valueOf(user.getUsername())});
    }

    public ArrayList<User> getAllData (){
        Cursor c = mDb.query(TABLE_USER, new String[]{
                COLONNE_USERNAME,COLONNE_FIRSTNAME,COLONNE_LASTNAME,COLONNE_PASSWORD },
               null , null, null, null, null);

        ArrayList<User> userArray = new ArrayList<>();

        while (c.moveToNext()) {
            String username = c.getString(0);
            String firstname = c.getString(1);
            String lastname = c.getString(2);
            String password = c.getString(3);

            User user = new User(username, firstname, lastname, password);
            userArray.add(user);
        }

        c.close();
        return userArray;
    }

}
