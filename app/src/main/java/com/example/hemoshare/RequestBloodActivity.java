package com.example.hemoshare;

import static com.example.hemoshare.Model.Constants.AB_NEG;
import static com.example.hemoshare.Model.Constants.AB_POS;
import static com.example.hemoshare.Model.Constants.A_NEG;
import static com.example.hemoshare.Model.Constants.A_POS;
import static com.example.hemoshare.Model.Constants.B_NEG;
import static com.example.hemoshare.Model.Constants.B_POS;
import static com.example.hemoshare.Model.Constants.O_NEG;
import static com.example.hemoshare.Model.Constants.O_POS;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hemoshare.Model.NotificationData;
import com.example.hemoshare.Model.PushNotification;
import com.example.hemoshare.api.ApiUtilities;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestBloodActivity extends AppCompatActivity {

    EditText edtName,edtPhoneNo,edtLocation,edtBloodType,edtUrgency,edtNote;
    Button btnRequestBlood;

    String name,phoneNo,location,bloodType,urgency,note,userID;

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_blood);

        //Binding UI
        edtName = findViewById(R.id.edtName);
        edtPhoneNo = findViewById(R.id.edtPhoneNumber);
        edtLocation = findViewById(R.id.edtLocation);
        edtBloodType = findViewById(R.id.edtBloodType);
        edtUrgency = findViewById(R.id.edtUrgency);
        edtNote = findViewById(R.id.edtNote);
        btnRequestBlood = findViewById(R.id.btnRequestBlood);

        //Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        //click listners
        btnRequestBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRequest();



            }
        });
    }

    private void sendNotification(PushNotification notification) {
        ApiUtilities.getClient().sendNotification(notification).enqueue(new Callback<PushNotification>() {
            @Override
            public void onResponse(Call<PushNotification> call, Response<PushNotification> response) {
                if(response.isSuccessful())
                    Toast.makeText(RequestBloodActivity.this, "Notification Send To Users", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(RequestBloodActivity.this,"error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<PushNotification> call, Throwable t) {
                Toast.makeText(RequestBloodActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void saveRequest() {
        name = edtName.getText().toString();
        phoneNo = edtPhoneNo.getText().toString();
        location = edtLocation.getText().toString();
        bloodType = edtBloodType.getText().toString();
        urgency = edtUrgency.getText().toString();
        note = edtNote.getText().toString();

        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("phoneNo",phoneNo);
        request.put("location",location);
        request.put("bloodType",bloodType);
        request.put("urgency",urgency);
        request.put("note",note);

        //Saving in Firebase Database
        DocumentReference documentReference = db.collection("requests").document();
        documentReference.set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                String TOPIC = AB_POS;

                switch (bloodType){
                    case "AB+" :
                        TOPIC = AB_POS;
                        break;
                    case "AB-" :
                        TOPIC = AB_NEG;
                        break;
                    case "O+" :
                        TOPIC = O_POS;
                        break;
                    case "O-" :
                        TOPIC = O_NEG;
                        break;
                    case "A+" :
                        TOPIC = A_POS;
                        break;
                    case "A-" :
                        TOPIC = A_NEG;
                        break;
                    case "B+" :
                        TOPIC = B_POS;
                        break;
                    case "B-" :
                        TOPIC = B_NEG;
                        break;
                }
                //unsubscribing to toipc so that the device that requests blood not get any notification
                FirebaseMessaging.getInstance().unsubscribeFromTopic(TOPIC);

                //Sending Notification
                PushNotification notification = new PushNotification(new NotificationData("Blood Requirement in your Area",name +" needs "+bloodType+" Blood" ),TOPIC);
                sendNotification(notification);
                Toast.makeText(RequestBloodActivity.this, "Request Submitted Successfully", Toast.LENGTH_SHORT).show();

                DocumentReference documentReference = db.collection("users").document(userID);
                FirebaseMessaging.getInstance().subscribeToTopic(TOPIC);


            }
        });
    }
}