package com.lintang.phonebook;

import android.database.Cursor;

public interface LoadCallback  {
    void preExcute();
    void postExcute(Cursor cursor);
}
