package com.example.naruto.weatherapp.Main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.naruto.weatherapp.APIS.Five3DayAeatherApi;
import com.example.naruto.weatherapp.Extras.GetTheImage;
import com.example.naruto.weatherapp.R;

import java.math.BigDecimal;

public class detail_1 extends AppCompatActivity {

    private Five3DayAeatherApi five3DayAeatherApis;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //check for the detail specifier
        Bundle bundle=getIntent().getExtras();
        five3DayAeatherApis=bundle.getParcelable("Details");
        int specifier=bundle.getInt("Detail_specifier");

        if(specifier==0){
            setContentView(R.layout.activity_detail_1);
            createViewDetail1();
        }
        if(specifier==1){
            setContentView(R.layout.activity_detail_2);
            createViewDetail2();
        }
    }

    private void createViewDetail2() {
        TextView Date=(TextView)findViewById(R.id.Date);
        TextView temp_max=(TextView)findViewById(R.id.temp_max);
        TextView temp_min=(TextView)findViewById(R.id.temp_min);
        TextView weather_status=(TextView)findViewById(R.id.weather_status);
        ImageView weather_image=(ImageView)findViewById(R.id.weather_image_jpg);
        TextView Humidity=(TextView)findViewById(R.id.Humidity);
        TextView pressure=(TextView)findViewById(R.id.pressure_new);
        TextView wind=(TextView)findViewById(R.id.wind);
        TextView temp_morn=(TextView)findViewById(R.id.temp_morn);
        TextView temp_eve=(TextView)findViewById(R.id.temp_eve);
        TextView cloudness=(TextView)findViewById(R.id.cloudness);
        TextView temp_night=(TextView)findViewById(R.id.temp_night);
        //setting the text/imageviews
        GetTheImage gettheimage=new GetTheImage(five3DayAeatherApis.getWeatherId());
        Date.setText(""+five3DayAeatherApis.getCurrent_date());
        temp_max.setText(""+round((float) five3DayAeatherApis.getTemp_max(),1)+ (char) 0x00B0 + "C");
        temp_min.setText(""+round((float) five3DayAeatherApis.getTemp_min(),1)+ (char) 0x00B0 + "C");
        weather_status.setText(five3DayAeatherApis.getDescription());
        weather_image.setImageResource((int) gettheimage.getImageResource());
        Humidity.setText("Humidity: "+five3DayAeatherApis.getHumidity()+"%");
        pressure.setText("Pressure: "+five3DayAeatherApis.getPressure()+" hPa");
        wind.setText("Wind: "+five3DayAeatherApis.getWind_speed()+" m/s, "+five3DayAeatherApis.getWind_direstion_degree()+"("+five3DayAeatherApis.getWind_direction_name()+")");
        cloudness.setText("Clouds: "+five3DayAeatherApis.getClouds()+"%");
        temp_morn.setText("Morning Temperature: "+round((float) five3DayAeatherApis.getMorning_temp(),2)+ (char) 0x00B0+"C");
        temp_eve.setText("Evening Temperature: "+round((float) five3DayAeatherApis.getEvening_temp(),2)+(char) 0x00B0+"C");
        temp_night.setText("Night Temperature: "+round((float) five3DayAeatherApis.getNight_temp(),2)+ (char) 0x00B0+"C");
    }

    //declare all the textviews/image view in the detail1
    private void createViewDetail1() {
        TextView Date=(TextView)findViewById(R.id.Date);
        TextView temp_max=(TextView)findViewById(R.id.temp_max);
        TextView temp_min=(TextView)findViewById(R.id.temp_min);
        TextView weather_status=(TextView)findViewById(R.id.weather_status);
        ImageView weather_image=(ImageView)findViewById(R.id.weather_image_jpg);
        TextView Humidity=(TextView)findViewById(R.id.Humidity);
        TextView pressure=(TextView)findViewById(R.id.pressure_new);
        TextView wind=(TextView)findViewById(R.id.wind);
        TextView sea_level=(TextView)findViewById(R.id.sea_level);
        TextView grnd_level=(TextView)findViewById(R.id.grnd_level);
        TextView cloudness=(TextView)findViewById(R.id.cloudness);
        //setting the text/imageviews
        GetTheImage gettheimage=new GetTheImage(five3DayAeatherApis.getWeatherId());
        Date.setText(""+five3DayAeatherApis.getCurrent_date());
        temp_max.setText(""+round((float) five3DayAeatherApis.getTemp_max(),1)+ (char) 0x00B0 + "C");
        temp_min.setText(""+round((float) five3DayAeatherApis.getTemp_min(),1)+ (char) 0x00B0 + "C");
        weather_status.setText(five3DayAeatherApis.getDescription());
        weather_image.setImageResource((int) gettheimage.getImageResource());
        Humidity.setText("Humidity: "+five3DayAeatherApis.getHumidity()+"%");
        pressure.setText("Pressure: "+five3DayAeatherApis.getPressure()+" hPa");
        wind.setText("Wind: "+five3DayAeatherApis.getWind_speed()+" m/s, "+five3DayAeatherApis.getWind_direstion_degree()+"("+five3DayAeatherApis.getWind_direction_name()+")");
        cloudness.setText("Clouds: "+five3DayAeatherApis.getClouds()+"%");
        sea_level.setText("Sea Level Pressure: "+five3DayAeatherApis.getSea_level()+" hPa");
        grnd_level.setText("Ground Level Pressure: "+five3DayAeatherApis.getGround_level()+" hPa");
    }
//round off the temp to 2 decimal places
    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }
}
