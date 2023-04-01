package com.example.hemoshare;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestsActivity extends AppCompatActivity {
    RecyclerView rcvRequests;
   ArrayList<RequestModel> requestsData;
   RequestAdapter requestAdapter;


   FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        rcvRequests = findViewById(R.id.rcvRequests);
        requestsData=new ArrayList<>();
        rcvRequests.setLayoutManager(new LinearLayoutManager(this));
        requestAdapter = new RequestAdapter(requestsData,RequestsActivity.this);
        rcvRequests.setAdapter(requestAdapter);



        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();
        DocumentReference documentReference = db.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String bloodGroup = value.getString("bloodType");
                if (bloodGroup != null) {
                    switch(bloodGroup)
                    {
                        case "AB+":
                            db.collection("requests").whereIn("bloodType", Arrays.asList("AB+")).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for(DocumentSnapshot d:list){
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "AB-":
                            db.collection("requests").whereIn("bloodType",Arrays.asList("AB+","AB-")).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for(DocumentSnapshot d:list){
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "O+":
                            db.collection("requests").whereIn("bloodType",Arrays.asList("AB+","O+","A+","B+")).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for(DocumentSnapshot d:list){
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "O-":
                            db.collection("requests").whereIn("bloodType",Arrays.asList("AB+","AB-","O+","O-","A+","A-","B+","B-")).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for(DocumentSnapshot d:list){
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "A+":
                            db.collection("requests").whereIn("bloodType",Arrays.asList("AB+","A+")).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for(DocumentSnapshot d:list){
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "A-":
                            db.collection("requests").whereIn("bloodType",Arrays.asList("AB+","AB-","A+","A-")).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for(DocumentSnapshot d:list){
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "B+":
                            db.collection("requests").whereIn("bloodType",Arrays.asList("AB+","B+")).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for(DocumentSnapshot d:list){
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "B-":
                            db.collection("requests").whereIn("bloodType",Arrays.asList("AB+","AB-","B+","B-")).get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for(DocumentSnapshot d:list){
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                    }
                }
            }
        });
        





    }
}