package com.example.accidentreport.report;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.accidentreport.R;
import com.example.accidentreport.domain.User;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class AccidentReportActivity extends AppCompatActivity implements View.OnClickListener {

    TextView txtlocation;
    Button buttonLocation;

    private User userLogged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accident_report);

        //RECUPERAR USUARIO LOGGEADO
        userLogged = (User) getIntent().getSerializableExtra("userLogged");

        txtlocation = findViewById(R.id.location);

        //Para el gps
        txtlocation = findViewById(R.id.location);

        buttonLocation = findViewById(R.id.locationButton);
        buttonLocation.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.locationButton:
                int permissionCheck = ContextCompat.checkSelfPermission(AccidentReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    } else {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    }
                } else {
                    getUbication();
                }
                break;
        }
    }

    public void getUbication() {
        LocationManager locationManager = (LocationManager) AccidentReportActivity.this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {

            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    txtlocation.setText((address.get(0).getAddressLine(0)));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
        int permissionCheck = ContextCompat.checkSelfPermission(AccidentReportActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);// para el emulador
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);// para el movil


    }
}
