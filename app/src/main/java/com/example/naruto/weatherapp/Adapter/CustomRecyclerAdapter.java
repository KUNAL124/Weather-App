package com.example.naruto.weatherapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.naruto.weatherapp.APIS.Five3DayAeatherApi;
import com.example.naruto.weatherapp.Main.detail_1;
import com.example.naruto.weatherapp.R;

import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by Naruto on 7/6/2016.
 */
public class CustomRecyclerAdapter extends RecyclerView.Adapter<CustomRecyclerAdapter.ViewHolderWeatherForcast> {
    private LayoutInflater layoutInflater;
    private int pager;
    Context context;
    private ArrayList<Five3DayAeatherApi> weatherlistforcast = new ArrayList<Five3DayAeatherApi>();

    public CustomRecyclerAdapter(Context context, int pager) {
        layoutInflater = LayoutInflater.from(context);
        this.pager = pager;
        this.context = context;
    }

    public void setForcastList(ArrayList<Five3DayAeatherApi> five3DayAeatherApis) {
        weatherlistforcast = five3DayAeatherApis;
        notifyItemRangeChanged(0, weatherlistforcast.size());
    }

    @Override
    public ViewHolderWeatherForcast onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.forcast_list_row, parent,false);
        ViewHolderWeatherForcast viewHolderWeatherForcast = new ViewHolderWeatherForcast(view, new ViewHolderWeatherForcast.ViewHolderInterface() {
            @Override
            public void showDetails(View v, int position) {
                Intent i = new Intent(context, detail_1.class);
                String strName = null;
                i.putExtra("Details", weatherlistforcast.get(position));
                i.putExtra("Detail_specifier", pager);
                context.startActivity(i);
            }
        });
        return viewHolderWeatherForcast;
    }

    @Override
    public void onBindViewHolder(final ViewHolderWeatherForcast holder, int position) {
        Five3DayAeatherApi currentforcast = weatherlistforcast.get(position);
        holder.date.setText("" + currentforcast.getCurrent_date());
        holder.weather_image.setImageResource(currentforcast.getIcon());
        holder.weather_image.setColorFilter(Color.argb(255, 255, 255, 255));
        holder.min_temp.setText("" + round((float) currentforcast.getTemp_min(), 1) + (char) 0x00B0 + "C");
        holder.max_temp.setText("" + round((float) currentforcast.getTemp_max(), 1) + (char) 0x00B0 + "C");
        holder.description.setText("" + currentforcast.getDescription());
        holder.wind.setText("" + currentforcast.getWind_speed() + " m/s, " + currentforcast.getWind_direstion_degree() + "(" + currentforcast.getWind_direction_name() + ")");
        holder.clouds.setText("clouds: " + currentforcast.getClouds() + "%");
    }

    @Override
    public int getItemCount() {
        return weatherlistforcast.size();
    }

    static class ViewHolderWeatherForcast extends RecyclerView.ViewHolder implements View.OnClickListener {
        //define all the widgets ids
        private TextView date;
        private ImageView weather_image;
        private TextView max_temp;
        private TextView min_temp;
        private TextView description;
        private TextView wind;
        private TextView clouds;
        private ViewHolderInterface myclickhandler;

        public ViewHolderWeatherForcast(View itemView, ViewHolderInterface m) {
            super(itemView);
            date = (TextView) itemView.findViewById(R.id.date);
            weather_image = (ImageView) itemView.findViewById(R.id.weather_image);
            max_temp = (TextView) itemView.findViewById(R.id.max_temp);
            min_temp = (TextView) itemView.findViewById(R.id.min_temp);
            description = (TextView) itemView.findViewById(R.id.weather_description);
            wind = (TextView) itemView.findViewById(R.id.wind_speed_direction);
            clouds = (TextView) itemView.findViewById(R.id.clouds);
            myclickhandler = m;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myclickhandler.showDetails(v, getAdapterPosition());
        }

        interface ViewHolderInterface {
            public void showDetails(View v, int position);
        }
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

}

