package com.example.hemoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DonationCompleteActivity extends AppCompatActivity {

    String donorName,donorId;
    MaterialTextView txvDonarName;
    ImageView imgvDonor;
    Button btnGoToHome;

    //FireBase
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_complete);

        Intent intent = getIntent();
        donorName = intent.getStringExtra("donorName");
        donorId = intent.getStringExtra("donorId");


        txvDonarName = findViewById(R.id.txvDonorName);
        btnGoToHome = findViewById(R.id.btnGoToHome);
        imgvDonor = findViewById(R.id.imgvDonor);

        //FireBase
        storageRef = FirebaseStorage.getInstance().getReference();

        updateUI();


        //CLick Listners
        btnGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DonationCompleteActivity.this,MainActivity.class));
                finish();
            }
        });





    }

    @Override
    public void onBackPressed() {

    }

    private void updateUI() {
        StorageReference fileRef = storageRef.child("users/"+donorId+"/profile.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgvDonor);
                txvDonarName.setText(donorName);
            }
        });

    }
}