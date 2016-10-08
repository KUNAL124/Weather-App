package com.example.naruto.weatherapp.Main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naruto.weatherapp.APIS.Location_details;
import com.example.naruto.weatherapp.Extras.FetchCurrentLocation;
import com.example.naruto.weatherapp.Extras.GetLocality;
import com.example.naruto.weatherapp.Fragment.Current_weather;
import com.example.naruto.weatherapp.NetStateChecker.NetAvailability;
import com.example.naruto.weatherapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CurrentLocationPage extends AppCompatActivity {

    TextView textview;
    private static final String Pref_file = "PREF_FILE";

    boolean firstrun = false;
    private boolean isGpsEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_location_page);
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // getting GPS status
        isGpsEnabled = locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        SharedPreferences firstprefs = PreferenceManager.getDefaultSharedPreferences(this);
        firstrun = firstprefs.getBoolean("first_run", true);
        //app is running for the first time
        //so fetch the current location and store it in the sharedprefrences
        if (NetAvailability.isNetworkAvailable(this) && isGpsEnabled) {
            if (firstrun) {
                FetchCurrentLocation fetchCurrentLocation = new FetchCurrentLocation(this);
                SharedPreferences preferences = getSharedPreferences(Pref_file, MODE_PRIVATE);
                SharedPreferences.Editor editor;
                editor = preferences.edit();
                //current location storing in shared preferences
                editor.putFloat(getString(R.string.Current_latitude), (float) fetchCurrentLocation.getLatitude());
                editor.putFloat(getString(R.string.Current_longitude), (float) fetchCurrentLocation.getLongitude());
                //at first run the seleected location will be equal to the current device location
                editor.putFloat(getString(R.string.Current_selected_latitude), (float) fetchCurrentLocation.getLatitude());
                editor.putFloat(getString(R.string.Current_selected_longitude), (float) fetchCurrentLocation.getLongitude());
                //get the details of location by get locality class
                GetLocality getLocality = new GetLocality(this, fetchCurrentLocation.getLatitude(), fetchCurrentLocation.getLongitude());
                ArrayList<Location_details> locations = new ArrayList<>();
                Location_details location_new = new Location_details();
                //set the data of location details
                location_new.setLoc_name(getLocality.getArearname());
                location_new.setLoc_state_name(getLocality.getLocality());
                location_new.setLoc_country_name(getLocality.getCountryname());
                location_new.setLoc_latitude(fetchCurrentLocation.getLatitude());
                location_new.setLoc_longitude(fetchCurrentLocation.getLongitude());
                locations.add(location_new);
                //save to shared preferences
                Gson gson = new Gson();
                String json = gson.toJson(locations);
                editor.putString("Location_Array_list", json);
                //apply
                editor.apply();
                SharedPreferences.Editor editorpref = firstprefs.edit();
                editorpref.putBoolean("first_run", false);
                editorpref.apply();
                launchFragment();
            } else {
                //After the first run
                //check that the last current location is same or not to the current device location
                //check the state(locality) by geocoder for both the conditions
                //make two objects of getLocality class with diffrent values of the latitude and longitude
                //fetch the latitude and longitude of the current location
                FetchCurrentLocation fetchCurrentLocation = new FetchCurrentLocation(this);
                GetLocality obj1 = new GetLocality(this, fetchCurrentLocation.getLatitude(), fetchCurrentLocation.getLongitude());
                //get the latitude and longitude of the current location stored in shared prefrences
                SharedPreferences prefs = getSharedPreferences(Pref_file, MODE_PRIVATE);
                float lat = prefs.getFloat(getString(R.string.Current_latitude), (float) 0.0);
                float lan = prefs.getFloat(getString(R.string.Current_longitude), (float) 0.0);
                Log.v("lat:", "" + lat);
                Log.v("log", "" + lan);
                GetLocality obj2 = new GetLocality(this, lat, lan);
                Log.v("obj2", "" + obj2.getLocality());
                //if last known location do not match with the current location
                if (obj1.getLocality() != null && obj2.getLocality() != null) {
                    if (!(obj1.getLocality().matches(obj2.getLocality()))) {
                        //change the value of the current location and the current selected location
                        prefs.edit().putFloat(getString(R.string.Current_latitude), (float) fetchCurrentLocation.getLatitude());
                        prefs.edit().putFloat(getString(R.string.Current_longitude), (float) fetchCurrentLocation.getLongitude());
                        prefs.edit().putFloat(getString(R.string.Current_selected_latitude), (float) fetchCurrentLocation.getLatitude());
                        prefs.edit().putFloat(getString(R.string.Current_selected_longitude), (float) fetchCurrentLocation.getLongitude());
                        //storing new location in the location list
                        Location_details location_new = new Location_details();
                        location_new.setLoc_latitude(fetchCurrentLocation.getLatitude());
                        location_new.setLoc_longitude(fetchCurrentLocation.getLongitude());
                        //fetching data about the location and storing
                        location_new.setLoc_name(obj1.getArearname());
                        location_new.setLoc_state_name(obj1.getLocality());
                        location_new.setLoc_country_name(obj1.getCountryname());
                        //retriving the array list of locations and saving new object in it
                        Gson gson = new Gson();
                        String json = prefs.getString("Location_Array_list", null);
                        Type type = new TypeToken<ArrayList<Location_details>>() {
                        }.getType();
                        ArrayList<Location_details> arrayList = gson.fromJson(json, type);
                        arrayList.add(0, location_new);
                        //storing back the array list in shared preferences
                        String jsonstoring = gson.toJson(arrayList);
                        prefs.edit().putString("Location_Array_list", jsonstoring).apply();
                        //apply
                        prefs.edit().apply();
                        //create a dialogue to tell the user that the current location and selected location is changed
                        display_dialogue(this);
                    }
                    launchFragment();
                } else {
                    //unable to get the locality internet connection faliure
                    //creating alert dialog by builder class
                    alert_dialog("Unable to get the location check connection!!!");
                }
            }
        } else {
            //creating alert dialog by builder class
            if (firstrun) {
                //first run stop app
                alert_dialog("No Internet Connection!!Check the GPS or NET connection and try again.No previous data to show");
            } else {
                Toast.makeText(getBaseContext(), "No Internet Connection!!Check the GPS or NET connection and try again", Toast.LENGTH_LONG).show();
                Toast.makeText(getBaseContext(), "Loading Previous Data", Toast.LENGTH_LONG).show();
                launchFragment();
            }
        }
    }


    private void display_dialogue(CurrentLocationPage currentLocationPage) {
        Dialog dialog = new Dialog(currentLocationPage);
        dialog.setContentView(R.layout.current_location_changed_dialog);
        dialog.setTitle("Location changed");
        dialog.show();
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }

    public void alert_dialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Check the network", Toast.LENGTH_SHORT).show();
                //finish the activity if no network connection
                finish();
            }
        });
        //set the message
        builder.setMessage("" + message);
        //create the alert dialog
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
        alertdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
    }

    private void launchFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.Current_weather_activity, new Current_weather()).commit();
    }
}

