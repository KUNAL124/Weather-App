package com.example.naruto.weatherapp.Fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naruto.weatherapp.APIS.Five3DayAeatherApi;
import com.example.naruto.weatherapp.Adapter.CustomRecyclerAdapter;
import com.example.naruto.weatherapp.Extras.GetTheVectorImage;
import com.example.naruto.weatherapp.NetStateChecker.NetAvailability;
import com.example.naruto.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link six10dayforcast#newInstance} factory method to
 * create an instance of this fragment.
 */
public class six10dayforcast extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView weatherforcastlist;
    private CustomRecyclerAdapter weatherlistadapter;
    private ArrayList<Five3DayAeatherApi> forcastlist = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private final String Log_Tag = Current_weather.class.getSimpleName();
    private final String API_KEY = "91a278269e52fa60e42041b4fbdf6372";
    private View view;
    private double latitude = 0;
    private double longitude = 0;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public six10dayforcast() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment six10dayforcast.
     */
    // TODO: Rename and change types and number of parameters
    public static six10dayforcast newInstance(String param1, String param2) {
        six10dayforcast fragment = new six10dayforcast();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        if(NetAvailability.isNetworkAvailable(getContext())){
        Refreshforcast();}
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(NetAvailability.isNetworkAvailable(getContext()))
        Refreshforcast();
        return super.onOptionsItemSelected(item);
    }

    private void Refreshforcast() {
        SharedPreferences preferences = this.getActivity().getSharedPreferences(getString(R.string.PREF_FILE), Context.MODE_PRIVATE);
        latitude = preferences.getFloat(getString(R.string.Current_selected_latitude), (float) 0.0);
        longitude = preferences.getFloat(getString(R.string.Current_selected_longitude), (float) 0.0);
        FetchCurrentWeather fetchCurrentWeather = new FetchCurrentWeather();
        fetchCurrentWeather.execute();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view=inflater.inflate(R.layout.forcast_16day, container, false);
        weatherforcastlist = (RecyclerView) view.findViewById(R.id.daysforcast);
        weatherforcastlist.setLayoutManager(new LinearLayoutManager(getActivity()));
        weatherlistadapter = new CustomRecyclerAdapter(getActivity(),1);
        weatherforcastlist.setAdapter(weatherlistadapter);
        TextView header=(TextView)view.findViewById(R.id.Heading);
        header.setText("16 Days-Daily Forcast");
        return view;
    }

    private class FetchCurrentWeather extends AsyncTask<Void, Void, ArrayList<Five3DayAeatherApi>> {
        @Override
        protected ArrayList<Five3DayAeatherApi> doInBackground(Void... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;
            ArrayList<Five3DayAeatherApi> five3DayAeatherApis = new ArrayList<>();

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                //Defining different parts of the url and building uri
                //by uri builder class
                //after uri creation it is converted to url
                final int results=16;
                final String URL_BASE = "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String query_param_lat = "lat";
                final String query_param_lon = "lon";
                final String query_param_cnt="cnt";
                final String query_param_mode="mode";
                final String mode="json";
                final String appid = "appid";
                Uri built_uri = Uri.parse(URL_BASE).buildUpon()
                        .appendQueryParameter(query_param_lat, String.valueOf(latitude))
                        .appendQueryParameter(query_param_lon, String.valueOf(longitude))
                        .appendQueryParameter(query_param_cnt,String.valueOf(results))
                        .appendQueryParameter(query_param_mode,mode)
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
            final String OWN_DATETIME = "dt";
            final String OWN_PRESSURE = "pressure";
            final String OWN_HUMIDITY = "humidity";
            final String OWN_SPEED = "speed";
            final String OWN_DEGREE = "deg";

            //all temperature are childer of temp object
            final String OWN_TEMPERATURE = "temp";
            final String OWN_MAX = "max";
            final String OWN_MIN = "min";
            final String OWN_DAY_TEMP="day";
            final String OWN_MORN_TEMP="morn";
            final String OWN_EVE_TEMP="eve";
            final String OWN_NIGHT_TEMP="night";
            final String OWN_WEATHER = "weather";
            final String OWN_MAIN = "main";
            final String OWN_DESCRIPTION = "description";
            final String OWN_WEATHER_ID = "id";

            final String OWN_LIST = "list";
            //clouds
            final String OWN_CLOUDS = "clouds";
            if(NetAvailability.isNetworkAvailable(getContext())) {
                try {
                    // These are the values that will be collected.
                    int weatherId;
                    String current_date;
                    long date;
                    double temp;
                    double pressure;
                    double humidity;
                    double windSpeed;
                    double windDirection;
                    double temp_min;
                    double temp_max;
                    double temp_morn;
                    double temp_eve;
                    double temp_night;
                    String main;
                    String description;
                    double clouds;
                    //get the json object from the fetched string
                    JSONObject weatherobject = new JSONObject(forecastJsonStr);
                    //get the weather array
                    JSONArray weather = weatherobject.getJSONArray(OWN_LIST);
                    for (int i = 0; i < weather.length(); i++) {
                        Five3DayAeatherApi five3DayAeatherApi = new Five3DayAeatherApi();
                        JSONObject listobject = weather.getJSONObject(i);
                        //current date;
                        date = listobject.getLong(OWN_DATETIME);
                        current_date = new SimpleDateFormat("MMM dd").format(new Date(date * 1000));
                        //get the main object
                        JSONObject temperature = listobject.getJSONObject(OWN_TEMPERATURE);
                        temp = temperature.getDouble(OWN_DAY_TEMP);
                        temp_min = temperature.getDouble(OWN_MIN);
                        temp_max = temperature.getDouble(OWN_MAX);
                        temp_morn = temperature.getDouble(OWN_MORN_TEMP) - 273;
                        temp_eve = temperature.getDouble(OWN_EVE_TEMP) - 273;
                        temp_night = temperature.getDouble(OWN_NIGHT_TEMP) - 273;
                        pressure = listobject.getDouble(OWN_PRESSURE);
                        humidity = listobject.getDouble(OWN_HUMIDITY);
                        JSONArray weatherArray = listobject.getJSONArray(OWN_WEATHER);

                        JSONObject weatherarrayobject = weatherArray.getJSONObject(0);
                        weatherId = weatherarrayobject.getInt(OWN_WEATHER_ID);
                        main = weatherarrayobject.getString(OWN_MAIN);
                        description = weatherarrayobject.getString(OWN_DESCRIPTION);
                        pressure = listobject.getDouble(OWN_PRESSURE);
                        clouds = listobject.getDouble(OWN_CLOUDS);

                        windSpeed = listobject.getDouble(OWN_SPEED);
                        windDirection = listobject.getDouble(OWN_DEGREE);
                        //calling setters;
                        five3DayAeatherApi.setCurrent_date(current_date);
                        five3DayAeatherApi.setDescription(description);
                        five3DayAeatherApi.setHumidity(humidity);
                        //get the image according to weather id
                        five3DayAeatherApi.setWeatherId(weatherId);
                        GetTheVectorImage getTheImage = new GetTheVectorImage(weatherId);
                        five3DayAeatherApi.setIcon((int) getTheImage.getImageResource());
                        five3DayAeatherApi.setMain(main);
                        five3DayAeatherApi.setPressure((double) pressure);
                        five3DayAeatherApi.setTemp((double) ((temp - 273)));
                        five3DayAeatherApi.setTemp_max((double) ((temp_max - 273)));
                        five3DayAeatherApi.setTemp_min((double) ((temp_min - 273)));
                        five3DayAeatherApi.setWind_speed((double) windSpeed);
                        five3DayAeatherApi.setWind_direstion_degree((double) windDirection);
                        five3DayAeatherApi.setClouds(clouds);
                        five3DayAeatherApi.setMorning_temp(temp_morn);
                        five3DayAeatherApi.setEvening_temp(temp_eve);
                        five3DayAeatherApi.setNight_temp(temp_night);
                        //setting the direction according to degrees
                        int val = (int) ((windDirection / 22.5) + 0.5);
                        String[] arr = {"N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW"};
                        five3DayAeatherApi.setWind_direction_name(arr[(val % 16)]);
                        five3DayAeatherApis.add(five3DayAeatherApi);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return five3DayAeatherApis;
            }else{

                return null;
            }

        }

        @Override
        protected void onPostExecute(ArrayList<Five3DayAeatherApi> five3DayAeatherApisList) {
            if(five3DayAeatherApisList!=null)
                weatherlistadapter.notifyDataSetChanged();
            weatherlistadapter.setForcastList(five3DayAeatherApisList);
        }
    }
    private void AlertDialog_netAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "Check the network", Toast.LENGTH_SHORT).show();
            }
        });
        //set the message
        builder.setMessage("Problem in fetching data,Check internet connect and try again");
        //create the alert dialog
        AlertDialog alertdialog = builder.create();
        alertdialog.show();
    }

}
