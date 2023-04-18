package com.example.hemoshare;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnbBottonNavigationBar;


    FirebaseAuth mAuth;
    Button btnLogOut, btnProfile, btnNewProfile, btnRequestBlood;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bnbBottonNavigationBar = findViewById(R.id.bnbBottonNavigationBar);

        replaceFragment(new HomeFragment());

        bnbBottonNavigationBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bnmiHome:
                        replaceFragment(new HomeFragment());
                        break;
                    case R.id.bnmiNewRequest:
                        replaceFragment(new BloodRequestFragment());
                        break;

                }
                return true;
            }
        });

       /* //Binding UI
        btnLogOut=findViewById(R.id.btnLogOut);
        btnProfile = findViewById(R.id.btnProfile);
        btnNewProfile = findViewById(R.id.btnNewProfile);
        btnRequestBlood = findViewById(R.id.btnRequest);

        //Firebase auth
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();




        //Click Listeners
        btnProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("userId",userId);
            startActivity(intent);
        });

        btnNewProfile.setOnClickListener(v -> {
            //startActivity(new Intent(MainActivity.this,NewProfileActivity.class));
            Intent intent = new Intent(MainActivity.this, RequestsActivity.class);
            intent.putExtra("activityCode","new");
            startActivity(intent);//test

        });

        btnLogOut.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        });
        btnRequestBlood.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RequestBloodActivity.class)));

    }
*/
    }
    @Override
    public void onBackPressed() {

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.flFrameLayout,fragment);
        fragmentTransaction.commit();

    }


}