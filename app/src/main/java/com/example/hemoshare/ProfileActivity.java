package com.example.hemoshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    TextView tvProfileName,tvAge,tvBloodType,tvGender,tvName,tvEmail,tvPhoneNo,tvCity,tvDonationCount,tvDonorRatings;
    Button btnEditProfile;

    String name,age,bloodType,gender,email,phoneNo,city,userID;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Binding UI
        tvProfileName = findViewById(R.id.tvProfileName);
        tvAge = findViewById(R.id.tvAge);
        tvBloodType = findViewById(R.id.tvBloodType);
        tvGender = findViewById(R.id.tvGender);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNo = findViewById(R.id.tvPhoneNumber);
        tvCity = findViewById(R.id.tvCity);
        tvDonationCount = findViewById(R.id.tvDonationCount);
        tvDonorRatings = findViewById(R.id.tvDonorRatings);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        getProfileData();



    }

    private void getProfileData() {
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name = value.getString("name");
                //age = String.valueOf(calculateAge((Integer) value.get("birthYear")));
                bloodType = value.getString("bloodType");
                gender = value.getString("gender");
                email = value.getString("email");
                phoneNo = value.getString("phoneNo");
                city = value.getString("city");

                //updateUi
                tvProfileName.setText(name);
                //tvAge.setText(age);
                tvBloodType.setText(bloodType);
                tvGender.setText(gender);
                tvName.setText(name);
                tvEmail.setText(email);
                tvPhoneNo.setText(phoneNo);
                tvCity.setText(city);

            }
        });
    }

    private int calculateAge(int birthYear){

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        return curYear - birthYear;
    }

}