package com.example.hemoshare;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class WaitingToAcceptActivity extends AppCompatActivity {

    String requestId,bloodGroup,address;

    MaterialTextView txvBloodGroup,txvAddress;
    Button btnDeleteRequest;



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

        db = FirebaseFirestore.getInstance();



        txvBloodGroup = findViewById(R.id.txvBloodGroup);
        txvAddress = findViewById(R.id.txvAddress);
        btnDeleteRequest = findViewById(R.id.btnDeleteRequest);

        db.collection("requests").document(requestId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                bloodGroup = documentSnapshot.getString("bloodGroup");
                address = documentSnapshot.getString("location");
                txvBloodGroup.setText(bloodGroup);
                txvAddress.setText(address);
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

    private void btnDeleteRequest() {

        documentReference.delete();
        startActivity(new Intent(WaitingToAcceptActivity.this,MainActivity.class));
        finish();


    }



}