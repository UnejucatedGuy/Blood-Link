package com.example.hemoshare;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hemoshare.Model.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewProfileActivity extends AppCompatActivity {

    TextInputLayout tilName,tilPhoneNumber,tilAddress,tilBloodGroup,tilBirthDate;
    TextView errorText,txvEditOrComplete;
    AutoCompleteTextView actvBloodGroup,actvBirthDate,actvAddress;
    ImageView imgProfile;

    Button btnSave,btnImgSelect;

    String name,email, phoneNumber,city,gender, bloodGroup,userID,address,activityCode;
    boolean isPictureSelected = false;
    String[] bloodGroups = new String[]{"AB+", "AB-", "O+", "O-", "A+", "A-", "B+", "B-"};
    Uri profileImgUri;
    long birthDay,birthMonth,birthYear;

    ArrayAdapter<String> arrayAdapter;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        Intent intent = getIntent();
        activityCode = intent.getStringExtra("activityCode");

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference();


        //Binding UI
        errorText =findViewById(R.id.errorText);
        txvEditOrComplete =findViewById(R.id.txvEditOrComplete);
        tilName = findViewById(R.id.tilName);
        //tilEmail = findViewById(R.id.tilEmail);
        tilPhoneNumber = findViewById(R.id.tilPhoneNumber);
        tilAddress = findViewById(R.id.tilAddress);
        //edtGender = findViewById(R.id.edtGender);
        tilBirthDate = findViewById(R.id.tilBirthDate);
        actvBirthDate = findViewById(R.id.actvBirthDate);
        actvBloodGroup = findViewById(R.id.actvBloodGroup);
        actvAddress = findViewById(R.id.actvAddress);
        btnSave = findViewById(R.id.btnSave);
        btnImgSelect = findViewById(R.id.btnImgSelect);
        imgProfile = findViewById(R.id.imgProfile);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item_blood_group, bloodGroups);
        actvBloodGroup.setAdapter(arrayAdapter);

        if(activityCode.equals("edit")){
            db.collection("users").document(userID).get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    name = documentSnapshot.getString("name");
                    phoneNumber = documentSnapshot.getString("phoneNumber");
                    address = documentSnapshot.getString("address");
                    birthDay = documentSnapshot.getLong("birthDay");
                    birthMonth = documentSnapshot.getLong("birthMonth");
                    birthYear = documentSnapshot.getLong("birthYear");
                    bloodGroup = documentSnapshot.getString("bloodGroup");
                    city = documentSnapshot.getString("city");
                    updateUI();

                }
            });
        }



        //Click Listners
        actvBirthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDate();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();

            }
        });

        actvAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(NewProfileActivity.this, SelectLocationActivity.class), 1);

            }
        });

        btnImgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open Gallery Intent
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);
            }
        });


    }

    private void updateUI() {
        tilName.getEditText().setText(name);
        tilPhoneNumber.getEditText().setText(phoneNumber);
        actvAddress.setText(address);
        actvBirthDate.setText(String.valueOf(birthDay)+"/"+String.valueOf(birthMonth)+"/"+String.valueOf(birthYear));
        actvBloodGroup.setText(bloodGroup,false);
        txvEditOrComplete.setText("Edit Your");
        setProfileImage();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                isPictureSelected=true;
                profileImgUri = data.getData();
                imgProfile.setImageURI(profileImgUri);
            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("address");
                city = data.getStringExtra("city");

                actvAddress.setText(result);
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Please selact a Location on Map", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveData() {
        //saving in strings
        name = tilName.getEditText().getText().toString();
        email = mAuth.getCurrentUser().getEmail().toString();
        phoneNumber = tilPhoneNumber.getEditText().getText().toString();
        address = actvAddress.getText().toString();
        String birthdate =  actvBirthDate.getText().toString();
        //gender = tilGender.getText().toString();
        bloodGroup = actvBloodGroup.getText().toString();

        if (TextUtils.isEmpty(name)) {
            tilName.setError("Please enter your name ");
            tilName.requestFocus();
        } else if (TextUtils.isEmpty(phoneNumber)) {
            tilPhoneNumber.setError("Please enter your phone number");
            tilPhoneNumber.requestFocus();
        } else if (TextUtils.isEmpty(address)) {
            tilAddress.setError("Please enter select your location");
            tilAddress.requestFocus();
        }else if (TextUtils.isEmpty(birthdate)) {
            tilAddress.setError("Please enter your birth date");
            tilAddress.requestFocus();
        } else if (TextUtils.isEmpty(bloodGroup)) {
            tilBloodGroup.setError("Please select your blood group");
            tilBloodGroup.requestFocus();
        }else if (activityCode.equals("new") && !isPictureSelected) {
            errorText.setVisibility(View.VISIBLE);
            btnImgSelect.requestFocus();
        } else {




            //Saving in Firebase Database
            uploadProfileImgToStorage(profileImgUri);
            DocumentReference documentReference = db.collection("users").document(userID);

            if (activityCode.equals("new")) {
                Map<String, Object> user = new HashMap<>();
                user.put("name", name);
                user.put("email", email);
                user.put("phoneNumber", phoneNumber);
                user.put("address", address);
                user.put("city", city);
                user.put("gender", gender);
                user.put("bloodGroup", bloodGroup);
                user.put("birthDay", birthDay);
                user.put("birthMonth", birthMonth);
                user.put("birthYear", birthYear);
                user.put("profileImgUri", profileImgUri);
                user.put("numberOfDonations", "-");
                user.put("donorRating", "-");
                user.put("ratingArray", Arrays.asList());
                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Constants.assignBloodGroups(bloodGroup);
                        Toast.makeText(NewProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(NewProfileActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
            else{
                Map<String, Object> user = new HashMap<>();
                user.put("name", name);
                user.put("phoneNumber", phoneNumber);
                user.put("address", address);
                user.put("city", city);
                user.put("bloodGroup", bloodGroup);
                user.put("birthDay", birthDay);
                user.put("birthMonth", birthMonth);
                user.put("birthYear", birthYear);
                documentReference.update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Constants.assignBloodGroups(bloodGroup);
                        Toast.makeText(NewProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(NewProfileActivity.this,MainActivity.class);
                        intent.putExtra("donorId",userID);
                        startActivity(intent);
                        finish();
                    }
                });
                /*Intent intent = new Intent(NewProfileActivity.this,MainActivity.class);
                intent.putExtra("donorId",userID);
                startActivity(intent);
                finish();*/
            }
        }




    }

    public void selectDate(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(NewProfileActivity.this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                birthDay = dayOfMonth;
                birthMonth = month+1;
                birthYear = year;
                actvBirthDate.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));

            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    public void uploadProfileImgToStorage(Uri profileImgUri){

        if (isPictureSelected) {
            StorageReference fileRef = storageRef.child("users/"+userID+"/profile.jpg");
            fileRef.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                }
            });
        }

    }
    public void setProfileImage() {
        StorageReference fileRef = storageRef.child("users/" + userID + "/profile.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgProfile);
                profileImgUri = uri;
                //isPictureSelected = true;
            }
        });
    }



}