package com.example.countriesdata.sqlite_access;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.countriesdata.model.Country;
import com.example.countriesdata.model.Currency;
import com.example.countriesdata.model.Language;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static android.os.Environment.getExternalStorageDirectory;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "countries.db";
    private static final int DB_VERSION = 1; //required for the constructor
    private static final String TABLE_NAME = "countries";

    private String fileName = "countries.json";
    private String appName = "countriesData";
    private String path = getExternalStorageDirectory() + "/" + appName + "/" + fileName;
    private String jsonString = null;

    private Context context;

    SQLiteHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
        SQLiteDatabase db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d(TAG, "At SQLiteHelper");

        createSQLIteTable(db);
        try {
            parseJsonAndInsertToSQLIte(db);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean tableIsEmpty(SQLiteDatabase db) {

        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);
        cur.moveToFirst();
        int count = cur.getInt(0);

        if (count > 0) {

            Log.i(TAG, "The table is not empty");
            cur.close();
            return false;

        } else {

            Log.i(TAG, "The table is empty");
            cur.close();
            return true;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    private void createSQLIteTable(SQLiteDatabase db) {

        //creating a table for SQLite
        String CREATE_SQL_TABLE_STRING = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME
                + " ("
                + Country.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + Country.NAME + " TEXT,"
                + Country.CAPITAL + " TEXT,"
                + Country.REGION + " TEXT,"
                + Country.SUBREGION + " TEXT,"
                + Country.POPULATION + " TEXT,"
                + Country.LATLNG + " TEXT,"
                + Country.AREA + " TEXT,"
                + Country.GINI + " TEXT,"
                + Country.TIME_ZONES + " TEXT,"
                + Country.FLAG + " TEXT "
                + ")";

        Log.i(TAG, "created sql table: " + CREATE_SQL_TABLE_STRING);

        db.execSQL(CREATE_SQL_TABLE_STRING);

        String CREATE_SQL_TABLE_STRING1 = "CREATE TABLE IF NOT EXISTS " + "currencies"
                + " ("
                + Currency.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + Currency.NAME + " TEXT,"
                + "Country_ID" + " TEXT,"
                + Currency.SYMBOL + " TEXT "
                + ")";

        db.execSQL(CREATE_SQL_TABLE_STRING1);
        Log.i(TAG, "created sql table: " + CREATE_SQL_TABLE_STRING1);

        String CREATE_SQL_TABLE_STRING2 = "CREATE TABLE IF NOT EXISTS " + "languages"
                + " ("
                + Language.ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ,"
                + Language.NAME + " TEXT, "
                + "Country_ID" + " TEXT,"
                + Language.NATIVANAME + " TEXT "
                + ")";

        db.execSQL(CREATE_SQL_TABLE_STRING2);
        Log.i(TAG, "created sql table: " + CREATE_SQL_TABLE_STRING2);
    }

    private void parseJsonAndInsertToSQLIte(SQLiteDatabase db) throws JSONException {
        // parsing the json

        if (tableIsEmpty(db)) {
            jsonString = getJsonFileData();
            JSONArray countriesArray = new JSONArray(jsonString);

            ContentValues insert = new ContentValues();

            int counter = 0;

            for (int i = 0; i < countriesArray.length(); i++) {
                counter++;
                JSONObject countryObject = countriesArray.getJSONObject(i);
                String name = countryObject.getString("name");
                String capital = countryObject.getString("capital");
                String region = countryObject.getString("region");
                String subregion = countryObject.getString("subregion");
                int population = countryObject.getInt("population");

                JSONArray latlngArray = countryObject.getJSONArray("latlng");
                List<String> latlng = new ArrayList<>();
                for (int k = 0; k < latlngArray.length(); k++) {
                    latlng.add(latlngArray.getString(k));
                }

                double area;
                if(countryObject.isNull("area")){
                    area = countryObject.optDouble("area");
                }
                else{
                    area = countryObject.getDouble("area");
                }
                if(countryObject.isNull("gini")){
                    String gini = "0.0";
                    insert.put(Country.GINI, gini);
                }
                else{
                    double gini = countryObject.getDouble("gini");
                    insert.put(Country.GINI, gini);
                }

                JSONArray timeZonesArray = countryObject.getJSONArray("timezones");
                List<String> timeZones = new ArrayList<>();
                for (int k = 0; k < timeZonesArray.length(); k++) {
                    timeZones.add(timeZonesArray.getString(k));
                }

                ContentValues insert1 = new ContentValues();

                JSONArray currenciesArray = countryObject.getJSONArray("currencies");
                for (int k = 0; k < currenciesArray.length(); k++) {
                    JSONObject currencyObject = currenciesArray.getJSONObject(k);

                    String currencyName;
                    if(currencyObject.isNull("name")){
                        currencyName = currencyObject.optString("name");
                    }
                    else {
                        currencyName = currencyObject.getString("name");
                    }

                    String symbol;
                    if(currencyObject.isNull("symbol")){
                        symbol = "N/A";
                    }
                    else{
                        symbol = currencyObject.getString("symbol");
                    }

                    // inserting the country's ID in order to query it later
                    insert1.put("Country_ID",counter);
                    insert1.put(Currency.NAME, currencyName);

                    insert1.put(Currency.SYMBOL, symbol);
                    System.out.println("time zones with comma: "+symbol);

                    long res1 = db.insert("currencies", null, insert1);
                }

                ContentValues insert2 = new ContentValues();

                JSONArray languagesArray = countryObject.getJSONArray("languages");
                for (int k = 0; k < languagesArray.length(); k++) {
                    JSONObject languagesObject = languagesArray.getJSONObject(k);
                    insert2.put(Language.NAME, languagesObject.getString("name"));
                    insert2.put("Country_ID",counter);
                    insert2.put(Language.NATIVANAME, languagesObject.getString("nativeName"));
                    long res2 = db.insert("languages", null, insert2);
                }

                String flag = countryObject.getString("flag");
// inserting to SQLite
                insert.put(Country.NAME, name);
                insert.put(Country.CAPITAL, capital);
                insert.put(Country.REGION, region);
                insert.put(Country.SUBREGION, subregion);

                DecimalFormat format = new DecimalFormat("#,###");
                insert.put(Country.POPULATION, format.format(population));

                insert.put(Country.AREA, format.format(area));

                StringBuilder latLngStringBuilder = new StringBuilder();
                for (int k = 0; k < latlngArray.length(); k++) {
                    if (k > 0) {
                        latLngStringBuilder.append(" , ");
                    }
                    latLngStringBuilder.append(latlng.get(k));
                }

                insert.put(Country.LATLNG, latLngStringBuilder.toString());

                StringBuilder timeZonesStringBuilder = new StringBuilder();
                for (int k = 0; k < timeZonesArray.length(); k++) {
                    if (k > 0) {
                        timeZonesStringBuilder.append(" \n");
                    }
                    timeZonesStringBuilder.append(timeZones.get(k));
                }

                insert.put(Country.TIME_ZONES, timeZonesStringBuilder.toString());
                insert.put(Country.FLAG, flag);

                long res = db.insert(TABLE_NAME, null, insert);
            }
        }
    }

    private String getJsonFileData() {
        //loading the jsonString
        try {
            InputStream in = new FileInputStream(new File(path));
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder output = new StringBuilder();
            while ((jsonString = reader.readLine()) != null) {
                output.append(jsonString);
            }

            in.close();
            jsonString = output.toString();
            Log.d(ContentValues.TAG, "the jsonString was loaded");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return jsonString;
    }
}