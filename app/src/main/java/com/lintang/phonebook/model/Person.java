package com.lintang.phonebook.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Person implements Parcelable {
   private String email;
   private String phone;
   private String name;
   private int id;

    public Person() {
    }

    public Person(String email, String phone, String name, int id) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.email);
        dest.writeString(this.phone);
        dest.writeString(this.name);
        dest.writeInt(this.id);
    }

    protected Person(Parcel in) {
        this.email = in.readString();
        this.phone = in.readString();
        this.name = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
