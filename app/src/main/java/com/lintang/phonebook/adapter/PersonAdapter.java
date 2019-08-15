package com.lintang.phonebook.adapter;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lintang.phonebook.DetailInterface;
import com.lintang.phonebook.R;
import com.lintang.phonebook.model.Person;

import java.util.ArrayList;

public class PersonAdapter  extends RecyclerView.Adapter<PersonAdapter.PersonViewHolder> {

   private ArrayList<Person> persons= new ArrayList<>();
   private Resources resources;
   private DetailInterface detailInterface;


    public PersonAdapter( Resources resources, DetailInterface detailInterface) {
        this.resources = resources;
        this.detailInterface = detailInterface;
    }

    @NonNull
    @Override
    public PersonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout,parent,false);
        return new PersonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PersonViewHolder holder, final int position) {
           holder.bind(persons.get(position));
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   detailInterface.showDetail(persons.get(position));
               }
           });
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    public void setData(ArrayList<Person> persons) {
        this.persons.clear();
        this.persons.addAll(persons);
        notifyDataSetChanged();
    }

    public  class PersonViewHolder extends RecyclerView.ViewHolder{
       TextView emailText;
       TextView nameText;
       TextView phoneText;
        public PersonViewHolder(@NonNull View itemView) {
            super(itemView);
            emailText=itemView.findViewById(R.id.email);
            nameText=itemView.findViewById(R.id.name);
            phoneText=itemView.findViewById(R.id.phone);
        }

        public void bind(Person person){
            emailText.setText(resources.getString(R.string.email,person.getEmail()));
            nameText.setText(resources.getString(R.string.name,person.getName()));
            phoneText.setText(resources.getString(R.string.phone,person.getPhone()));
        }
    }

    public ArrayList<Person> getPersons() {
        return persons;
    }
}
