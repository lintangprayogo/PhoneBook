package com.lintang.phonebook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.lintang.phonebook.R;
import com.lintang.phonebook.model.Person;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.EMAIL;
import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.NAME;
import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.PHONE;
import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.TABLE_NAME;

public class PersonHelper {
    private static final String TABLE= TABLE_NAME;
    private final DatabaseHelper databaseHelper;
    private PersonHelper INSTANCE;
    private SQLiteDatabase database;


    public PersonHelper(Context context) {
        this.databaseHelper = new DatabaseHelper(context);
    }

    public PersonHelper getInstance(Context context) {
        if(INSTANCE==null){
           synchronized (SQLiteOpenHelper.class){
               INSTANCE = new PersonHelper(context);
           }
        }

        return INSTANCE;
    }



    public  void open () throws SQLException {
         database = databaseHelper.getWritableDatabase();
    }



     public ArrayList<Person> query(){
        ArrayList<Person>  persons = new ArrayList<>();
         Cursor cursor = database.query(TABLE,
                 null,
                 null,
                 null,
                 null,
                 null,
                 null);
        cursor.moveToFirst();
        if(cursor.getCount()<0) {
            do {
                String email = DatabaseContract.getCollumnString(cursor, EMAIL);
                String phone = DatabaseContract.getCollumnString(cursor, PHONE);
                String name = DatabaseContract.getCollumnString(cursor, NAME);
                int id = DatabaseContract.getCollumnInt(cursor, _ID);
                persons.add(new Person(email, phone, name, id));
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
           cursor.close();
        }
        return  persons;
     }


     public Cursor queryByIdProvider(String id){
        return  database.query(TABLE_NAME,
                null,
                _ID+" = ? ",
                new String[]{id},
                null,
                null,
                NAME);
     }

     public Cursor queryProvider(){
        return  database.query(TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                NAME);
     }

     public long insertProvider(ContentValues values){
         return  database.insert(TABLE,null,values);
     }

     public int updateProvider(ContentValues values,String id){
        return database.update(TABLE,values,_ID+" = ?",new String[]{id});
     }

    public int deleteProvider(String id) {
        return database.delete(TABLE, _ID + " = ?", new String[]{id});
    }

}
