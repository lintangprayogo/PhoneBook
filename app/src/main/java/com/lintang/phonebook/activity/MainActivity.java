package com.lintang.phonebook.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.lintang.phonebook.DetailInterface;
import com.lintang.phonebook.LoadCallback;
import com.lintang.phonebook.R;
import com.lintang.phonebook.adapter.PersonAdapter;
import com.lintang.phonebook.helper.MapHelper;
import com.lintang.phonebook.model.Person;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.CONTENT_URI;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
        , LoadCallback, DetailInterface {


    private FloatingActionButton add;
    private HandlerThread handlerThread;
    private ProgressBar progressBar;
    private PersonAdapter adapter;
    private RecyclerView rvPerson;
    private DataObserver myObserver;
    private static String TAG = "MainActivity";
    private static String TAG_STATE = "LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add= findViewById(R.id.add);
        progressBar=findViewById(R.id.load);
        rvPerson=findViewById(R.id.rv_person);
        rvPerson.setLayoutManager(new LinearLayoutManager(this));
        adapter=new PersonAdapter(getResources(),this);
        rvPerson.setAdapter(adapter);
        add.setOnClickListener(this);


        handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(CONTENT_URI, true, myObserver);

        if (savedInstanceState == null) {
            new LoadPersonAsync(this, this).execute();
        } else {
            ArrayList<Person> list = savedInstanceState.getParcelableArrayList(TAG_STATE);
            if(list!=null){
                adapter.setData(list);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList(TAG_STATE,adapter.getPersons());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.add:
                Intent intent = new Intent(this,UpdateAddActivity.class);
                startActivity(intent);
        }
    }

    @Override
    public void preExcute() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void postExcute(Cursor cursor) {
            ArrayList<Person> persons = MapHelper.cursorMapHelper(cursor);

        if (persons.size() > 0) {
            adapter.setData(persons);
        } else {
            adapter.setData(persons);
            showSnackbarMessage("There Is No Entry");
        }
        progressBar.setVisibility(View.GONE);

    }




    public  static  class LoadPersonAsync extends AsyncTask<Void , Void,Cursor>{
         private  final WeakReference<Context> contextWeakReference;
         private final WeakReference<LoadCallback> callbackWeakReference ;

        public LoadPersonAsync(Context context,LoadCallback loadCallback) {
            this.contextWeakReference = new WeakReference<>(context);
            this.callbackWeakReference = new WeakReference<>(loadCallback);
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            return contextWeakReference.get().getContentResolver().query(CONTENT_URI,null,null,null,null);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            callbackWeakReference.get().preExcute();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            callbackWeakReference.get().postExcute(cursor);
        }
    }

    public  static  class DataObserver extends ContentObserver{
         Context context;
        public DataObserver(Handler handler,Context context) {
            super(handler);
            this.context=context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadPersonAsync(context,(LoadCallback) context).execute();
        }
    }

    private void showSnackbarMessage(String message) {
        Snackbar.make(rvPerson, message, Snackbar.LENGTH_SHORT).show();
    }
    @Override
    public void showDetail(Person person) {
        Intent intent = new Intent(this,UpdateAddActivity.class);
        intent.putExtra(UpdateAddActivity.EXTRA_DATA,person);
        intent.setData(Uri.parse(CONTENT_URI+"/"+person.getId()));
        startActivity(intent);
    }
}
