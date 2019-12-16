package com.example.countriesdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.countriesdata.model.Language;

import java.util.ArrayList;
import java.util.List;

public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.ViewHolder> {

    Context context;
    private List<Language> languagesList;

    public LanguagesAdapter(Context context) {
        this.context = context;
        languagesList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_list_languages, parent,false);
        return new ViewHolder(itemView);
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView languageNameView, nativeNameView;

        ViewHolder(View view){
            super(view);
            languageNameView = view.findViewById(R.id.languageNameId);
            nativeNameView = view.findViewById(R.id.nativeNameId);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.languageNameView.setText(languagesList.get(position).getName());
        CharSequence sequence = "("+languagesList.get(position).getNativeName()+")";
        holder.nativeNameView.setText(sequence);
    }

    @Override
    public int getItemCount() {
        return languagesList.size();
    }

    public void attachCountriesList(List<Language> languages) {
        this.languagesList = languages;
    }
}
