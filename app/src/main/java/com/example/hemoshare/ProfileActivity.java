package com.example.hemoshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    TextView tvProfileName, tvAge, tvBloodGroup, tvGender, tvName, tvEmail, tvPhoneNo, tvCity, tvDonationCount, tvDonorRatings;
    Button btnEditProfile;
    ImageView imgProfile;

    String name, age, bloodGroup, gender, email, phoneNo, city, userID,numberOfDonations,donorRating;
    Uri profileImgUri;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userId");

        //Binding UI
        tvProfileName = findViewById(R.id.tvProfileName);
        tvAge = findViewById(R.id.tvAge);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        //tvGender = findViewById(R.id.tvGender);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNo = findViewById(R.id.tvPhoneNumber);
        tvCity = findViewById(R.id.tvCity);
        tvDonationCount = findViewById(R.id.tvDonationCount);
        tvDonorRatings = findViewById(R.id.tvDonorRatings);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        imgProfile = findViewById(R.id.imgProfile);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        getProfileData();

        //click listners
        btnEditProfile.setOnClickListener(v -> {
            Intent intent1 = new Intent(ProfileActivity.this, NewProfileActivity.class);
            intent1.putExtra("activityCode","edit");
            startActivity(intent1);
        });



    }

    private void getProfileData() {
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name = value.getString("name");
                age = calculateAge(value.getLong("birthYear"));
                bloodGroup = value.getString("bloodGroup");
                //gender = value.getString("gender");
                email = value.getString("email");
                phoneNo = value.getString("phoneNumber");
                city = value.getString("city");
                donorRating = value.getString("donorRating");
                numberOfDonations = value.getString("numberOfDonations");

                //updateUi
                tvProfileName.setText(name);
                tvAge.setText(age);
                tvBloodGroup.setText(bloodGroup);
                //tvGender.setText(gender);
                tvName.setText(name);
                tvEmail.setText(email);
                tvPhoneNo.setText(phoneNo);
                tvCity.setText(city);
                tvDonationCount.setText(numberOfDonations);
                tvDonorRatings.setText(donorRating);
                if (userID.equals(mAuth.getCurrentUser().getUid()))
                    btnEditProfile.setVisibility(View.VISIBLE);
                else
                    btnEditProfile.setVisibility(View.GONE);
                setProfileImage();


            }
        });
    }

    private String calculateAge(long birthYear) {

        Calendar calendar = Calendar.getInstance();
        long curYear = calendar.get(Calendar.YEAR);
        return String.valueOf(curYear - birthYear);
    }

    public void setProfileImage() {
        StorageReference fileRef = storageRef.child("users/" + userID + "/profile.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgProfile);


            }
        });
    }


}