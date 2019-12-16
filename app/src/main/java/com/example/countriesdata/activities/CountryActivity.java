package com.example.countriesdata.activities;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.example.countriesdata.CurrenciesAdapter;
import com.example.countriesdata.LanguagesAdapter;
import com.example.countriesdata.R;
import com.example.countriesdata.model.Country;
import com.example.countriesdata.model.Currency;
import com.example.countriesdata.model.Language;
import com.example.countriesdata.sqlite_access.GetDatabase;

import java.util.ArrayList;
import java.util.List;

public class CountryActivity extends AppCompatActivity {

    Country currentCountry;

    String doubleToString, noDataString = "No data";

    Context context;

    RecyclerView languagesRV, currenciesRV;
    LanguagesAdapter languagesAdapter;
    CurrenciesAdapter currenciesAdapter;

    ArrayList <Currency> currenciesList;
    ArrayList<Language> languagesList;

    TextView nameView, capitalView, populationView, regionView, subRegionView;
    TextView areaView, giniView, latlngView, timeZonesView;

    ImageView flagView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_country);

        context = getApplicationContext();


        Bundle countryBundle = getIntent().getExtras();

        if (countryBundle != null) {
            currentCountry = countryBundle.getParcelable("currentCountry");
            languagesList = countryBundle.getParcelableArrayList("languagesList");
            currenciesList = countryBundle.getParcelableArrayList("currenciesList");
        }
        if (currentCountry != null) {
            nameView = findViewById(R.id.countryNameId);
            flagView = findViewById(R.id.flagId);
            capitalView = findViewById(R.id.capitalId);
            populationView = findViewById(R.id.populationId);
            regionView = findViewById(R.id.regionId);
            subRegionView = findViewById(R.id.subregionId);
            areaView = findViewById(R.id.areaId);
            giniView = findViewById(R.id.giniId);
            latlngView = findViewById(R.id.latlngId);
            timeZonesView = findViewById(R.id.timeZonesId);

            nameView.setText(currentCountry.getName());
            Uri imageUri = Uri.parse(String.valueOf(currentCountry.getFlag()));
            SvgLoader.pluck()
                    .with(this)
                    .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                    .load(imageUri, flagView);
            if(currentCountry.getCapital().equals("")){
                capitalView.setText(noDataString);
            }
            else {
                capitalView.setText(currentCountry.getCapital());
            }
            if(currentCountry.getPopulation().equals("0")){
                populationView.setText(noDataString);
            }
            else {
                populationView.setText((currentCountry.getPopulation()));
            }
            if(currentCountry.getRegion().equals("")){
                regionView.setText(noDataString);
            }
            else {
                regionView.setText(currentCountry.getRegion());
            }
            if(currentCountry.getSubregion().equals("")){
                subRegionView.setText(noDataString);
            }
            else {
                subRegionView.setText(currentCountry.getSubregion());
            }
            if(currentCountry.getArea().equals("0")){
                areaView.setText(noDataString);
            }
            else {
                doubleToString = currentCountry.getArea();
                areaView.setText(doubleToString);
            }
            if(currentCountry.getGini() == 0.0){
                giniView.setText(noDataString);
            }
            else {
                doubleToString = Double.toString(currentCountry.getGini());
                giniView.setText(doubleToString);
            }
        }

        if (currentCountry != null) {
            GetDatabase getDatabase = new GetDatabase(this);
            // getting the current cursor in order to get the current latlng
            getDatabase.open();
            Cursor cursor = getDatabase.getData(currentCountry.getId());

            String latlng = null;

            if(cursor.moveToFirst()) {
                latlng = cursor.getString(cursor.getColumnIndex("latlng"));
            }
            getDatabase.close();
            latlngView.setText(latlng);

            String timeZones = null;

            if(cursor.moveToFirst()) {
                timeZones = cursor.getString(cursor.getColumnIndex("timezones"));
//                    genres.add(genreFromSQLite);
            }
            getDatabase.close();
            timeZonesView.setText(timeZones);
        }

        // language adapter
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        languagesAdapter = new LanguagesAdapter(context);

        languagesRV = findViewById(R.id.languagesListId);
        languagesRV.setLayoutManager(linearLayoutManager);
        languagesRV.setHasFixedSize(true);

//        DividerItemDecoration dividerItemDecoration =
//                new DividerItemDecoration(context, linearLayoutManager.getOrientation());
//
//        languagesRV.addItemDecoration(dividerItemDecoration);
        languagesRV.setAdapter(languagesAdapter);

        languagesAdapter.attachCountriesList(languagesList);

        //currency adapter
        LinearLayoutManager currenciesManager = new LinearLayoutManager(context);
        currenciesAdapter = new CurrenciesAdapter(context);

        currenciesRV = findViewById(R.id.currenciesListId);
        currenciesRV.setLayoutManager(currenciesManager);
        currenciesRV.setHasFixedSize(true);

//        DividerItemDecoration decoration =
//                new DividerItemDecoration(context, linearLayoutManager.getOrientation());

//        languagesRV.addItemDecoration(decoration);
//
//        currenciesRV.addItemDecoration(decoration);
        currenciesRV.setAdapter(currenciesAdapter);

        currenciesAdapter.attachCurrenciesList(currenciesList);
    }
}