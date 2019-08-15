package com.lintang.phonebook.activity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.lintang.phonebook.R;
import com.lintang.phonebook.model.Person;

import static com.lintang.phonebook.database.DatabaseContract.PersonCollumns.*;

public class UpdateAddActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_DATA = "extra_data";
    public static final int DIALOG_CANCEL = 10;
    public static final int DIALOG_REMOVE = 20;
    private boolean isEdit = false;
    private Person person;
    private int position;
    private Button button;
    private EditText emailText,phoneText,nameText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_add);


        person = getIntent().getParcelableExtra(EXTRA_DATA);
        button=findViewById(R.id.submit);
        emailText=findViewById(R.id.email_edit_text);
        phoneText=findViewById(R.id.phone_edit_text);
        nameText=findViewById(R.id.name_edit_text);
        button.setOnClickListener(this);

        if (person != null) {
            isEdit = true;
        } else {
            person = new Person();
        }

        String btnTitle;
        String barTitle;


        if(isEdit){
            btnTitle="Update";
            barTitle="Edit Contact";
            phoneText.setText(person.getPhone());
            nameText.setText(person.getName());
            emailText.setText(person.getEmail());
        }else{
            btnTitle="Submit";
            barTitle="Add Contact";
        }


         button.setText(btnTitle);
         if(getSupportActionBar()!=null){
             getSupportActionBar().setTitle(barTitle);
         }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {
            getMenuInflater().inflate(R.menu.menu_item, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.remove) {
            showDialogAlert(DIALOG_REMOVE);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        showDialogAlert(DIALOG_CANCEL);
    }

    public void showDialogAlert(int code) {
        final boolean isDialogClose = code == DIALOG_CANCEL;
        String dialogTitle, dialogMessage;
        if (isDialogClose) {
            dialogTitle = "Cancel";
            dialogMessage = "Do you want to cancel the changes to the form?";
        } else {
            dialogMessage = "Are you sure you want to delete this item?";
            dialogTitle = "Delete Contact";
        }

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isDialogClose) {
                            finish();
                        } else {
                            getContentResolver().delete(getIntent().getData(), null, null);
                            Toast.makeText(UpdateAddActivity.this, "One Item Has Been Deleted", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.submit:
                submit();
                break;
        }
    }


    public void submit(){

            boolean isEmpty=false;
            String email= emailText.getText().toString();
            String phone= phoneText.getText().toString();
            String name= nameText.getText().toString();

            if(TextUtils.isEmpty(email)){
                emailText.setError("CAN NOT BE EMPTY");
                isEmpty=true;
            }
            if(TextUtils.isEmpty(phone)){
                phoneText.setError("CAN NOT BE EMPTY");
                isEmpty=true;
            }
            if(TextUtils.isEmpty(name)){
                nameText.setError("CAN NOT BE EMPTY");
                isEmpty=true;
            }


        if(!isEmpty) {
            ContentValues contentValues=new ContentValues();
            contentValues.put(EMAIL,email);
            contentValues.put(PHONE,phone);
            contentValues.put(NAME,name);
             if (isEdit) {
                 getContentResolver().update(getIntent().getData(),contentValues,null,null);
                 Toast.makeText(UpdateAddActivity.this, "One Item Has Been Updated", Toast.LENGTH_SHORT).show();
                 finish();
             } else {
                 getContentResolver().insert(CONTENT_URI,contentValues);
                 Toast.makeText(UpdateAddActivity.this, "One Item Has Been Added", Toast.LENGTH_SHORT).show();
                 finish();
             }
         }
    }


}
