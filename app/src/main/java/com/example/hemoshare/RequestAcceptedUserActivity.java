package com.example.hemoshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class RequestAcceptedUserActivity extends AppCompatActivity implements EnterCodeDialog.EnterCodeDialogListner {

    String requestId, name, phoneNumber, address, note,requestCode,numberOfDonations,userId;

    MaterialTextView txvName, txvAddress, txvNote, txvPhoneNumber;
    Button btnEnterCode,btnGetDirections,btnCall;
    LatLng requestLatlng;
    Double requestLat,requestLng;

    //Firebase
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_accepted_user);

        Intent intent = getIntent();
        requestId = intent.getStringExtra("requestId");
        name = intent.getStringExtra("name");
        phoneNumber = intent.getStringExtra("phoneNumber");
        address = intent.getStringExtra("address");
        note = intent.getStringExtra("note");
        requestLat = intent.getDoubleExtra("requestLat",0);
        requestLng = intent.getDoubleExtra("requestLng",0);
        requestLatlng = new LatLng(requestLat,requestLng);

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

        updateUI();

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

        db.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                numberOfDonations =value.getString("numberOfDonations");
            }
        });

        db.collection("requests").document(requestId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                requestCode = value.getString("code");
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

    }

    @Override
    public void applyTexts(String code) {


        //check code is correct or not
        if(Objects.equals(code, requestCode)){

            numberOfDonations = String.valueOf((Integer.parseInt(numberOfDonations))+1);

            db.collection("users").document(userId).update("numberOfDonations",numberOfDonations);
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
        else{
            Toast.makeText(this, numberOfDonations, Toast.LENGTH_SHORT).show();
        }

    }
}