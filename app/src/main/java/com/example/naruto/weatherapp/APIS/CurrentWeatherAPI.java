package com.example.naruto.weatherapp.APIS;

/**
 * Created by Naruto on 7/2/2016.
 */
public class CurrentWeatherAPI  {
    private double latitude;
    private double longitude;
    private String sunrise;
    private String sunset;
    private String main;
    private String Description;
    private double temp;
    private double humidity;
    private double pressure;
    private double wind_speed;
    private double wind_direstion_degree;
    private String wind_direction_name;
    private String Current_date;
    private int icon;
    private int weatherId;
    private String Country_Code;
    private String City_Name;

    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public void setCountry_Code(String country_Code) {
        Country_Code = country_Code;
    }

    public String getCityName() {
        return City_Name;
    }

    public void setCityName(String city_Name) {
        City_Name = city_Name;
    }

    public String getCountry_Code() {
        return Country_Code;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
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

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
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

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
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

}
