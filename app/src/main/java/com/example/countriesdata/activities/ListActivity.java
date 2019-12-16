package com.example.countriesdata.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countriesdata.ListAdapter;
import com.example.countriesdata.R;
import com.example.countriesdata.model.Country;
import com.example.countriesdata.model.Currency;
import com.example.countriesdata.model.Language;
import com.example.countriesdata.sqlite_access.GetDatabase;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ListAdapter.ItemClickListener {

    Country currentCountry;
    GetDatabase getDatabase;

    Context context;
    ArrayList<Country> countriesList;
    ArrayList<Currency> currenciesList;
    ArrayList<Language> languagesList;
    private ArrayList<Country> latLngArray;

    ListAdapter adapter;
    RecyclerView rv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        context = getApplicationContext();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        adapter = new ListAdapter(context, this);

        adapter.setClickListener(this);
        rv = findViewById(R.id.rvId);
        rv.setLayoutManager(linearLayoutManager);
        rv.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(context, linearLayoutManager.getOrientation());

        rv.addItemDecoration(dividerItemDecoration);
        rv.setAdapter(adapter);

        Bundle listBundle = getIntent().getExtras();
        if (listBundle != null) {
            countriesList = listBundle.getParcelableArrayList("countriesList");
            adapter.attachCountriesList(countriesList);
        }
    }

    public void setCurrentCountry(Country currentCountry) {
        this.currentCountry = currentCountry;
        getDatabase = new GetDatabase(context);
        currenciesList = getCurrencies();
        languagesList = getLanguages();
    }

    private Country getCurrentCountry(){
        return currentCountry;
    }

    private ArrayList<Currency> getCurrencies() {
        getDatabase.open();
        currenciesList = getDatabase.getCurrencies(currentCountry.getId());
        return currenciesList;
    }

    private ArrayList<Language> getLanguages() {
        getDatabase.open();
        languagesList = getDatabase.getLanguages(currentCountry.getId());
        getDatabase.close();
        return languagesList;
    }

    @Override
    public void onItemClick(Country country) {
        Intent intent = new Intent(context, CountryActivity.class);
        Bundle countryBundle = new Bundle();
        countryBundle.putParcelable("currentCountry", getCurrentCountry());
        countryBundle.putParcelableArrayList("languagesList", getLanguages());
        countryBundle.putParcelableArrayList("currenciesList", getCurrencies());
        intent.putExtras(countryBundle);
        startActivity(intent);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }
}
