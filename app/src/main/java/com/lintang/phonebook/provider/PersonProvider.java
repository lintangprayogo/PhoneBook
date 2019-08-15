package com.lintang.phonebook.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import com.lintang.phonebook.activity.MainActivity;
import com.lintang.phonebook.database.PersonHelper;

import static com.lintang.phonebook.database.DatabaseContract.AUTHORITY;
import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.CONTENT_URI;
import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.TABLE_NAME;

public class PersonProvider extends ContentProvider {

    private static final int PERSON = 1;
    private static final int PERSON_ID = 2;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY,TABLE_NAME,PERSON);
        sUriMatcher.addURI(AUTHORITY,TABLE_NAME+"/#",PERSON_ID);
    }
    private PersonHelper personHelper;


    @Override
    public boolean onCreate() {
        personHelper= new PersonHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        int  match=sUriMatcher.match(uri);
        Cursor cursor=null;
        personHelper.open();
        switch (match){
            case  PERSON:
                cursor=  personHelper.queryProvider();
                break;
            case PERSON_ID:
                cursor=  personHelper.queryByIdProvider(uri.getLastPathSegment());
        }

        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        long added;
         int match=sUriMatcher.match(uri);
         personHelper.open();
        switch (match){
            case  PERSON:
                added = personHelper.insertProvider(contentValues);
                Log.d("TAG", "insert: match");
            default:
                added=0;
        }
        Log.d("TAG", "insert: "+added);
        getContext().getContentResolver().notifyChange(CONTENT_URI,new MainActivity.DataObserver(new Handler(),getContext()));
        return Uri.parse(CONTENT_URI+"/"+added);
    }

    @Override
        public int delete(Uri uri, String s, String[] strings) {
        int delete;
        int match=sUriMatcher.match(uri);
        personHelper.open();
        switch (match){
            case  PERSON_ID:
                delete = personHelper.deleteProvider(uri.getLastPathSegment());
                Log.d("TAG", "delete: "+match);
            default:
                delete=0;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI,new MainActivity.DataObserver(new Handler(),getContext()));
        return delete;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int updated=0;
        int match= sUriMatcher.match(uri);
        personHelper.open();
        switch (match){
            case PERSON_ID:
                updated= personHelper.updateProvider(contentValues,uri.getLastPathSegment());
                default:
                    updated=0;
        }

        getContext().getContentResolver().notifyChange(CONTENT_URI,new MainActivity.DataObserver(new Handler(),getContext()));
        return updated;
    }
}
