package com.lintang.phonebook.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract  {

    public static final String AUTHORITY="com.lintang.phonebook";
    public static final String SCHEME="content";


    public  static final class PersonCollumns  implements BaseColumns {
             public static final String TABLE_NAME="person";
             public static  final String EMAIL="email";
             public static  final String PHONE="phone";
             public static  final String NAME="name";
             public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME).authority(AUTHORITY).appendPath(TABLE_NAME).build();
    }


    public static String getCollumnString(Cursor cursor,String collumName){
        return cursor.getString(cursor.getColumnIndexOrThrow(collumName));
    }
    public static int  getCollumnInt(Cursor cursor,String collumName){
        return cursor.getInt(cursor.getColumnIndexOrThrow(collumName));
    }
}
