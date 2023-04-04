package com.example.hemoshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textview.MaterialTextView;

import org.checkerframework.checker.units.qual.C;

public class CongratsActivity extends AppCompatActivity {

    String numberOfDonations;
    MaterialTextView txvNumberOfDonations;
    Button btnGoToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_congrats);
        Intent intent = getIntent();
        numberOfDonations = intent.getStringExtra("numberOfDonations");

        txvNumberOfDonations = findViewById(R.id.txvNumberOfDonations);
        btnGoToHome = findViewById(R.id.btnGoToHome);

        txvNumberOfDonations.setText(numberOfDonations);

        //clickListners
        btnGoToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CongratsActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}