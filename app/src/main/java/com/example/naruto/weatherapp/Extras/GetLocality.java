package com.example.naruto.weatherapp.Extras;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Naruto on 7/3/2016.
 */
public class GetLocality {
    private  String locality;
    private String countryname;
    private String arearname;
    private String CompleteAddress;
    private double latitude;
    private double longitude;
    //get the latitude and longitude of the location
    public GetLocality(Context context, double lat, double lan) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat,lan,1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                locality=address.getLocality();
                arearname=address.getSubLocality();
                countryname=address.getCountryName();
                latitude=address.getLatitude();
                longitude=address.getLongitude();
                CompleteAddress=address.getAddressLine(0);
                for(int i=1;i<address.getMaxAddressLineIndex();i++)
                CompleteAddress+=(address.getAddressLine(i)+" ");
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
    }

    public String getLocality() {
        return locality;
    }

    public String getArearname() {
        return arearname;
    }

    public String getCountryname() {
        return countryname;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCompleteAddress() {
        return CompleteAddress;
    }
}
