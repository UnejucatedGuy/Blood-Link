package com.example.hemoshare;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;

import org.checkerframework.checker.units.qual.C;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogOut,btnProfile,btnNewProfile,btnRequestBlood;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Binding UI
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
            startActivity(intent);//test

        });

        btnLogOut.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this,LoginActivity.class));
        });
        btnRequestBlood.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RequestBloodActivity.class)));

    }

    @Override
    public void onBackPressed() {

    }

}