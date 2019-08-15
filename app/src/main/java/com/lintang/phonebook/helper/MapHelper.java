package com.lintang.phonebook.helper;

import android.database.Cursor;

import com.lintang.phonebook.database.DatabaseContract;
import com.lintang.phonebook.model.Person;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;
import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.*;

public class MapHelper  {

    public  static ArrayList<Person> cursorMapHelper (Cursor cursor){
        ArrayList<Person >persons=new ArrayList<>();
        while (cursor.moveToNext()){
            int id= DatabaseContract.getCollumnInt(cursor,_ID);
            String email= DatabaseContract.getCollumnString(cursor,EMAIL);
            String phone= DatabaseContract.getCollumnString(cursor,PHONE);
            String name= DatabaseContract.getCollumnString(cursor,NAME);
            persons.add(new Person(email,phone,name,id));
        }
        return persons;
    }
}
