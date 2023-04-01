package com.example.hemoshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class RequestDetailsActivity extends AppCompatActivity {
    String requestId, name, bloodGroup, phoneNumber, address, note;
    LatLng requestLatlng;
    List<Address> addressList;
    Marker marker;
    GoogleMap mMap;
    MaterialTextView txvName, txvBloodGroup, txvAddress, txvNote, txvPhoneNumber;
    MaterialButton btnCall, btnAcceptRequest;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    //Firebase
    FirebaseFirestore db;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        Intent intent = getIntent();
        requestId = intent.getStringExtra("requestId");
        //Map
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);


        txvName = findViewById(R.id.txvName);
        txvBloodGroup = findViewById(R.id.txvBloodGroup);
        txvAddress = findViewById(R.id.txvAddress);
        txvPhoneNumber = findViewById(R.id.txvPhoneNumber);
        txvNote = findViewById(R.id.txvNote);
        btnCall = findViewById(R.id.btnCall);
        btnAcceptRequest = findViewById(R.id.btnAcceptRequest);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Firebase
        db = FirebaseFirestore.getInstance();
        getRequestData();

        //Click Listners
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });
        btnAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + String.valueOf(requestLatlng.latitude) + "," + String.valueOf(requestLatlng.longitude) + "&mode=l"));
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        });
    }

    private void updateUI() {
        txvName.setText(name);
        txvPhoneNumber.setText(phoneNumber);
        txvBloodGroup.setText(bloodGroup);
        txvAddress.setText(address);
        txvNote.setText(note);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                if (requestLatlng != null) {
                    mMap = googleMap;
                    MarkerOptions markerOptions = new MarkerOptions().position(requestLatlng).title("needs " + bloodGroup);
                    marker = mMap.addMarker(markerOptions);
                    LatLng focusLatlng = new LatLng(requestLatlng.latitude - 0.0037, requestLatlng.longitude);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(focusLatlng, 16));
                }
            }
        });
    }

    private void getRequestData() {
        DocumentReference documentReference = db.collection("requests").document(requestId);
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name = value.getString("name");
                phoneNumber = value.getString("phoneNo");
                address = value.getString("location");
                bloodGroup = value.getString("bloodType");
                note = value.getString("note");
                requestLatlng = new LatLng((Double) value.get("requestLat"), (Double) value.get("requestLng"));
                updateUI();
            }
        });


    }


}