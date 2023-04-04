package com.example.hemoshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    TextView tvProfileName,tvAge, tvBloodGroup,tvGender,tvName,tvEmail,tvPhoneNo,tvCity,tvDonationCount,tvDonorRatings;
    Button btnEditProfile;
    ImageView imgProfile;

    String name,age, bloodGroup,gender,email,phoneNo,city,userID;
    Uri profileImgUri;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //Binding UI
        tvProfileName = findViewById(R.id.tvProfileName);
        tvAge = findViewById(R.id.tvAge);
        tvBloodGroup = findViewById(R.id.tvBloodGroup);
        tvGender = findViewById(R.id.tvGender);
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhoneNo = findViewById(R.id.tvPhoneNumber);
        tvCity = findViewById(R.id.tvCity);
        tvDonationCount = findViewById(R.id.tvDonationCount);
        tvDonorRatings = findViewById(R.id.tvDonorRatings);
        imgProfile = findViewById(R.id.imgProfile);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference();
        getProfileData();




    }

    private void getProfileData() {
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name = value.getString("name");
                //age = String.valueOf(calculateAge((Integer) value.get("birthYear")));
                bloodGroup = value.getString("bloodGroup");
                gender = value.getString("gender");
                email = value.getString("email");
                phoneNo = value.getString("phoneNo");
                city = value.getString("city");

                //updateUi
                tvProfileName.setText(name);
                //tvAge.setText(age);
                tvBloodGroup.setText(bloodGroup);
                tvGender.setText(gender);
                tvName.setText(name);
                tvEmail.setText(email);
                tvPhoneNo.setText(phoneNo);
                tvCity.setText(city);
                setProfileImage();
                


            }
        });
    }

    private int calculateAge(int birthYear){

        Calendar calendar = Calendar.getInstance();
        int curYear = calendar.get(Calendar.YEAR);
        return curYear - birthYear;
    }
    
    public void setProfileImage(){
        StorageReference fileRef = storageRef.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgProfile);
            }
        });
    }
        

}