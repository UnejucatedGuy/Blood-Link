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
import android.widget.Button;
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
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RequestDetailsActivity extends AppCompatActivity {
    String requestId, name, bloodGroup, phoneNumber, address, note,userId,donorName,donorPhoneNumber;
    LatLng requestLatlng;
    List<Address> addressList;
    Marker marker;
    GoogleMap mMap;
    MaterialTextView txvName, txvBloodGroup, txvAddress, txvNote, txvPhoneNumber;
    Button btnCall, btnAcceptRequest,btnDeclineRequest;
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;

    //Firebase
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    DocumentReference documentReference;


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
        btnDeclineRequest = findViewById(R.id.btnDeclineRequest);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();



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
                documentReference = db.collection("users").document(userId);
                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        donorName = value.getString("name");
                        donorPhoneNumber = value.getString("phoneNo");

                        Map<String, Object> request = new HashMap<>();
                        request.put("donorId", userId);
                        request.put("donorName", donorName);
                        request.put("donorPhoneNumber",donorPhoneNumber);
                        request.put("isAccepted",true);
                        db.collection("requests").document(requestId).update(request);

                        Intent intent = new Intent(RequestDetailsActivity.this,RequestAcceptedUserActivity.class);
                        intent.putExtra("requestId",requestId);
                        intent.putExtra("name",name);
                        intent.putExtra("phoneNumber",phoneNumber);
                        intent.putExtra("address",address);
                        intent.putExtra("note",note);
                        intent.putExtra("requestLat", requestLatlng.latitude);
                        intent.putExtra("requestLng", requestLatlng.longitude);
                        startActivity(intent);
                        finish();
                    }
                });





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
                bloodGroup = value.getString("bloodGroup");
                note = value.getString("note");
                requestLatlng = new LatLng((Double) value.get("requestLat"), (Double) value.get("requestLng"));
                updateUI();
            }
        });


    }


}