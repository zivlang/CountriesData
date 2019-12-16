package com.example.countriesdata.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Language implements Parcelable {

    public static final String LANGUAGES = "languages";
    public static final String NAME = "name";
    public static final String NATIVANAME = "nativeName";
    public static final String ID = "id";
    private String name;
    private String nativeName;

    public Language(Parcel in) {
        name = in.readString();
        nativeName = in.readString();
    }

    public static final Creator<Language> CREATOR = new Creator<Language>() {
        @Override
        public Language createFromParcel(Parcel in) {
            return new Language(in);
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };

    public Language() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    @Override
    public String toString() {
        return "Language{" +
                "name='" + name + '\'' +
                ", nativeName='" + nativeName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(nativeName);
    }
}