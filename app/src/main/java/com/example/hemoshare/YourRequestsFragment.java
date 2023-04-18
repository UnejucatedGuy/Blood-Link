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

import com.example.hemoshare.Adapters.YourRequestAdapter;
import com.example.hemoshare.Models.YourRequestModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class YourRequestsFragment extends Fragment {

    RecyclerView rcvRequests;
    TextView txvEmptyView;
    ArrayList<YourRequestModel> requestsData;
    YourRequestAdapter yourRequestAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_your_requests, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rcvRequests = view.findViewById(R.id.rcvRequests);
        txvEmptyView = view.findViewById(R.id.empty_view);
        requestsData = new ArrayList<>();
        rcvRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvRequests.setHasFixedSize(true);
        yourRequestAdapter = new YourRequestAdapter(requestsData, getContext());
        rcvRequests.setAdapter(yourRequestAdapter);

        //Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        String userID = mAuth.getCurrentUser().getUid();

        db.collection("requests").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                yourRequestAdapter.notifyDataSetChanged();
            }
        });

        db.collection("requests").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && Objects.equals(d.getString("receiverId"), userID)) {
                                YourRequestModel obj = d.toObject(YourRequestModel.class);
                                if (d.getBoolean("isAccepted").booleanValue())
                                    obj.setAccepted(true);
                                requestsData.add(obj);
                            }
                        }
                        yourRequestAdapter.notifyDataSetChanged();//update adapter
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




    }
}