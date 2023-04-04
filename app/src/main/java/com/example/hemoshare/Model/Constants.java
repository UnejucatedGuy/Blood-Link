package com.example.hemoshare.Model;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.hemoshare.MainActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.messaging.FirebaseMessaging;

public class Constants {
    public static final String BASE_URL = "https://fcm.googleapis.com";
    public static final String SERVER_KEY = "AAAAKkSniss:APA91bGlDlfJ3dI2gYtE6-osrVsoyECWw6tgyVe4k2Q8W0JrdFgTBDMMD94o-LmC290kyrIqqCPczOjFHNebTqWBwzq8m7ZzWlYsyBx16TvQYjq04sxawWIpwDHtzM1BAGW4syKU4y52";
    public static final String CONTENT_TYPE = "application/json";
    public static final String AB_POS = "/topics/AB_POS";
    public static final String AB_NEG = "/topics/AB_NEG";
    public static final String O_POS = "/topics/O_POS";
    public static final String O_NEG = "/topics/O_NEG";
    public static final String A_POS = "/topics/A_POS";
    public static final String A_NEG = "/topics/A_NEG";
    public static final String B_POS = "/topics/B_POS";
    public static final String B_NEG = "/topics/B_NEG";
    private static FirebaseFirestore db;



    public static void assignBloodGroups(@NonNull String bloodGroup) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(AB_POS);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(AB_NEG);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(O_POS);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(O_NEG);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(A_POS);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(A_NEG);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(B_POS);
        FirebaseMessaging.getInstance().unsubscribeFromTopic(B_NEG);
        switch (bloodGroup) {
            case "AB+":
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                break;
            case "AB-":
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(AB_NEG);
                break;
            case "O+":
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(O_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(A_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(B_POS);
                break;
            case "O-":
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(AB_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(O_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(O_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(A_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(A_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(B_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(B_NEG);
                break;
            case "A+":
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(A_POS);
                break;
            case "A-":
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(AB_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(A_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(A_NEG);
                break;
            case "B+":
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(B_POS);
                break;
            case "B-":
                FirebaseMessaging.getInstance().subscribeToTopic(AB_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(AB_NEG);
                FirebaseMessaging.getInstance().subscribeToTopic(B_POS);
                FirebaseMessaging.getInstance().subscribeToTopic(B_NEG);
                break;
        }
    }
}
