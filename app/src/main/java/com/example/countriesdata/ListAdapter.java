package com.example.countriesdata;

import android.content.Context;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.example.countriesdata.activities.ListActivity;
import com.example.countriesdata.model.Country;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.RowViewHolder> {

    private ItemClickListener mClickListener;
    FragmentManager fm;
    ListActivity listActivity;
    private Context context;
    private List<Country> countriesList;

    public ListAdapter(Context context, ListActivity listActivity) {

        this.fm = fm;
        this.listActivity = listActivity;
        this.context = context;
        countriesList = new ArrayList<>();
    }

    @NotNull
    @Override
    public RowViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.row_list, parent,false);

        return new RowViewHolder(itemView);
    }

    class RowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView viewName;
        ImageView viewFlag;

        LinearLayout countryEntry;

        RowViewHolder(View view) {

            super(view);
            viewName = view.findViewById(R.id.name);
            viewFlag = view.findViewById(R.id.flag);
            countryEntry = view.findViewById(R.id.rowId);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            final Country currentCountry = countriesList.get(getAdapterPosition());
            listActivity.setCurrentCountry(currentCountry);
            mClickListener.onItemClick(currentCountry);
        }
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(Country country);
    }

    @Override
    public void onBindViewHolder(@NotNull RowViewHolder holder, int position) {

        holder.viewName.setText(countriesList.get(position).getName());

        Uri imageUrl = Uri.parse(String.valueOf(countriesList.get(position).getFlag()));

        SvgLoader.pluck()
                .with(listActivity)
                .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                .load(imageUrl, holder.viewFlag);
    }

    @Override
    public int getItemCount() {
        return countriesList.size();
    }

    public void attachCountriesList(List<Country> countriesList) {
        this.countriesList = countriesList;
    }
}