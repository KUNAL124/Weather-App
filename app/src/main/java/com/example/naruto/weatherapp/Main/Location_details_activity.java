package com.example.naruto.weatherapp.Main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.naruto.weatherapp.APIS.Location_details;
import com.example.naruto.weatherapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Location_details_activity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private double latitude;
    private double longitude;
    private String Loc_name;
    private String State_name;
    private String Country_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_details);
        //get the details for the location
        Bundle bundle=getIntent().getExtras();
        Location_details location=bundle.getParcelable("Location_details");
        Loc_name=location.getLoc_name();
        State_name=location.getLoc_state_name();
        Country_name=location.getLoc_country_name();
        latitude=location.getLoc_latitude();
        longitude=location.getLoc_longitude();
        TextView lat=(TextView)findViewById(R.id.location_latitude);
        TextView lon=(TextView)findViewById(R.id.location_longitude);
        TextView country=(TextView)findViewById(R.id.location_country);
        TextView state=(TextView)findViewById(R.id.location_state);
        TextView city=(TextView)findViewById(R.id.location_city);
        //set the data to text views
        lat.setText("Latitude: "+latitude);
        lon.setText("Longitude: "+longitude);
        if(Country_name==null){
            country.setText("Country: -----");
        }else country.setText("Country: "+Country_name);
        if(State_name==null){
            state.setText("State: -----");
        }else state.setText("State: "+State_name);
        if(Loc_name==null){
            city.setText("City: -----");
        }else city.setText("City: "+Loc_name);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        int zoom=14;
        if(Loc_name!=null){
            zoom=14;
        }else
        if(State_name!=null){
            zoom=10;
        }else {
            zoom=4;
        }
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(latitude,longitude);
        mMap.addMarker(new MarkerOptions().position(location));
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.latitude,location.longitude))      // Sets the center of the map to location user
                .zoom(zoom)                   // Sets the zoom
                .build();                   // Creates a CameraPosition from the builder
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
