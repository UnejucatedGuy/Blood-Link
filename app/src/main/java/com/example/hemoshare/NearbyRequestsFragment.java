package com.example.hemoshare;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hemoshare.Adapters.RequestAdapter;
import com.example.hemoshare.Models.RequestModel;
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


public class NearbyRequestsFragment extends Fragment {

    RecyclerView rcvRequests;
    TextView txvEmptyView;
    ArrayList<RequestModel> requestsData;
    RequestAdapter requestAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvRequests = view.findViewById(R.id.rcvRequests);
        txvEmptyView = view.findViewById(R.id.empty_view);
        requestsData = new ArrayList<>();
        rcvRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvRequests.setHasFixedSize(true);
        requestAdapter = new RequestAdapter(requestsData, getContext());
        rcvRequests.setAdapter(requestAdapter);

        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        db.collection("requests").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                requestAdapter.notifyDataSetChanged();
            }
        });

        DocumentReference documentReference = db.collection("users").document(userID);
        db.collection("requests").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                documentReference.addSnapshotListener((EventListener<DocumentSnapshot>) new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        String bloodGroup = value.getString("bloodGroup");
                        requestsData.clear();
                        if (bloodGroup != null) {
                            switch (bloodGroup) {
                                case "AB+":
                                    db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+")).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d : list) {
                                                        ArrayList list1 = (ArrayList) d.get("declinedBy");
                                                        if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID)) && !d.getString("receiverId").equals(userID)) {
                                                            RequestModel obj = d.toObject(RequestModel.class);
                                                            if(d.getBoolean("isAccepted").booleanValue())
                                                                obj.setAccepted(true);
                                                            requestsData.add(obj);
                                                        }
                                                    }
                                                    requestAdapter.notifyDataSetChanged();//update adapter
                                                    if(requestsData.isEmpty()){
                                                        rcvRequests.setVisibility(View.GONE);
                                                        txvEmptyView.setVisibility(View.VISIBLE);
                                                    }
                                                    else {
                                                        rcvRequests.setVisibility(View.VISIBLE);
                                                        txvEmptyView.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                    break;
                                case "AB-":
                                    db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "AB-")).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d : list) {
                                                        ArrayList list1 = (ArrayList) d.get("declinedBy");
                                                        if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID)) && !d.getString("receiverId").equals(userID)) {
                                                            RequestModel obj = d.toObject(RequestModel.class);
                                                            if(d.getBoolean("isAccepted").booleanValue())
                                                                obj.setAccepted(true);
                                                            requestsData.add(obj);
                                                        }
                                                    }
                                                    requestAdapter.notifyDataSetChanged();//update adapter
                                                    if(requestsData.isEmpty()){
                                                        rcvRequests.setVisibility(View.GONE);
                                                        txvEmptyView.setVisibility(View.VISIBLE);
                                                    }
                                                    else {
                                                        rcvRequests.setVisibility(View.VISIBLE);
                                                        txvEmptyView.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                    break;
                                case "O+":
                                    db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "O+", "A+", "B+")).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d : list) {
                                                        ArrayList list1 = (ArrayList) d.get("declinedBy");
                                                        if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID)) && !d.getString("receiverId").equals(userID)) {
                                                            RequestModel obj = d.toObject(RequestModel.class);
                                                            if(d.getBoolean("isAccepted").booleanValue())
                                                                obj.setAccepted(true);
                                                            requestsData.add(obj);
                                                        }
                                                    }
                                                    requestAdapter.notifyDataSetChanged();//update adapter
                                                    if(requestsData.isEmpty()){
                                                        rcvRequests.setVisibility(View.GONE);
                                                        txvEmptyView.setVisibility(View.VISIBLE);
                                                    }
                                                    else {
                                                        rcvRequests.setVisibility(View.VISIBLE);
                                                        txvEmptyView.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                    break;
                                case "O-":
                                    db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "AB-", "O+", "O-", "A+", "A-", "B+", "B-")).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d : list) {
                                                        ArrayList list1 = (ArrayList) d.get("declinedBy");
                                                        if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID)) && !d.getString("receiverId").equals(userID)) {
                                                            RequestModel obj = d.toObject(RequestModel.class);
                                                            if(d.getBoolean("isAccepted").booleanValue())
                                                                obj.setAccepted(true);
                                                            requestsData.add(obj);
                                                        }
                                                    }
                                                    requestAdapter.notifyDataSetChanged();//update adapter
                                                    if(requestsData.isEmpty()){
                                                        rcvRequests.setVisibility(View.GONE);
                                                        txvEmptyView.setVisibility(View.VISIBLE);
                                                    }
                                                    else {
                                                        rcvRequests.setVisibility(View.VISIBLE);
                                                        txvEmptyView.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                    break;
                                case "A+":
                                    db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "A+")).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d : list) {
                                                        ArrayList list1 = (ArrayList) d.get("declinedBy");
                                                        if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID)) && !d.getString("receiverId").equals(userID)) {
                                                            RequestModel obj = d.toObject(RequestModel.class);
                                                            if(d.getBoolean("isAccepted").booleanValue())
                                                                obj.setAccepted(true);
                                                            requestsData.add(obj);
                                                        }
                                                    }
                                                    requestAdapter.notifyDataSetChanged();//update adapter
                                                    if(requestsData.isEmpty()){
                                                        rcvRequests.setVisibility(View.GONE);
                                                        txvEmptyView.setVisibility(View.VISIBLE);
                                                    }
                                                    else {
                                                        rcvRequests.setVisibility(View.VISIBLE);
                                                        txvEmptyView.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                    break;
                                case "A-":
                                    db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "AB-", "A+", "A-")).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d : list) {
                                                        ArrayList list1 = (ArrayList) d.get("declinedBy");
                                                        if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID)) && !d.getString("receiverId").equals(userID)) {
                                                            RequestModel obj = d.toObject(RequestModel.class);
                                                            if(d.getBoolean("isAccepted").booleanValue())
                                                                obj.setAccepted(true);
                                                            requestsData.add(obj);
                                                        }
                                                    }
                                                    requestAdapter.notifyDataSetChanged();//update adapter
                                                    if(requestsData.isEmpty()){
                                                        rcvRequests.setVisibility(View.GONE);
                                                        txvEmptyView.setVisibility(View.VISIBLE);
                                                    }
                                                    else {
                                                        rcvRequests.setVisibility(View.VISIBLE);
                                                        txvEmptyView.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                    break;
                                case "B+":
                                    db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "B+")).orderBy("time").get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d : list) {
                                                        ArrayList list1 = (ArrayList) d.get("declinedBy");
                                                        if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID)) && !d.getString("receiverId").equals(userID)) {
                                                            RequestModel obj = d.toObject(RequestModel.class);
                                                            if(d.getBoolean("isAccepted").booleanValue())
                                                                obj.setAccepted(true);
                                                            requestsData.add(obj);
                                                        }
                                                    }
                                                    requestAdapter.notifyDataSetChanged();//update adapter
                                                    if(requestsData.isEmpty()){
                                                        rcvRequests.setVisibility(View.GONE);
                                                        txvEmptyView.setVisibility(View.VISIBLE);
                                                    }
                                                    else {
                                                        rcvRequests.setVisibility(View.VISIBLE);
                                                        txvEmptyView.setVisibility(View.GONE);
                                                    }

                                                }
                                            });
                                    break;
                                case "B-":
                                    db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "AB-", "B+", "B-")).get()
                                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                @Override
                                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                                    for (DocumentSnapshot d : list) {
                                                        if (Boolean.FALSE.equals(d.getBoolean("isCompleted"))) {
                                                            ArrayList list1 = (ArrayList) d.get("declinedBy");
                                                            if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID)) && !d.getString("receiverId").equals(userID)) {
                                                                RequestModel obj = d.toObject(RequestModel.class);
                                                                if(d.getBoolean("isAccepted").booleanValue())
                                                                    obj.setAccepted(true);
                                                                requestsData.add(obj);
                                                            }
                                                        }
                                                    }
                                                    requestAdapter.notifyDataSetChanged();//update adapter
                                                    if(requestsData.isEmpty()){
                                                        rcvRequests.setVisibility(View.GONE);
                                                        txvEmptyView.setVisibility(View.VISIBLE);
                                                    }
                                                    else {
                                                        rcvRequests.setVisibility(View.VISIBLE);
                                                        txvEmptyView.setVisibility(View.GONE);
                                                    }
                                                }
                                            });
                                    break;
                            }
                        }



                    }
                });

            }
        });





    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nearby_requests, container, false);


    }
}