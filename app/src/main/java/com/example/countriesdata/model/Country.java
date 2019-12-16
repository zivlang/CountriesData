package com.example.countriesdata.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
import java.util.List;

public class Country implements Parcelable {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CAPITAL = "capital";
    public static final String REGION = "region";
    public static final String SUBREGION = "subregion";
    public static final String LATLNG = "latlng";
    public static final String AREA = "area";
    public static final String GINI = "gini";
    public static final String TIME_ZONES = "timezones";
    public static final String FLAG = "flag";
    public static final String POPULATION = "population";

    int id;
    private String name;
    private String capital;
    private String region;
    private String subregion;
    private String population;
    private List latlng;
    private String area;
    private double gini;
    private List timezones;
    private String flag;

    public Country(int id, String name, String capital, String region,
                   String subregion, String population, List<String> latlng,
                   String area, double gini, List<String> timezones,
                   String flag) {
        this.id = id;
        this.name = name;
        this.capital = capital;
        this.region = region;
        this.subregion = subregion;
        this.population = population;
        this.latlng = latlng;
        this.area = area;
        this.gini = gini;
        this.timezones = timezones;
        this.flag = flag;
    }

    public Country() {}

    protected Country(Parcel in) {
        id = in.readInt();
        name = in.readString();
        capital = in.readString();
        region = in.readString();
        subregion = in.readString();
        population = in.readString();
        latlng = Collections.singletonList(in.readString());
        area = in.readString();
        gini = in.readDouble();
        timezones = Collections.singletonList(in.readString());
        flag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(capital);
        dest.writeString(region);
        dest.writeString(subregion);
        dest.writeString(population);
        dest.writeString(String.valueOf(latlng));
        dest.writeString(area);
        dest.writeDouble(gini);
        dest.writeString(String.valueOf(timezones));
        dest.writeString(flag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Country> CREATOR = new Creator<Country>() {
        @Override
        public Country createFromParcel(Parcel in) {
            return new Country(in);
        }

        @Override
        public Country[] newArray(int size) {
            return new Country[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getSubregion() {
        return subregion;
    }

    public void setSubregion(String subregion) {
        this.subregion = subregion;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public List<String> getLatlng() {
        return latlng;
    }

    public void setLatlng(List<String> latlng) {
        this.latlng = Collections.singletonList(latlng);
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public double getGini() {
        return gini;
    }

    public void setGini(double gini) {
        this.gini = gini;
    }

    public List<String> getTimezones() {
        return timezones;
    }

    public void setTimezones(List<String> timezones) {
        this.timezones = Collections.singletonList(timezones);
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                "name='" + name + '\'' +
                ", capital='" + capital + '\'' +
                ", region='" + region + '\'' +
                ", subregion='" + subregion + '\'' +
                ", population=" + population +
                ", latlng=" + latlng +
                ", area=" + area +
                ", gini=" + gini +
                ", timezones=" + timezones +
                ", flag='" + flag + '\'' +
                '}';
    }
}