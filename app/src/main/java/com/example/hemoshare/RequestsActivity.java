package com.example.hemoshare;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;

public class RequestsActivity extends AppCompatActivity {
    /*RecyclerView rcvRequests;
    ArrayList<RequestModel> requestsData;
    RequestAdapter requestAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;*/

    TabLayout tblRequests;
    ViewPager2 vpViewPager;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);

        tblRequests = findViewById(R.id.tblRequests);
        vpViewPager = findViewById(R.id.vpViewPager);

        tblRequests.addTab(tblRequests.newTab().setText("Nearby Requests"));
        tblRequests.addTab(tblRequests.newTab().setText("Your Requests"));

        vpViewPager.setAdapter(new FragmentStateAdapter(this) {
            @Override
            public int getItemCount() {
                return tblRequests.getTabCount();
            }

            @NonNull
            @Override
            public Fragment createFragment(int position) {
                switch (position) {
                    case 0:
                        NearbyRequestsFragment nearbyRequestsFragment = new NearbyRequestsFragment();
                        return nearbyRequestsFragment;

                    case 1:
                        YourRequestsFragment yourRequestsFragment = new YourRequestsFragment();
                        return yourRequestsFragment;

                    default:
                        return null;
                }
            }
        });

        tblRequests.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vpViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tblRequests.getTabAt(position).select();
            }
        });


    }
}




        /*rcvRequests = findViewById(R.id.rcvRequests);
        requestsData = new ArrayList<>();
        rcvRequests.setLayoutManager(new LinearLayoutManager(this));
        requestAdapter = new RequestAdapter(requestsData, RequestsActivity.this);
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
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                String bloodGroup = value.getString("bloodGroup");
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
                                                if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID))) {
                                                    RequestModel obj = d.toObject(RequestModel.class);
                                                    requestsData.add(obj);
                                                }
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
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
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
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
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
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
                                                if (Boolean.FALSE.equals(d.getBoolean("isCompleted")) && !(list1.contains(userID))) {
                                                    RequestModel obj = d.toObject(RequestModel.class);
                                                    requestsData.add(obj);
                                                }
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "A+":
                            db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "A+")).orderBy("time").get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot d : list) {
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "A-":
                            db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "AB-", "A+", "A-")).orderBy("time").get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot d : list) {
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
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
                                                RequestModel obj = d.toObject(RequestModel.class);
                                                requestsData.add(obj);
                                            }
                                            requestAdapter.notifyDataSetChanged();//update adapter
                                        }
                                    });
                            break;
                        case "B-":
                            db.collection("requests").whereIn("bloodGroup", Arrays.asList("AB+", "AB-", "B+", "B-")).orderBy("time").get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                            for (DocumentSnapshot d : list) {
                                                if (Boolean.FALSE.equals(d.getBoolean("isCompleted"))) {
                                                    RequestModel obj = d.toObject(RequestModel.class);
                                                    requestsData.add(obj);
                                                }
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
}*/