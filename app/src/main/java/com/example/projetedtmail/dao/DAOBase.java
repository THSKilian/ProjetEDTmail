package com.example.projetedtmail.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public abstract class DAOBase {
    protected final static int VERSION = 1;
    protected final static String BASE_NOM = "user.db";

    protected SQLiteDatabase mDb = null;
    protected AccountDBHelper mHandler = null;

    public DAOBase(Context pContext){
        this.mHandler = new AccountDBHelper(pContext, BASE_NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }

}
