package com.example.naruto.weatherapp.Fragment;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naruto.weatherapp.APIS.CurrentWeatherAPI;
import com.example.naruto.weatherapp.APIS.Location_details;
import com.example.naruto.weatherapp.Adapter.Locatio_List_Adapter;
import com.example.naruto.weatherapp.Extras.GetTheImage;
import com.example.naruto.weatherapp.Main.Locatio_chooser;
import com.example.naruto.weatherapp.Main.More_details;
import com.example.naruto.weatherapp.NetStateChecker.NetAvailability;
import com.example.naruto.weatherapp.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.snappydb.DB;
import com.snappydb.DBFactory;
import com.snappydb.SnappydbException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Current_weather extends Fragment {

    private static final int RequestCode=901;
    private static final String Pref_file = "PREF_FILE";
    private final String Log_Tag = Current_weather.class.getSimpleName();
    private final String API_KEY = "91a278269e52fa60e42041b4fbdf6372";
    public static double latitude = 0;
    public static double longitude = 0;
    private DB snappydb;
    private View view;
    private View load;
    private Context context=this.getActivity();
    //delete test text;
    public Current_weather() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            snappydb= DBFactory.open(getContext(),"current_weather");
        } catch (SnappydbException e) {
            e.printStackTrace();
        }

        setHasOptionsMenu(true);
    }
    void LocationUpdate(){
        SharedPreferences preferences = this.getActivity().getSharedPreferences(getString(R.string.PREF_FILE), Context.MODE_PRIVATE);
        latitude = preferences.getFloat(getString(R.string.Current_selected_latitude), (float) 0.0);
        longitude = preferences.getFloat(getString(R.string.Current_selected_longitude), (float) 0.0);
        FetchCurrentWeather fetchCurrentWeather = new FetchCurrentWeather();
        fetchCurrentWeather.execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu, menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_current_weather, container, false);
        load=(View)view.findViewById(R.id.load);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        //set the tile to "Weatther guide"
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Weather Guide");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        if(NetAvailability.isNetworkAvailable(getContext()))
        LocationUpdate();
        else {
            getPreviousData();
        }
        return view;
    }

    private void getPreviousData() {
        try {
            CurrentWeatherAPI currentWeatherAPI=snappydb.getObject("current_weather_data",CurrentWeatherAPI.class);
            setData(currentWeatherAPI);
        } catch (SnappydbException e) {
            e.printStackTrace();
        }
    }

    void show_dialog(Current_weather current_weather){
        final Dialog settingsDialog = new Dialog(current_weather.getActivity());
        settingsDialog.getWindow().setBackgroundDrawableResource((R.color.Transparent));
        settingsDialog.setTitle("Locations");
        View view=((AppCompatActivity) getActivity()).getLayoutInflater().inflate(R.layout.location_list_view, null);
        RecyclerView location_list=(RecyclerView) view.findViewById(R.id.location_list);
        location_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        Locatio_List_Adapter listadapter = new Locatio_List_Adapter(getActivity());
        FloatingActionButton LocationAdding=(FloatingActionButton)view.findViewById(R.id.location_add);
        LocationAdding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), Locatio_chooser.class),RequestCode);
                settingsDialog.cancel();
            }
        });
        settingsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                LocationUpdate();
            }
        });
        location_list.setAdapter(listadapter);
        settingsDialog.setContentView(view);
        SharedPreferences prefs = this.getActivity().getSharedPreferences(Pref_file,Context.MODE_PRIVATE);
        //getting the array list from shared preferences
        Gson gson = new Gson();
        String json = prefs.getString("Location_Array_list", null);
        Type type = new TypeToken<ArrayList<Location_details>>() {}.getType();
        ArrayList<Location_details> arrayList = gson.fromJson(json,type);
        Log.v("jsonString",""+json);
        listadapter.setlocationList(arrayList);
        settingsDialog.show();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.location_settings) {
            show_dialog(this);
        }
          else {
                if (id == R.id.details_navigation) {
                    startActivity(new Intent(this.getActivity(), More_details.class));
            }else if(id==R.id.refresh_weather) {
                    if (NetAvailability.isNetworkAvailable(getContext()))
                        LocationUpdate();
                    else {
                        Toast.makeText(getContext(), "No Internet Connection!!Check the GPS or NET connection and try again", Toast.LENGTH_LONG).show();
                        Toast.makeText(getContext(), "Loading Previous Data", Toast.LENGTH_LONG).show();

                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private class FetchCurrentWeather extends AsyncTask<Void, Void, CurrentWeatherAPI> {
        @Override
        protected CurrentWeatherAPI doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            CurrentWeatherAPI currentWeatherAPI = new CurrentWeatherAPI();
            if (NetAvailability.isNetworkAvailable(getContext())) {

                try {
                    // Construct the URL for the OpenWeatherMap query
                    // Possible parameters are avaiable at OWM's forecast API page, at
                    // http://openweathermap.org/API#forecast
                    //Defining different parts of the url and building uri
                    //by uri builder class
                    //after uri creation it is converted to url
                    final String URL_BASE = "http://api.openweathermap.org/data/2.5/weather?";
                    final String query_param_lat = "lat";
                    final String query_param_lon = "lon";
                    final String appid = "appid";
                    Uri built_uri = Uri.parse(URL_BASE).buildUpon()
                            .appendQueryParameter(query_param_lat, String.valueOf(latitude))
                            .appendQueryParameter(query_param_lon, String.valueOf(longitude))
                            .appendQueryParameter(appid, API_KEY).build();
                    Log.v("lat123:", "" + built_uri.toString());
                    URL url = new URL(built_uri.toString());
                    // Create the request to OpenWeatherMap, and open the connection

                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    // Read the input stream into a String
                    InputStream inputStream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    if (inputStream == null) {
                        // Nothing to do.
                        return null;
                    }
                    reader = new BufferedReader(new InputStreamReader(inputStream));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                        // But it does make debugging a *lot* easier if you print out the completed
                        // buffer for debugging.
                        buffer.append(line + "\n");
                    }

                    if (buffer.length() == 0) {
                        // Stream was empty.  No point in parsing.
                        return null;
                    }
                    forecastJsonStr = buffer.toString();
                } catch (
                        IOException e
                        )

                {
                    Log.e(Log_Tag, "Error ", e);
                    // If the code didn't successfully get the weather data, there's no point in attemping
                    // to parse it.
                    forecastJsonStr = null;
                } finally

                {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (final IOException e) {
                            Log.e(Log_Tag, "Error closing stream", e);
                        }
                    }
                }
                //returning the data from getWeatherDataFromJson
                //here are the string for the the data to be fetched
                final String OWN_COUNTRY = "country";
                final String OWN_CITY_NAME = "name";
                final String OWN_COORD = "coord";
                final String OWN_LATITUDE = "lat";
                final String OWN_LONGITUDE = "lon";
                final String OWN_DATETIME = "dt";
                final String OWN_PRESSURE = "pressure";
                final String OWN_HUMIDITY = "humidity";
                final String OWN_SPEED = "speed";
                final String OWN_DEGREE = "deg";
                final String OWN_WIND = "wind";

                //all temperature are childer of temp object
                final String OWN_TEMPERATURE = "temp";
                final String OWN_MAX = "temp_max";
                final String OWN_MIN = "temp_min";

                final String OWN_WEATHER = "weather";
                final String OWN_MAIN = "main";
                final String OWN_DESCRIPTION = "description";
                final String OWN_WEATHER_ID = "id";

                final String OWN_SYS = "sys";
                final String OWN_SUNSET = "sunset";
                final String OWN_SUNRISE = "sunrise";

                try {
                    // These are the values that will be collected.
                    int weatherId;
                    long dateTime;
                    double temp;
                    double pressure;
                    int humidity;
                    double windSpeed;
                    double windDirection;
                    double lat;
                    double lan;
                    double temp_min;
                    double temp_max;
                    String main;
                    String description;
                    String country_code;
                    String name;
                    long sunrise;
                    long sunset;
                    long datetime;
                    //get the json object from the fetched string
                    JSONObject weatherobject = new JSONObject(forecastJsonStr);
                    //get the coordinate object
                    JSONObject coordinates = weatherobject.getJSONObject(OWN_COORD);
                    lat = coordinates.getDouble(OWN_LATITUDE);
                    lan = coordinates.getDouble(OWN_LONGITUDE);
                    //get the weather array
                    JSONArray weather = weatherobject.getJSONArray(OWN_WEATHER);
                    JSONObject weather_O = weather.getJSONObject(0);
                    main = weather_O.getString(OWN_MAIN);
                    description = weather_O.getString(OWN_DESCRIPTION);
                    weatherId = weather_O.getInt(OWN_WEATHER_ID);
                    JSONObject mainobject = weatherobject.getJSONObject(OWN_MAIN);
                    temp = mainobject.getDouble(OWN_TEMPERATURE);
                    pressure = mainobject.getDouble(OWN_PRESSURE);
                    humidity = (int) mainobject.getDouble(OWN_HUMIDITY);
                    JSONObject wind = weatherobject.getJSONObject(OWN_WIND);
                    windSpeed = wind.getDouble(OWN_SPEED);
                    windDirection = wind.getDouble(OWN_DEGREE);
                    long current = System.currentTimeMillis();
                    JSONObject sys = weatherobject.getJSONObject(OWN_SYS);
                    sunrise = (long) sys.getLong(OWN_SUNRISE);
                    sunset = (long) sys.getLong(OWN_SUNSET);
                    country_code = sys.getString(OWN_COUNTRY);
                    name = weatherobject.getString(OWN_CITY_NAME);
                    datetime = weatherobject.getLong(OWN_DATETIME);
                    String sunriseString = new SimpleDateFormat("MMM dd,yyyy  hh:mm a").format(new Date(sunrise * 1000));
                    String sunsetString = new SimpleDateFormat("MMM dd,yyyy  hh:mm a").format(new Date(sunset * 1000));
                    String dayTimeString = new SimpleDateFormat("MMM dd").format(new Date(datetime * 1000));
                    //calling setters;
                    currentWeatherAPI.setWeatherId(weatherId);
                    GetTheImage getTheImage = new GetTheImage(weatherId);
                    currentWeatherAPI.setSunrise(sunriseString);
                    currentWeatherAPI.setSunset(sunsetString);
                    currentWeatherAPI.setCurrent_date(dayTimeString);
                    currentWeatherAPI.setDescription(description);
                    currentWeatherAPI.setHumidity(humidity);
                    Log.v("image:", "" + getTheImage.getImageResource());
                    currentWeatherAPI.setIcon((int) getTheImage.getImageResource());
                    currentWeatherAPI.setLatitude(lat);
                    currentWeatherAPI.setLongitude(lan);
                    currentWeatherAPI.setMain(main);
                    currentWeatherAPI.setPressure((double) pressure);
                    currentWeatherAPI.setTemp((double) ((temp - 273)));
                    currentWeatherAPI.setWind_speed((double) windSpeed);
                    currentWeatherAPI.setWind_direstion_degree((double) windDirection);
                    //setting the direction according to degrees
                    int val = (int) ((windDirection / 22.5) + 0.5);
                    String[] arr = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
                    currentWeatherAPI.setWind_direction_name(arr[(val % 16)]);
                    currentWeatherAPI.setCountry_Code(country_code);
                    currentWeatherAPI.setCityName(name);
                    try {
                        snappydb.put("current_weather_data",currentWeatherAPI);
                    } catch (SnappydbException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                e.printStackTrace();
                }

                return currentWeatherAPI;
            }
            else {
                return  null;
            }
        }

        @Override
        protected void onPostExecute(CurrentWeatherAPI currentWeatherAPI) {
            if (currentWeatherAPI != null) {
            setData(currentWeatherAPI);
            }
            else{
                    AlertDialog_netAlert();
            }
        }


    }
    public void setData(CurrentWeatherAPI currentWeatherAPI){
        load.setVisibility(View.GONE);
        TextView fragtext = (TextView) view.findViewById(R.id.fragment_text);
        //all table row text views
        TextView current_temp = (TextView) view.findViewById(R.id.current_temp);
        TextView status = (TextView) view.findViewById(R.id.status);
        TextView winddesc = (TextView) view.findViewById(R.id.Wind_desc);
        TextView temp_desc = (TextView) view.findViewById(R.id.temp_desc);
        TextView pressure_desc = (TextView) view.findViewById(R.id.pressure_desc);
        TextView humidity_desc = (TextView) view.findViewById(R.id.humidity_desc);
        TextView Sunrise_desc = (TextView) view.findViewById(R.id.Sunrise_desc);
        TextView Sunset_desc = (TextView) view.findViewById(R.id.Sunset_desc);
        TextView Current_date = (TextView) view.findViewById(R.id.current_date);
        //setting data in the table
        ImageView current_temp_image = (ImageView) view.findViewById(R.id.current_temp_image);
        fragtext.setText("City of " + currentWeatherAPI.getCountry_Code() + ", " + currentWeatherAPI.getCityName());
        current_temp_image.setImageResource(currentWeatherAPI.getIcon());
        Current_date.setText("Today," + currentWeatherAPI.getCurrent_date());
        BigDecimal temp = round((float) (currentWeatherAPI.getTemp()), 2);
        current_temp.setText("" + temp + (char) 0x00B0 + "C");
        status.setText("" + currentWeatherAPI.getDescription());
        winddesc.setText("Wind Speed: " + currentWeatherAPI.getWind_speed() + "m/s\n" + "Wind Direction:" + currentWeatherAPI.getWind_direstion_degree() + (char) 0x00B0 + "(" + currentWeatherAPI.getWind_direction_name() + ")");
        temp_desc.setText("C: " + round((float) (currentWeatherAPI.getTemp()), 2) + (char) 0x00B0 + "C     F: " + round((float) ((currentWeatherAPI.getTemp()) * 1.8 + 32), 2) + (char) 0x00B0 + "F");
        pressure_desc.setText("" + currentWeatherAPI.getPressure() + " hPa");
        humidity_desc.setText("" + currentWeatherAPI.getHumidity() + "%");
        Sunrise_desc.setText("" + currentWeatherAPI.getSunrise());
        Sunset_desc.setText("" + currentWeatherAPI.getSunset());
        Log.v("I am running","");
    }



    private void AlertDialog_netAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });
        //set the message
        builder.setMessage("Problem in fetching data,Check internet connect and try again");
        //create the alert dialog
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
        alertdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                getActivity().finish();
            }
        });

    }
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==901&&resultCode== Activity.RESULT_OK){
            Bundle bundle=data.getExtras();
            //storing new location in the location list
            Location_details location_new=new Location_details();
            location_new.setLoc_latitude(bundle.getDouble("Latitude"));
            location_new.setLoc_longitude(bundle.getDouble("Longitude"));
            //fetching data about the location and storing
            location_new.setLoc_name(bundle.getString("Sublocality"));
            location_new.setLoc_state_name(bundle.getString("Locality"));
            location_new.setLoc_country_name(bundle.getString("Countryname"));
            //retriving the array list of locations and saving new object in it
            Gson gson = new Gson();
            SharedPreferences prefs = this.getActivity().getSharedPreferences(Pref_file,Context.MODE_PRIVATE);
            String json = prefs.getString("Location_Array_list", null);
            Log.v("jsonString",""+json);
            Type type = new TypeToken<ArrayList<Location_details>>() {}.getType();
            ArrayList<Location_details> arrayList = gson.fromJson(json,type);
            arrayList.add(location_new);
            //storing back the array list in shared preferences
            String jsonstoring = gson.toJson(arrayList);
            prefs.edit().putString("Location_Array_list",jsonstoring).apply();
            String jsonnew = prefs.getString("Location_Array_list", null);
            Log.v("jsonString",""+jsonnew);
            show_dialog(this);
        }else{
            Toast.makeText(this.getActivity(),"No New Location Choosen",Toast.LENGTH_SHORT).show();
        }
    }
}
