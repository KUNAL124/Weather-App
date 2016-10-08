package com.example.naruto.weatherapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naruto.weatherapp.APIS.Location_details;
import com.example.naruto.weatherapp.Main.Location_details_activity;
import com.example.naruto.weatherapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Naruto on 7/9/2016.
 */
public class Locatio_List_Adapter extends RecyclerView.Adapter<Locatio_List_Adapter.Location_Item> {
    Context mcontext;
    private static final String Pref_file = "PREF_FILE";
    SharedPreferences prefs;
    LayoutInflater layoutInflater;
    private int selected_position;
    private ArrayList<Location_details> location = new ArrayList<>();
    public Locatio_List_Adapter(Context mcontext) {
        layoutInflater = LayoutInflater.from(mcontext);
        this.mcontext = mcontext;
        prefs = mcontext.getSharedPreferences(Pref_file, Context.MODE_PRIVATE);
        selected_position=prefs.getInt("Selected_position",0);
    }

    public void setlocationList(ArrayList<Location_details> location_detailses) {
        location = location_detailses;
        notifyItemRangeChanged(0, location.size());
    }

    @Override
    public Location_Item onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.location_list_row, parent, false);
        Location_Item location_item = new Location_Item(view, new Location_Item.Click_handler() {
            @Override
            public void show_details(View v, int adapterPosition) {
                //start the location details activity
                Location_details location_details=location.get(adapterPosition);
                mcontext.startActivity(new Intent(mcontext,Location_details_activity.class).putExtra("Location_details",location_details));
            }
        });
        return location_item;
    }

    @Override
    public void onBindViewHolder(Location_Item holder, final int position) {
        final Location_details location_details = location.get(position);

        if (location_details.getLoc_name() != null)
            holder.Location_name.setText(location_details.getLoc_name());
        else {
            if (location_details.getLoc_state_name() != null)
                holder.Location_name.setText(location_details.getLoc_state_name());
            else
                holder.Location_name.setText(location_details.getLoc_country_name());

        }
        selected_position=prefs.getInt("Selected_position",0);
        if(position==selected_position){
            holder.choose.setChecked(true);
        }else{
            holder.choose.setChecked(false);
        }
        //change the background of card view for item selected
        if(position==selected_position){
            holder.row_card.setCardBackgroundColor(mcontext.getResources().getColor(R.color.white));
        }else{
            holder.row_card.setCardBackgroundColor(mcontext.getResources().getColor(R.color.card_view));
        }
        //on delete clicked
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position != 0) {
                    prefs.edit().putInt("Selected_position",0).apply();
                    Location_details location_details=location.get(0);
                    prefs.edit().putFloat(mcontext.getString(R.string.Current_selected_latitude), (float) location_details.getLoc_latitude()).apply();
                    prefs.edit().putFloat(mcontext.getString(R.string.Current_selected_longitude), (float) location_details.getLoc_longitude()).apply();
                    location.remove(position);
                    notifyItemRemoved(position);
                    //delete the data from arraylist
                    Gson gson = new Gson();
                    String json = prefs.getString("Location_Array_list", null);
                    Log.v("jsonString", "" + json);
                    Type type = new TypeToken<ArrayList<Location_details>>() {
                    }.getType();
                    ArrayList<Location_details> arrayList = gson.fromJson(json, type);
                    arrayList.remove(position);
                    //saving new arraylist
                    String jsonstoring = gson.toJson(arrayList);
                    prefs.edit().putString("Location_Array_list", jsonstoring).apply();
                    notifyItemRangeChanged(position, location.size());
                } else {
                    Toast.makeText(mcontext, "Cannot Delete the current location data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //on choosed
        holder.choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.edit().putInt("Selected_position",position).apply();
                prefs.edit().putFloat(mcontext.getString(R.string.Current_selected_latitude), (float) location_details.getLoc_latitude()).apply();
                prefs.edit().putFloat(mcontext.getString(R.string.Current_selected_longitude), (float) location_details.getLoc_longitude()).apply();
                notifyDataSetChanged();
            }

        });
    }


    @Override
    public int getItemCount() {
        return location.size();
    }

    static class Location_Item extends RecyclerView.ViewHolder implements View.OnClickListener {

        //defining all the widgets
        private TextView Location_name;
        private RelativeLayout relative_layout;
        private RadioButton choose;
        private ImageView delete;
        private Click_handler myclickhandler;
        private CardView row_card;
        public Location_Item(View itemView, Click_handler click_handler) {
            super(itemView);
            relative_layout = (RelativeLayout) itemView.findViewById(R.id.relative_layout_row);
            Location_name = (TextView) itemView.findViewById(R.id.Location_name);
            choose = (RadioButton) itemView.findViewById(R.id.choose);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            row_card=(CardView)itemView.findViewById(R.id.row_card);
            myclickhandler = click_handler;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myclickhandler.show_details(v, getAdapterPosition());
        }

        interface Click_handler {
            public void show_details(View v, int adapterPosition);

        }
    }

}
