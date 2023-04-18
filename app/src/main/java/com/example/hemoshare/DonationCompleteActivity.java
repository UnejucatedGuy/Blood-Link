package com.example.hemoshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class DonationCompleteActivity extends AppCompatActivity {

    String donorName, donorId, donorRating, numberOfDonations;
    Double donorNowRating;
    MaterialTextView txvDonarName, txvRatingText;
    RatingBar ratingBar;
    ImageView imgvDonor;
    Button btnGoToHome;


    //FireBase
    StorageReference storageRef;
    FirebaseFirestore db;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_complete);

        Intent intent = getIntent();
        donorName = intent.getStringExtra("donorName");
        donorId = intent.getStringExtra("donorId");


        txvDonarName = findViewById(R.id.txvDonorName);
        txvRatingText = findViewById(R.id.txvRatingText);
        btnGoToHome = findViewById(R.id.btnGoToHome);
        imgvDonor = findViewById(R.id.imgvDonor);
        ratingBar = findViewById(R.id.ratingBar);

        //FireBase
        storageRef = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        updateUI();

        ratingBar.setMin(1);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating > 4)
                    txvRatingText.setText("Great");
                else if (rating > 3)
                    txvRatingText.setText("Good");
                else if (rating > 2)
                    txvRatingText.setText("Average");
                else if (rating > 1)
                    txvRatingText.setText("Bad");
                else
                    txvRatingText.setText("Worst");
                donorNowRating = Double.valueOf(rating);


            }
        });


        //CLick Listners
        btnGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DonationCompleteActivity.this, MainActivity.class));
                finish();
                //updateRating();
                /*db.collection("users").document(donorId).update("ratingArray", FieldValue.arrayUnion(donorNowRating)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        db.collection("users").document(donorId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                                List<Double> ratingArray = (List<Double>) value.get("ratingArray");
                                Double sum = 0.0;
                                for (Double d : ratingArray)
                                    sum += d;
                                donorRating = String.valueOf(sum / ratingArray.size());
                                db.collection("users").document(donorId).update("donorRating", donorRating).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        startActivity(new Intent(DonationCompleteActivity.this, MainActivity.class));
                                        finish();
                                    }
                                });
                            }
                        });

                    }
                });*/

            }
        });


    }

    private void updateRating() {
        db.collection("users").document(donorId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                numberOfDonations = value.getString("numberOfDonations");
            }
        });

        Map<String, Object> rating = new HashMap<>();
        rating.put(numberOfDonations, rating);
        //db.collection("users").document(donorId).update("rating", Field);

        db.collection("users").document(donorId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Map<String, Object> rating = value.getData();
                Double sum = 0.0;
                for (Map.Entry<String, Object> entry : rating.entrySet()) {
                    sum+=Double.parseDouble((String)entry.getValue());
                }
                donorRating = String.valueOf(sum/rating.size());
            }
        });
        db.collection("users").document(donorId).update("donorRating", donorRating).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                startActivity(new Intent(DonationCompleteActivity.this, MainActivity.class));
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    private void updateUI() {
        StorageReference fileRef = storageRef.child("users/" + donorId + "/profile.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgvDonor);
                txvDonarName.setText(donorName);
            }
        });

    }
}