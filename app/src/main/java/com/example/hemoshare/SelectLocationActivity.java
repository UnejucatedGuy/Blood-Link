package com.example.hemoshare;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class SelectLocationActivity extends AppCompatActivity {

    TextInputLayout tilLocation;
    AutoCompleteTextView actvLocation;
    Button btnSelectLocation;

    SupportMapFragment smf;

    FusedLocationProviderClient client;

    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    GoogleMap mMap;
    Geocoder geocoder;
    Marker marker;
    double selectedLat, selectedLng;
    List<Address> addressList;
    String selectedAddress,city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        actvLocation = findViewById(R.id.actvLocation);
        tilLocation = findViewById(R.id.tilLocation);
        btnSelectLocation = findViewById(R.id.btnSelectLocation);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        geocoder = new Geocoder(SelectLocationActivity.this, Locale.getDefault());


        smf = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);

        Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getUserCurrentLocation();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        Toast.makeText(SelectLocationActivity.this, " Location Permission Denied ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
        //click Listners
        btnSelectLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("address", selectedAddress);
                resultIntent.putExtra("lat",selectedLat);
                resultIntent.putExtra("lng",selectedLng);
                resultIntent.putExtra("city",city);


                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }


    public void getUserCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> task = client.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                smf.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {
                        if (location != null) {
                            mMap = googleMap;
                            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                            if (latLng != null) {
                                try {
                                    addressList = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                if (addressList != null) {
                                    String mAddress = addressList.get(0).getAddressLine(0);
                                    city = addressList.get(0).getLocality();
                                    selectedLat = latLng.latitude;
                                    selectedLng = latLng.longitude;
                                    selectedAddress = mAddress;
                                    actvLocation.setText(selectedAddress);

                                }
                            }

                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("You are here...!!");
                            marker = mMap.addMarker(markerOptions);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(@NonNull LatLng latLng) {
                                    checkConnection();
                                    if (networkInfo.isConnected() && networkInfo.isAvailable()) {
                                        selectedLat = latLng.latitude;
                                        selectedLng = latLng.longitude;
                                        getAddress(selectedLat, selectedLng);
                                        actvLocation.setText(selectedAddress);
                                    } else {
                                        Toast.makeText(SelectLocationActivity.this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SelectLocationActivity.this, "Please Turn On Location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void getAddress(double mLat, double mLng) {

        if (mLat != 0) {
            try {
                addressList = geocoder.getFromLocation(mLat, mLng, 1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if (addressList != null) {
                String mAddress = addressList.get(0).getAddressLine(0);
                city = addressList.get(0).getLocality();
                selectedAddress = mAddress;

                if (selectedAddress != null) {
                    marker.remove();
                    MarkerOptions markerOptions = new MarkerOptions();
                    LatLng latLng = new LatLng(mLat, mLng);
                    markerOptions.position(latLng).title(selectedAddress);
                    marker = mMap.addMarker(markerOptions);
                    marker.showInfoWindow();
                }

            }
        }
    }

    private void checkConnection() {
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();


    }

}