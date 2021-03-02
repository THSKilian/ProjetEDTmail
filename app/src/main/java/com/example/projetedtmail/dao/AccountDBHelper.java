package com.example.projetedtmail.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AccountDBHelper extends SQLiteOpenHelper {
    /**
     * Les champs nécessaires à la création de la base de données
     */
    private static final String TABLE_USER = "table_user";

    public static final String COLONNE_USERNAME = "USERNAME";
    public static final int COLONNE_USERNAME_ID = 0;
    public static final String COLONNE_FIRSTNAME = "FIRSTNAME";
    public static final int COLONNE_FIRSTNAMEL_ID = 1;
    public static final String COLONNE_LASTNAME = "LASTNAME";
    public static final int COLONNE_LASTNAME_ID = 2;
    public static final String COLONNE_PASSWORD = "PASSWORD";
    public static final int COLONNE_PASSWORD_ID = 3;

    /**
     * La requête de création de la structure de la base de données.
     */
    private static final String REQUETE_CREATION_BD = "create table " + TABLE_USER + " ("
            + COLONNE_USERNAME + " text not null, "
            + COLONNE_FIRSTNAME + " text not null, "
            + COLONNE_LASTNAME + " text not null, "
            + COLONNE_PASSWORD + " text not null " + ");";

    /**
     * La requête de suppression de la structure de la base de données.
     */
    public static final String REQUETE_DROP_BD = "DROP TABLE IF EXISTS " + TABLE_USER + ";";


    public AccountDBHelper(Context context, String nom, SQLiteDatabase.CursorFactory cursorfactory, int version) {
        super(context, nom, cursorfactory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(REQUETE_CREATION_BD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(REQUETE_DROP_BD);
        onCreate(db);
    }
}
