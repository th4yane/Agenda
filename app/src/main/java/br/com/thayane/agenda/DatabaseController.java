package br.com.thayane.agenda;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Thayane on 21/12/2016.
 */

public class DatabaseController {
    private SQLiteDatabase db;
    private CreateDatabase calendar_database;

    public DatabaseController(Context context) {
        calendar_database = new CreateDatabase(context);
    }

    public String insertData(String date, String description) {
        ContentValues values;
        long feedback;

        db = calendar_database.getWritableDatabase();
        values = new ContentValues();
        values.put(CreateDatabase.DATE, date);
        values.put(CreateDatabase.DESCRIPTION, description);

        feedback = db.insert(CreateDatabase.TABLE, null, values);
        db.close();

        if (feedback == -1)
            return "Insert Error";
        else
            return "Insert OK";

    }

    public Cursor returnData(String date){
        Cursor cursor;
        String[] campos =  {calendar_database.DESCRIPTION};
        String where = CreateDatabase.DATE + "= '" + date +"'";
        db = calendar_database.getReadableDatabase();
        cursor = db.query(CreateDatabase.TABLE,campos,where, null, null, null, null, null);
        if(cursor!=null){
          cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public void alterData(String date, String description){
        ContentValues values;
        String where;

        db = calendar_database.getWritableDatabase();

        where = CreateDatabase.DATE + "= '" + date + "'";

        values = new ContentValues();
        values.put(CreateDatabase.DESCRIPTION, description);

        db.update(CreateDatabase.TABLE,values,where,null);
        db.close();
    }

    public void deleteData(String date){
        String where = CreateDatabase.DATE + "= '" + date +"'";
        db = calendar_database.getReadableDatabase();
        db.delete(CreateDatabase.TABLE,where,null);
        db.close();
    }
}
