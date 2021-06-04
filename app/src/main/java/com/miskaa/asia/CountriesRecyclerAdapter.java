package com.miskaa.asia;

import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextLinks;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.request.RequestOptions;
import com.caverock.androidsvg.SVG;
import com.miskaa.asia.database.Country;
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.util.List;

public class CountriesRecyclerAdapter extends RecyclerView.Adapter<CountriesRecyclerAdapter.ViewHolder> {

    List<Country> countryList;
    Context context;

    public CountriesRecyclerAdapter(List<Country> countryList, Context context) {
        this.countryList = countryList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.countries_recycler_single_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Country model = countryList.get(position);

        GlideApp.with(context).load(model.getFlag())
                .apply(RequestOptions.centerCropTransform())
                .into(holder.imgFlag);

        if (model.getBorders().isEmpty()){
            holder.borders.setVisibility(View.GONE);
        }else {
            holder.borders.setVisibility(View.VISIBLE);
            holder.borders.setText("Borders: " + model.getBorders());
        }

        holder.name.setText(model.getName());
        holder.capital.setText("Capital: " + model.getCapital());
        holder.region.setText("Region: " + model.getRegion());
        holder.subregion.setText("Subregion: " + model.getSubregion());
        holder.population.setText("Population: " + model.getPopulation());

        holder.languages.setText("Languages: " + model.getLanguages());

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgFlag;
        TextView name, capital, region, subregion, population, borders, languages;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.txtCountryName);
            capital = itemView.findViewById(R.id.txtCapital);
            region = itemView.findViewById(R.id.txtRegion);
            subregion = itemView.findViewById(R.id.txtSubRegion);
            population = itemView.findViewById(R.id.txtPopulation);
            borders = itemView.findViewById(R.id.txtBorders);
            languages = itemView.findViewById(R.id.txtLang);
            imgFlag = itemView.findViewById(R.id.imgFlag);

        }
    }
}
