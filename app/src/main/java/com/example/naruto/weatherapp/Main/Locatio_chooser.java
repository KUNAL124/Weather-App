package com.example.naruto.weatherapp.Main;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.naruto.weatherapp.Extras.GetLocality;
import com.example.naruto.weatherapp.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Locatio_chooser extends FragmentActivity {

    private GoogleMap mMap;
    private double latitude = -1;
    private double longitude = -1;
    private String Locality;
    private String Sublocality;
    private String Countryname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locatio_chooser);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        /*mapFragment.getMapAsync(this);*/
        // Setting a click event handler for the map
        mMap = mapFragment.getMap();
        ImageButton submitResult = (ImageButton) findViewById(R.id.Submit);
        submitResult.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                if (latitude == -1 && longitude == -1) {
                                                    Toast.makeText(getBaseContext(), "No location choosen", Toast.LENGTH_SHORT).show();
                                                } else if (Locality == null && Sublocality == null && Countryname == null) {
                                                    Toast.makeText(getBaseContext(), "Invalid Location!This location has no name\nPlease select a valid location", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    setResult(RESULT_OK, getIntent()
                                                            .putExtra("Latitude", latitude)
                                                            .putExtra("Longitude", longitude)
                                                            .putExtra("Locality", Locality)
                                                            .putExtra("Sublocality", Sublocality)
                                                            .putExtra("Countryname", Countryname));
                                                    finish();
                                                }
                                            }
                                        }
        );
        final TextView location_name = (TextView) findViewById(R.id.Location_selected_name);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener()

                                   {

                                       @Override
                                       public void onMapClick(LatLng latLng) {
                                           // Creating a marker
                                           MarkerOptions markerOptions = new MarkerOptions();

                                           // Setting the position for the marker
                                           markerOptions.position(latLng);
                                           // Setting the title for the marker.
                                           // This will be displayed on taping the marker
                                           //creating an object of the GetLocality
                                           markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                                           latitude = latLng.latitude;
                                           longitude = latLng.longitude;
                                           GetLocality getLocality = new GetLocality(getBaseContext(), latLng.latitude, latLng.longitude);
                                           Locality = getLocality.getLocality();
                                           Sublocality = getLocality.getArearname();
                                           Countryname = getLocality.getCountryname();
                                           location_name.setText(getLocality.getCompleteAddress());
                                           // Clears the previously touched position
                                           mMap.clear();

                                           // Animating to the touched position
                                           mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                           // Placing a marker on the touched position
                                           mMap.addMarker(markerOptions);
                                       }
                                   }

        );

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
    /*@Override
    public void onMapReady(GoogleMap mMap) {
        mMap = mMap;
        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }*/
}
