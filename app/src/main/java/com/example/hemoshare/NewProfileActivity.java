package com.example.hemoshare;





import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.hemoshare.Model.Constants;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class NewProfileActivity extends AppCompatActivity {

    EditText edtName,edtEmail,edtPhoneNumber,edtCity,edtGender,edtBloodGroup,edtBirthDate;
    ImageView imgProfile;

    Button btnSave,btnImgSelect;

    String name,email,phoneNo,city,gender, bloodGroup,userID;
    Uri profileImgUri;
    int birthDay,birthMonth,birthYear;

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference storageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profile);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference();


        //Binding UI
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        edtCity = findViewById(R.id.edtCity);
        edtGender = findViewById(R.id.edtGender);
        edtBirthDate = findViewById(R.id.edtBirthDate);
        edtBloodGroup = findViewById(R.id.edtBloodGroup);
        btnSave = findViewById(R.id.btnSave);
        btnImgSelect = findViewById(R.id.btnImgSelect);
        imgProfile = findViewById(R.id.imgProfile);


        //Click Listners
        edtBirthDate.setOnClickListener(new View.OnClickListener() {
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

        btnImgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open Gallery Intent
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery,1000);
            }
        });


    }

   /* public void assignBloodGroups(String bloodGroup) {
        unsubscribeAllTopics();
        switch (bloodGroup){
            case "AB+" :
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                return;
            case "AB-" :
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(AB_NEG);
                return;
            case "O+" :
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(O_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(A_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(B_POS);
                return;
            case "O-" :
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(AB_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(O_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(O_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(A_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(A_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(B_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(B_NEG);
                return;
            case "A+" :
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(A_POS);
                return;
            case "A-" :
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(AB_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(A_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(A_NEG);
                return;
            case "B+" :
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(B_POS);
                return;
            case "B-" :
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(AB_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(B_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(B_NEG);
                return;
        }
    }

    private void unsubscribeAllTopics() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(AB_POS);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(AB_NEG);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(O_POS);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(O_NEG);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(A_POS);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(A_NEG);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(B_POS);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(B_NEG);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode== Activity.RESULT_OK){
                profileImgUri = data.getData();
                imgProfile.setImageURI(profileImgUri);
            }
        }
    }

    private void saveData() {
        //saving in strings
        name = edtName.getText().toString();
        email = edtEmail.getText().toString();
        phoneNo = edtPhoneNumber.getText().toString();
        city = edtCity.getText().toString();
        gender = edtGender.getText().toString();
        bloodGroup = edtBloodGroup.getText().toString();

        Map<String, Object> user = new HashMap<>();
        user.put("name", name);
        user.put("email", email);
        user.put("phoneNo", phoneNo);
        user.put("city", city);
        user.put("gender", gender);
        user.put("bloodGroup", bloodGroup);
        user.put("birthDay", birthDay);
        user.put("birthMonth", birthMonth);
        user.put("birthYear", birthYear);
        user.put("profileImgUri",profileImgUri);


        //Saving in Firebase Database
        uploadProfileImgToStorage(profileImgUri);
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Constants.assignBloodGroups(bloodGroup);
                Toast.makeText(NewProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(NewProfileActivity.this,MainActivity.class));
            }
        });




    }

    public void selectDate(){
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(NewProfileActivity.this,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                birthDay = dayOfMonth;
                birthMonth = month+1;
                birthYear = year;
                edtBirthDate.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));

            }
        },calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();

    }

    public void uploadProfileImgToStorage(Uri profileImgUri){
        StorageReference fileRef = storageRef.child("users/"+mAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(profileImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            }
        });

    }



}