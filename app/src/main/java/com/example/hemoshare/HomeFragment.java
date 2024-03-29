package com.example.hemoshare;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.hemoshare.Adapters.HomeCardAdapter;
import com.example.hemoshare.Models.HomeCardModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    ImageView imgvProfile, imgvRequests, imgvLogOut;
    MaterialTextView txvName;

    String userId, name, donationCount, donorRating;
    TextView txvDonationCount, txvDonorRatings;

    //ViewPager
    ViewPager vpHomeCards;
    HomeCardAdapter adapter;
    List<HomeCardModel> cardList;

    //FireBase
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    StorageReference storageRef;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imgvProfile = view.findViewById(R.id.imgvProfile);
        imgvRequests = view.findViewById(R.id.imgvRequests);
        imgvLogOut = view.findViewById(R.id.imgvLogOut);
        txvDonationCount = view.findViewById(R.id.txvDonationCount);
        txvDonorRatings = view.findViewById(R.id.txvDonorRatings);
        txvName = view.findViewById(R.id.txvName);
        vpHomeCards = view.findViewById(R.id.vpHomePage);

        //FireBase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser().getUid();
        storageRef = FirebaseStorage.getInstance().getReference();

        //ViewPager

        db.collection("cards").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                List<DocumentSnapshot> list = value.getDocuments();
                cardList = new ArrayList<>();
                for (DocumentSnapshot d : list) {
                    HomeCardModel obj = d.toObject(HomeCardModel.class);
                    cardList.add(obj);
                }
                adapter = new HomeCardAdapter(cardList,getContext());
                vpHomeCards.setAdapter(adapter);
            }
        });

        db.collection("users").document(userId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                name = value.getString("name");
                donationCount = value.getString("numberOfDonations");
                donorRating = value.getString("donorRating");

                txvName.setText(name);
                txvDonationCount.setText(donationCount);
                txvDonorRatings.setText(donorRating);
                setProfileImage();

            }
        });
        /*db.collection("users").document(userId).get(Source.CACHE).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                name = documentSnapshot.getString("name");
                donationCount = documentSnapshot.getString("numberOfDonations");
                donorRating = documentSnapshot.getString("donorRating");

                txvName.setText(name);
                txvDonationCount.setText(donationCount);
                txvDonorRatings.setText(donorRating);
                setProfileImage();

            }
        });*/


        //click Listners
        imgvRequests.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RequestsActivity.class);
                startActivity(intent);
            }
        });
        imgvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
        });
        imgvLogOut.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(getContext(), LoginActivity.class));
        });


    }

    public void setProfileImage() {
        StorageReference fileRef = storageRef.child("users/" + userId + "/profile.jpg");
        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imgvProfile);


            }
        });
    }
}