package com.example.hemoshare;

import static com.example.hemoshare.Models.Constants.AB_NEG;
import static com.example.hemoshare.Models.Constants.AB_POS;
import static com.example.hemoshare.Models.Constants.A_NEG;
import static com.example.hemoshare.Models.Constants.A_POS;
import static com.example.hemoshare.Models.Constants.B_NEG;
import static com.example.hemoshare.Models.Constants.B_POS;
import static com.example.hemoshare.Models.Constants.O_NEG;
import static com.example.hemoshare.Models.Constants.O_POS;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hemoshare.Models.Constants;
import com.example.hemoshare.Models.NotificationData;
import com.example.hemoshare.Models.PushNotification;
import com.example.hemoshare.api.ApiUtilities;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Source;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestBloodActivity extends AppCompatActivity {


    TextInputLayout tilName, tilPhoneNumber, tilLocation, tilBloodGroup, tilNote;
    AutoCompleteTextView actvBloodGroup, actvLocation;
    Button btnRequestBlood;
    String[] bloodGroups = new String[]{"AB+", "AB-", "O+", "O-", "A+", "A-", "B+", "B-"};
    String TOPIC = AB_POS;
    ArrayAdapter<String> arrayAdapter;

    String name, phoneNumber, location, bloodGroup, urgency, note, time, userID, requestId, code, city;
    double selectedLat, selectedLng;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    DateFormat formatter = new SimpleDateFormat("h:mm a");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);

        //Binding UI
        tilName = findViewById(R.id.tilName);
        tilPhoneNumber = findViewById(R.id.tilPhoneNumber);
        tilLocation = findViewById(R.id.tilLocation);
        actvBloodGroup = findViewById(R.id.actvBloodGroup);
        actvLocation = findViewById(R.id.actvLocation);
        tilBloodGroup = findViewById(R.id.tilBloodGroup);
        tilNote = findViewById(R.id.tilNote);
        btnRequestBlood = findViewById(R.id.btnRequestBlood);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.item_blood_group, bloodGroups);
        actvBloodGroup.setAdapter(arrayAdapter);


        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        db.collection("users").document(userID).get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name = documentSnapshot.getString("name");
                phoneNumber = documentSnapshot.getString("phoneNumber");
                bloodGroup = documentSnapshot.getString("bloodGroup");
                city = documentSnapshot.getString("city");
                note = "-";
                updateUI();

            }
        });

        //click listners
        btnRequestBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRequest();


            }
        });
        actvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(RequestBloodActivity.this, SelectLocationActivity.class), 1);

            }
        });

    }

    private void updateUI() {
        tilName.getEditText().setText(name);
        tilPhoneNumber.getEditText().setText(phoneNumber);
        actvBloodGroup.setText(bloodGroup, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                String result = data.getStringExtra("address");
                selectedLat = data.getDoubleExtra("lat", 0);
                selectedLng = data.getDoubleExtra("lng", 0);
                actvLocation.setText(result);
            }
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Please selact a Location on Map", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void sendNotification(PushNotification notification) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if (response.isSuccessful()) {
                    assignNotifications();
                    Toast.makeText(RequestBloodActivity.this, "Notification Send To Users", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(RequestBloodActivity.this, "error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(RequestBloodActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void saveRequest() {
        name = tilName.getEditText().getText().toString();
        phoneNumber = tilPhoneNumber.getEditText().getText().toString();
        location = tilLocation.getEditText().getText().toString();
        bloodGroup = actvBloodGroup.getText().toString();
        note = tilNote.getEditText().getText().toString();
        time = formatter.format(new Date());

        if (TextUtils.isEmpty(name)) {
            tilName.setError("Please enter a name ");
            tilName.requestFocus();
        } else if (TextUtils.isEmpty(phoneNumber)) {
            tilPhoneNumber.setError("Please Enter a Phone Number");
            tilPhoneNumber.requestFocus();
        } else if (TextUtils.isEmpty(location)) {
            tilLocation.setError("Please select a Location");
            tilLocation.requestFocus();
        } else if (TextUtils.isEmpty(bloodGroup)) {
            tilBloodGroup.setError("Please Select a Blood Group");
            tilBloodGroup.requestFocus();
        } else {
            generateCode();
            DocumentReference documentReference = db.collection("requests").document();
            requestId = documentReference.getId();
            Map<String, Object> request = new HashMap<>();
            request.put("name", name);
            request.put("donorId", "-");
            request.put("donorName", "-");
            request.put("donorPhoneNumber", "-");
            request.put("phoneNumber", phoneNumber);
            request.put("location", location);
            request.put("city", city);
            request.put("bloodGroup", bloodGroup);
            //request.put("urgency",urgency);
            request.put("note", note);
            request.put("time", time);
            request.put("requestId", requestId);
            request.put("requestLat", selectedLat);
            request.put("requestLng", selectedLng);
            request.put("isAccepted", false);
            request.put("isCompleted", false);
            request.put("code", code);
            request.put("declinedBy", Arrays.asList());
            request.put("receiverId", userID);
            request.put("donorRatings", "-");


            //Saving in Firebase Database

            documentReference.set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {


                    switch (bloodGroup) {
                        case "AB+":
                            TOPIC = AB_POS;
                            break;
                        case "AB-":
                            TOPIC = AB_NEG;
                            break;
                        case "O+":
                            TOPIC = O_POS;
                            break;
                        case "O-":
                            TOPIC = O_NEG;
                            break;
                        case "A+":
                            TOPIC = A_POS;
                            break;
                        case "A-":
                            TOPIC = A_NEG;
                            break;
                        case "B+":
                            TOPIC = B_POS;
                            break;
                        case "B-":
                            TOPIC = B_NEG;
                            break;
                    }
                    //unsubscribing to toipc so that the device that requests blood not get any notification
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //Sending Notification
                            PushNotification notification = new PushNotification(new NotificationData("Blood Requirement in your Area", name + " needs " + bloodGroup + " Blood", requestId), TOPIC);
                            sendNotification(notification);
                            Toast.makeText(RequestBloodActivity.this, "Request Submitted Successfully", Toast.LENGTH_SHORT).show();


                        }
                    });


                }
            });
        }
    }

    private void assignNotifications() {
        //assigning blood groups for notifications

        userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userBloodGroup = documentSnapshot.getString("bloodGroup");
                Constants.assignBloodGroups(userBloodGroup);
                Intent intent = new Intent(RequestBloodActivity.this, WaitingToAcceptActivity.class);
                intent.putExtra("requestId", requestId);
                startActivity(intent);
                finish();
            }
        });

    }

    private void generateCode() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        // this will convert any number sequence into 6 character.
        code = String.format("%06d", number);
    }
}