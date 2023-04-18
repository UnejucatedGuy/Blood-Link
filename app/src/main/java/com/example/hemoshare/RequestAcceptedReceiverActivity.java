package com.example.hemoshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class RequestAcceptedReceiverActivity extends AppCompatActivity {

    String requestId, donorName, donorPhoneNumber, donorId, code;
    MaterialTextView txvDonorName, txvDonorPhoneNumber,txvCode;
    Button btnShowCode, btnVisitProfile, btnCall;

    //Map
    LatLng requestLatlng;
    Double requestLat, requestLng;

    //FireBase
    FirebaseFirestore db;
    DocumentReference documentReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_accepted_receiver);

        Intent intent = getIntent();
        requestId = intent.getStringExtra("requestId");
        /*donorName = intent.getStringExtra("donorName");
        donorPhoneNumber = intent.getStringExtra("donorPhoneNumber");
        donorId = intent.getStringExtra("donorId");
        code = intent.getStringExtra("code");
        requestLat = intent.getDoubleExtra("requestLat", 0);
        requestLng = intent.getDoubleExtra("requestLng", 0);
        requestLatlng = new LatLng(requestLat, requestLng);*/





        txvDonorName = findViewById(R.id.txvDonorName);
        txvDonorPhoneNumber = findViewById(R.id.txvDonorPhoneNumber);
        txvCode = findViewById(R.id.txvCode);
        btnCall = findViewById(R.id.btnCall);
        btnVisitProfile = findViewById(R.id.btnVisitProfile);


//FireBase
        db = FirebaseFirestore.getInstance();

        db.collection("requests").document(requestId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                donorName = documentSnapshot.getString("donorName");
                donorPhoneNumber = documentSnapshot.getString("donorPhoneNumber");
                donorId = documentSnapshot.getString("donorId");
                code = documentSnapshot.getString("code");
                requestLat = documentSnapshot.getDouble("requestLat");
                requestLng = documentSnapshot.getDouble("requestLng");
                requestLatlng = new LatLng(requestLat, requestLng);
                updateUI();

            }
        });





        //click Listners
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + donorPhoneNumber));
                startActivity(intent);
            }
        });

        btnVisitProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RequestAcceptedReceiverActivity.this, ProfileActivity.class);
                intent.putExtra("userId",donorId);
                startActivity(intent);
            }
        });


        //Data Change Listner
        db.collection("requests").document(requestId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.getBoolean("isCompleted"))    {
                    Intent intent = new Intent(RequestAcceptedReceiverActivity.this,DonationCompleteActivity.class);
                    intent.putExtra("donorId",donorId);
                    intent.putExtra("donarName",donorName);
                    startActivity(intent);
                    finish();
                }
            }
        });




    }
    private void updateUI() {

        txvDonorName.setText(donorName);
        txvDonorPhoneNumber.setText(donorPhoneNumber);
        txvCode.setText(code);

    }
}