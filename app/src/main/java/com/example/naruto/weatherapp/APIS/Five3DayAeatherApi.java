package com.example.naruto.weatherapp.APIS;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Naruto on 7/6/2016.
 */

public class Five3DayAeatherApi implements Parcelable {
    private String main;
    private String Description;
    private double temp;
    private double humidity;
    private double pressure;
    private double temp_min;
    private double temp_max;
    private double wind_speed;
    private double wind_direstion_degree;
    private String wind_direction_name;
    private String Current_date;
    private int icon;
    private int weatherId;
    private double sea_level;
    private double ground_level;
    private double clouds;
    private double night_temp;
    private double morning_temp;
    private double evening_temp;

    public double getEvening_temp() {
        return evening_temp;
    }

    public void setEvening_temp(double evening_temp) {
        this.evening_temp = evening_temp;
    }

    public double getMorning_temp() {
        return morning_temp;
    }

    public void setMorning_temp(double morning_temp) {
        this.morning_temp = morning_temp;
    }

    public double getNight_temp() {
        return night_temp;
    }

    public void setNight_temp(double night_temp) {
        this.night_temp = night_temp;
    }

    public double getClouds() {
        return clouds;
    }

    public void setClouds(double clouds) {
        this.clouds = clouds;
    }


    public String getCurrent_date() {
        return Current_date;
    }

    public void setCurrent_date(String current_date) {
        Current_date = current_date;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public double getGround_level() {
        return ground_level;
    }

    public void setGround_level(double ground_level) {
        this.ground_level = ground_level;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public double getSea_level() {
        return sea_level;
    }

    public void setSea_level(double sea_level) {
        this.sea_level = sea_level;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(double temp_max) {
        this.temp_max = temp_max;
    }

    public double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(double temp_min) {
        this.temp_min = temp_min;
    }

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public String getWind_direction_name() {
        return wind_direction_name;
    }

    public void setWind_direction_name(String wind_direction_name) {
        this.wind_direction_name = wind_direction_name;
    }

    public double getWind_direstion_degree() {
        return wind_direstion_degree;
    }

    public void setWind_direstion_degree(double wind_direstion_degree) {
        this.wind_direstion_degree = wind_direstion_degree;
    }

    public double getWind_speed() {
        return wind_speed;
    }

    public void setWind_speed(double wind_speed) {
        this.wind_speed = wind_speed;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.main);
        dest.writeString(this.Description);
        dest.writeDouble(this.temp);
        dest.writeDouble(this.humidity);
        dest.writeDouble(this.pressure);
        dest.writeDouble(this.temp_min);
        dest.writeDouble(this.temp_max);
        dest.writeDouble(this.wind_speed);
        dest.writeDouble(this.wind_direstion_degree);
        dest.writeString(this.wind_direction_name);
        dest.writeString(this.Current_date);
        dest.writeInt(this.icon);
        dest.writeInt(this.weatherId);
        dest.writeDouble(this.sea_level);
        dest.writeDouble(this.ground_level);
        dest.writeDouble(this.clouds);
        dest.writeDouble(this.night_temp);
        dest.writeDouble(this.morning_temp);
        dest.writeDouble(this.evening_temp);
    }

    public Five3DayAeatherApi() {
    }

    protected Five3DayAeatherApi(Parcel in) {
        this.main = in.readString();
        this.Description = in.readString();
        this.temp = in.readDouble();
        this.humidity = in.readDouble();
        this.pressure = in.readDouble();
        this.temp_min = in.readDouble();
        this.temp_max = in.readDouble();
        this.wind_speed = in.readDouble();
        this.wind_direstion_degree = in.readDouble();
        this.wind_direction_name = in.readString();
        this.Current_date = in.readString();
        this.icon = in.readInt();
        this.weatherId = in.readInt();
        this.sea_level = in.readDouble();
        this.ground_level = in.readDouble();
        this.clouds = in.readDouble();
        this.night_temp = in.readDouble();
        this.morning_temp = in.readDouble();
        this.evening_temp = in.readDouble();
    }

    public static final Parcelable.Creator<Five3DayAeatherApi> CREATOR = new Parcelable.Creator<Five3DayAeatherApi>() {
        @Override
        public Five3DayAeatherApi createFromParcel(Parcel source) {
            return new Five3DayAeatherApi(source);
        }

        @Override
        public Five3DayAeatherApi[] newArray(int size) {
            return new Five3DayAeatherApi[size];
        }
    };
}
