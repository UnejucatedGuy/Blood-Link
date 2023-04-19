package com.example.hemoshare;

import android.content.Intent;
import android.location.Address;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Objects;

public class RequestAcceptedUserActivity extends AppCompatActivity implements EnterCodeDialog.EnterCodeDialogListner {

    String requestId, name, phoneNumber, address, note,requestCode,numberOfDonations,userId,bloodGroup;

    MaterialTextView txvName, txvAddress, txvNote, txvPhoneNumber;
    Button btnEnterCode,btnGetDirections,btnCall;


    //MAP
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    LatLng requestLatlng;
    Double requestLat,requestLng;
    List<Address> addressList;
    Marker marker;
    GoogleMap mMap;

    //Firebase
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_accepted_user);

        Intent intent = getIntent();
        requestId = intent.getStringExtra("requestId");
        /*name = intent.getStringExtra("name");
        phoneNumber = intent.getStringExtra("phoneNumber");
        address = intent.getStringExtra("address");
        note = intent.getStringExtra("note");
        requestLat = intent.getDoubleExtra("requestLat",0);
        requestLng = intent.getDoubleExtra("requestLng",0);
        requestLatlng = new LatLng(requestLat,requestLng);*/

        //Map
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);

        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        txvName = findViewById(R.id.txvName);
        txvAddress = findViewById(R.id.txvAddress);
        txvPhoneNumber = findViewById(R.id.txvPhoneNumber);
        txvNote = findViewById(R.id.txvNote);
        btnCall = findViewById(R.id.btnCall);
        btnEnterCode = findViewById(R.id.btnEnterCode);
        btnGetDirections = findViewById(R.id.btnGetDirection);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        db.collection("requests").document(requestId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name = documentSnapshot.getString("name");
                phoneNumber= documentSnapshot.getString("phoneNumber");
                address= documentSnapshot.getString("location");
                note= documentSnapshot.getString("note");
                bloodGroup= documentSnapshot.getString("bloodGroup");
                requestLat= documentSnapshot.getDouble("requestLat");
                requestLng= documentSnapshot.getDouble("requestLng");
                requestLatlng = new LatLng(requestLat,requestLng);
                updateUI();

            }
        });



        //click Listners
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(intent);
            }
        });

        btnEnterCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

        btnGetDirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=" + String.valueOf(requestLatlng.latitude) + "," + String.valueOf(requestLatlng.longitude) + "&mode=l"));
                intent.setPackage("com.google.android.apps.maps");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }

            }
        });

        db.collection("users").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("numberOfDonations").equals("-")) {
                    numberOfDonations="0";

                } else {
                    numberOfDonations = documentSnapshot.getString("numberOfDonations");
                }
            }
        });

        db.collection("requests").document(requestId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                requestCode = documentSnapshot.getString("code");
            }
        });
    }

    private void openDialog() {
        EnterCodeDialog enterCodeDialog = new EnterCodeDialog();
        enterCodeDialog.show(getSupportFragmentManager(),"Enter Code Dialog");

    }


    private void updateUI() {
        txvName.setText(name);
        txvPhoneNumber.setText(phoneNumber);
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

    @Override
    public void applyTexts(String code) {


        //check code is correct or not
        if(Objects.equals(code, requestCode)){

            numberOfDonations = String.valueOf((Integer.parseInt(numberOfDonations))+1);

            db.collection("users").document(userId).update("numberOfDonations",numberOfDonations).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    db.collection("requests").document(requestId).update("isCompleted",true).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Intent intent = new Intent(RequestAcceptedUserActivity.this,CongratsActivity.class);
                            intent.putExtra("numberOfDonations",numberOfDonations);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });


        }
        else{
            Toast.makeText(this, "Incorrect Code" , Toast.LENGTH_SHORT).show();
        }

    }
}