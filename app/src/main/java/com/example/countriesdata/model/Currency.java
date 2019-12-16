package com.example.countriesdata.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Currency implements Parcelable {

    public static final String CURRENCIES = "currencies";
    public static final String NAME = "name";
    public static final String SYMBOL = "symbol";
    public static final String ID = "id";
    private String name;
    private String symbol;

    public Currency(Parcel in) {
        name = in.readString();
        symbol = in.readString();
    }

    public static final Creator<Currency> CREATOR = new Creator<Currency>() {
        @Override
        public Currency createFromParcel(Parcel in) {
            return new Currency(in);
        }

        @Override
        public Currency[] newArray(int size) {
            return new Currency[size];
        }
    };

    public Currency() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "Currency{" +
                "name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(symbol);
    }
}