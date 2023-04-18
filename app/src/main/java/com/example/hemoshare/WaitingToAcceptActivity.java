package com.example.hemoshare;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.List;

public class WaitingToAcceptActivity extends AppCompatActivity {

    String requestId,bloodGroup,address;

    MaterialTextView txvBloodGroup,txvAddress;
    Button btnDeleteRequest;

    //MAP
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient client;
    LatLng requestLatlng;
    List<Address> addressList;
    Marker marker;
    GoogleMap mMap;

    //Firebase
    FirebaseFirestore db;
    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_to_accept);

        Intent intent = getIntent();
        requestId = intent.getStringExtra("requestId");
        /*bloodGroup = intent.getStringExtra("bloodGroup");
        address = intent.getStringExtra("address");*/

        //Map
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);



        db = FirebaseFirestore.getInstance();



        txvBloodGroup = findViewById(R.id.txvBloodGroup);
        txvAddress = findViewById(R.id.txvAddress);
        btnDeleteRequest = findViewById(R.id.btnDeleteRequest);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        db.collection("requests").document(requestId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                bloodGroup = documentSnapshot.getString("bloodGroup");
                address = documentSnapshot.getString("location");
                requestLatlng = new LatLng(documentSnapshot.getDouble("requestLat"),documentSnapshot.getDouble("requestLng"));
                updateUI();
            }
        });



        //Firebase

        documentReference = db.collection("requests").document(requestId);

        //click Listners
        btnDeleteRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDeleteRequest();
            }
        });

        //Data Change Listners
        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if((Boolean)value.get("isAccepted"))    {
                    Intent intent = new Intent(WaitingToAcceptActivity.this,RequestAcceptedReceiverActivity.class);
                    intent.putExtra("requestId",requestId);
                    intent.putExtra("donorName",(String) value.get("donorName"));
                    intent.putExtra("donorPhoneNumber",(String) value.get("donorPhoneNumber"));
                    intent.putExtra("donorId",(String) value.get("donorNId"));
                    intent.putExtra("code",(String) value.get("code"));
                    intent.putExtra("requestLat",(Double) value.get("requestLat"));
                    intent.putExtra("requestLng",(Double) value.get("requestLng"));
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void updateUI() {
        txvBloodGroup.setText(bloodGroup);
        txvAddress.setText(address);
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                if (requestLatlng != null) {
                    mMap = googleMap;
                    MarkerOptions markerOptions = new MarkerOptions().position(requestLatlng).title("You requested for "+bloodGroup);
                    marker = mMap.addMarker(markerOptions);
                    LatLng focusLatlng = new LatLng(requestLatlng.latitude - 0.0037, requestLatlng.longitude);
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(focusLatlng, 15));
                }
            }
        });
    }

    private void btnDeleteRequest() {
        startActivity(new Intent(WaitingToAcceptActivity.this,MainActivity.class));
        documentReference.delete();
        finish();


    }



}