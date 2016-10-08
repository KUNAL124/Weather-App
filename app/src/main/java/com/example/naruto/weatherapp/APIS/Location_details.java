package com.example.naruto.weatherapp.APIS;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Naruto on 7/9/2016.
 */


public class Location_details implements Parcelable {
    private String loc_name;
    private String loc_state_name;
    private String loc_country_name;
    private double loc_latitude;
    private double loc_longitude;

    public String getLoc_country_name() {
        return loc_country_name;
    }

    public void setLoc_country_name(String loc_country_name) {
        this.loc_country_name = loc_country_name;
    }

    public double getLoc_latitude() {
        return loc_latitude;
    }

    public void setLoc_latitude(double loc_latitude) {
        this.loc_latitude = loc_latitude;
    }

    public double getLoc_longitude() {
        return loc_longitude;
    }

    public void setLoc_longitude(double loc_longitude) {
        this.loc_longitude = loc_longitude;
    }

    public String getLoc_name() {
        return loc_name;
    }

    public void setLoc_name(String loc_name) {
        this.loc_name = loc_name;
    }

    public String getLoc_state_name() {
        return loc_state_name;
    }

    public void setLoc_state_name(String loc_state_name) {
        this.loc_state_name = loc_state_name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.loc_name);
        dest.writeString(this.loc_state_name);
        dest.writeString(this.loc_country_name);
        dest.writeDouble(this.loc_latitude);
        dest.writeDouble(this.loc_longitude);
    }

    public Location_details() {
    }

    protected Location_details(Parcel in) {
        this.loc_name = in.readString();
        this.loc_state_name = in.readString();
        this.loc_country_name = in.readString();
        this.loc_latitude = in.readDouble();
        this.loc_longitude = in.readDouble();
    }

    public static final Parcelable.Creator<Location_details> CREATOR = new Parcelable.Creator<Location_details>() {
        @Override
        public Location_details createFromParcel(Parcel source) {
            return new Location_details(source);
        }

        @Override
        public Location_details[] newArray(int size) {
            return new Location_details[size];
        }
    };
}
