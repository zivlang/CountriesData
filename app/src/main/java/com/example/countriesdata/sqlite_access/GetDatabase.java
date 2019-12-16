package com.example.countriesdata.sqlite_access;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.countriesdata.model.Country;
import com.example.countriesdata.model.Currency;
import com.example.countriesdata.model.Language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.example.countriesdata.model.Country.LATLNG;

public class GetDatabase {

    private static final int DB_VERSION = 1; //required for the constructor
    private static final String dbName = "countriesList";

    private SQLiteOpenHelper sqLiteOpenHelper;
    private SQLiteDatabase db ;

    public GetDatabase(Context context) {
        Log.d("GetDatabase", "Cont.");

        this.sqLiteOpenHelper = new SQLiteHelper(context, dbName, null, DB_VERSION);
    }

    public void open() {
        db = sqLiteOpenHelper.getWritableDatabase();
    }

    public void close() {
        if (sqLiteOpenHelper != null) {
            sqLiteOpenHelper.close();
        }
    }

    public ArrayList<Country> getCountries() {
        String[] columns = {
                Country.ID,
                Country.NAME,
                Country.CAPITAL,
                Country.REGION,
                Country.SUBREGION,
                Country.POPULATION,
                Country.LATLNG,
                Country.AREA,
                Country.GINI,
                Country.TIME_ZONES,
                Country.FLAG
        };

        ArrayList<Country> countriesList = new ArrayList<>();

        Cursor cursor = db.query("countries", //Table to query
                columns,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            do {
                Country country = new Country();
                country.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Country.ID))));
                country.setName(cursor.getString(cursor.getColumnIndex("name")));
                country.setCapital(cursor.getString(cursor.getColumnIndex("capital")));
                country.setRegion(cursor.getString(cursor.getColumnIndex("region")));
                country.setSubregion(cursor.getString(cursor.getColumnIndex("subregion")));
                country.setPopulation(cursor.getString(cursor.getColumnIndex("population")));
                country.setLatlng(Collections.singletonList(String.valueOf(getLatLng(cursor))));
                country.setArea(cursor.getString(cursor.getColumnIndex("area")));
                country.setGini(cursor.getDouble(cursor.getColumnIndex("gini")));
                country.setTimezones(Collections.singletonList(String.valueOf(getTimeZones(cursor))));
                country.setFlag(cursor.getString(cursor.getColumnIndex(Country.FLAG)));

//                 Adding a country to the list
                countriesList.add(country);
                country.getTimezones();
                country.getLatlng();
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return countriesList;
    }

    public List<String> getTimeZones(Cursor cursor) {
        return Arrays.asList((cursor.getString(cursor.getColumnIndex(Country.TIME_ZONES))).split(",",0));
    }

    public List<String> getLatLng(Cursor cursor) {
        return Collections.singletonList((cursor.getString(cursor.getColumnIndex(LATLNG))));
    }

    public Cursor getData(int id) {
        return db.rawQuery( "select * from countries where id="+id+"", null );
    }

    public Cursor getCountryDetails(int countryId) {

        Cursor cursor = getData(countryId);

        cursor.moveToFirst();

        return cursor;
    }

    public ArrayList<Currency> getCurrencies(int id) {
        String[] columns = {
                Currency.NAME,
                Currency.SYMBOL
        };

        ArrayList<Currency> currenciesList = new ArrayList<>();

        Cursor cursor = db.query("currencies", //Table to query
                columns,
                "Country_ID="+id+"",
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            do {
                Currency currency = new Currency();
                currency.setName(cursor.getString(cursor.getColumnIndex("name")));
                currency.setSymbol(cursor.getString(cursor.getColumnIndex("symbol")));

//                 Adding a country to the list
                currenciesList.add(currency);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return currenciesList;
    }

    public ArrayList<Language> getLanguages(int id) {
        String[] columns = {
                Language.NAME,
                Language.NATIVANAME
        };

        ArrayList<Language> languagesList = new ArrayList<>();

        Cursor cursor = db.query("languages", //Table to query
                columns,
                "Country_ID="+id+"",
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {

            do {
                Language language = new Language();
                language.setName(cursor.getString(cursor.getColumnIndex("name")));
                language.setNativeName(cursor.getString(cursor.getColumnIndex("nativeName")));

//                 Adding a country to the list
                languagesList.add(language);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return languagesList;
    }
}