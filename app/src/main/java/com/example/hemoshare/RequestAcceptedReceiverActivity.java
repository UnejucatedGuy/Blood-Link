package com.example.hemoshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Random;

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
        donorName = intent.getStringExtra("donorName");
        donorPhoneNumber = intent.getStringExtra("donorPhoneNumber");
        donorId = intent.getStringExtra("donorId");
        code = intent.getStringExtra("code");
        requestLat = intent.getDoubleExtra("requestLat", 0);
        requestLng = intent.getDoubleExtra("requestLng", 0);
        requestLatlng = new LatLng(requestLat, requestLng);

        txvDonorName = findViewById(R.id.txvDonorName);
        txvDonorPhoneNumber = findViewById(R.id.txvDonorPhoneNumber);
        txvCode = findViewById(R.id.txvCode);
        btnCall = findViewById(R.id.btnCall);
        btnVisitProfile = findViewById(R.id.btnVisitProfile);

        //FireBase
        db = FirebaseFirestore.getInstance();

        documentReference = db.collection("requests").document(requestId);

        updateUI();

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
                //Open Profile Activity here
            }
        });


        //Data Change Listner
        db.collection("requests").document(requestId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if((Boolean)value.get("isCompleted"))    {
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