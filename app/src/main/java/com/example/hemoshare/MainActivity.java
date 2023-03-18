package com.example.hemoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button btnLogOut,btnProfile,btnNewProfile,btnRequestBlood;

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

        //Click Listeners
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,ProfileActivity.class));
            }
        });

        btnNewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,NewProfileActivity.class));
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
        btnRequestBlood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RequestBloodActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

}