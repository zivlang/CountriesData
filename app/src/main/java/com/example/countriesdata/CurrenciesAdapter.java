package com.example.countriesdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countriesdata.model.Currencies;
import com.example.countriesdata.model.Currency;
import com.example.countriesdata.model.Language;

import java.util.ArrayList;
import java.util.List;

public class CurrenciesAdapter extends RecyclerView.Adapter<CurrenciesAdapter.ViewHolder> {

    Context context;
    private List<Currency> currenciesList;

    public CurrenciesAdapter(Context context) {
        this.context = context;
        currenciesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_list_currencies, parent,false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView currencyNameView, symbolView;

        ViewHolder(View view){
            super(view);
            currencyNameView = view.findViewById(R.id.currencyNameId);
            symbolView = view.findViewById(R.id.symbolId);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.currencyNameView.setText(currenciesList.get(position).getName());
        CharSequence sequence = "("+currenciesList.get(position).getSymbol()+")";
        holder.symbolView.setText(sequence);
//        System.out.println("symbol: "+currenciesList.get(position).getSymbol());
    }

    @Override
    public int getItemCount() {
        return currenciesList.size();
    }

    public void attachCurrenciesList(List<Currency> currencies) {
        this.currenciesList = currencies;
    }
}