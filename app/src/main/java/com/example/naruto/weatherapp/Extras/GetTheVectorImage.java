package com.example.naruto.weatherapp.Extras;

import com.example.naruto.weatherapp.R;

/**
 * Created by Naruto on 7/6/2016.
 */
public class GetTheVectorImage {
    private long imageResource;

    public GetTheVectorImage(int weatherId) {
        if (weatherId >= 200 && weatherId <= 232) {
            imageResource = R.drawable.ic_storm;
        } else if (weatherId >= 300 && weatherId <= 321) {
            imageResource = R.drawable.ic_light_rain;
        } else if (weatherId >= 500 && weatherId <= 504) {
            imageResource = R.drawable.ic_rain;
        } else if (weatherId == 511) {
            imageResource = R.drawable.ic_snow;
        } else if (weatherId >= 520 && weatherId <= 531) {
            imageResource = R.drawable.ic_rain;
        } else if (weatherId >= 600 && weatherId <= 622) {
            imageResource = R.drawable.ic_snow;
        } else if (weatherId >= 701 && weatherId <= 761) {
            imageResource = R.drawable.ic_fog;
        } else if (weatherId == 761 || weatherId == 781) {
            imageResource = R.drawable.ic_storm;
        } else if (weatherId == 800) {
            imageResource = R.drawable.ic_clear;
        } else if (weatherId == 801) {
            imageResource = R.drawable.ic_light_clouds;
        } else if (weatherId >= 802 && weatherId <= 804) {
            imageResource = R.drawable.ic_cloudy;
        } else imageResource = -1;
    }


    public long getImageResource() {
        return imageResource;
    }

}
