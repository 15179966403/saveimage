package com.hrc.administrator.test.contacts;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/12/19.
 */

public class DBService extends SQLiteOpenHelper{
    private final static int DATABASE_VERSION=3;
    private final static String DATABASE_NAME="contact.db";

    public DBService(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE [t_contacts] (" +
                "[id] AUTOINC," +
                "[name] VARCHAR(20) NOT NULL ON CONFLICT FAIL," +
                "[telephone] VARCHAR(20) NOT NULL ON CONFLICT FAIL." +
                "[eamil] VARCHAR(20)," +
                "[photo] BINARY," +
                "CONSTRAINT [sqlite_autoindex_t_contacts_1] PRIMARY KEY ([id])";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void execSQL(String sql,Object[] args){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(sql,args);
    }

    public Cursor query(String sql,String[] args){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(sql,args);
        return cursor;
    }
}
