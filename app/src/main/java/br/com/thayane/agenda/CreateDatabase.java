package br.com.thayane.agenda;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Thayane on 21/12/2016.
 */


public class CreateDatabase extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "calendar_database.db";
    static final String TABLE = "events";
    static final String ID = "_id";
    static final String DATE = "date";
    static final String DESCRIPTION = "description";
    static final int VERSAO = 12;

    public CreateDatabase(Context context){
        super(context, DATABASE_NAME,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABLE+" ("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DATE + " TEXT,"
                + DESCRIPTION + " TEXT"
                +")";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}

