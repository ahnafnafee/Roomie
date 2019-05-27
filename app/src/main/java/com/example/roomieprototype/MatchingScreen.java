package com.example.roomieprototype;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MatchingScreen extends AppCompatActivity {

    private Query firebaseUsers;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String userSleep;
    private String userClean;
    private String userEat;
    private String userStudy;
    private String userSocial;
    private String userTemperature;
    private String userApart;
    private String userDorm;
    private ArrayList<String> matchList, matchEmailList, swipedRightBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_screen);

        // firebase database initiated
        db = FirebaseFirestore.getInstance();

        // firebase auth initiated
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // firebase user initiated
        user = mAuth.getCurrentUser();

        firebaseUsers = db.collection("userData");
        matchList = new ArrayList<>();
        matchEmailList = new ArrayList<>();
        swipedRightBy = new ArrayList<>();
        Task task = ((CollectionReference) firebaseUsers).document(user.getEmail()).get();

        task.addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        userSleep = document.getData().get("sleep").toString();
                        Log.d("TAG:", document.getId() + " userSleep =  " + document.getData().get("sleep"));
                        userClean = document.getData().get("clean").toString();
                        userEat = document.getData().get("Eat").toString();
                        userStudy = document.getData().get("Study").toString();
                        userSocial = document.getData().get("Social").toString();
                        userTemperature = document.getData().get("Temperature").toString();
                        userApart = document.getData().get("Apartment").toString();
                        userDorm = document.getData().get("Dorm").toString();
                        Log.d("TAG:", "DocumentSnapshot data: " + document.getData());

                        //Matching the user from the people in the database
                        firebaseUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Integer count = 0;
                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                        if(!(user.getEmail().equals(document.getData().get("email").toString()))) {
                                            Integer points = 0;
                                            Integer match = 1;
                                            if (document.getData().get("sleep") != null) {

                                                //Matching the eating traits
                                                if ((document.getData().get("Eat").toString().equals(userEat)) && match.equals(1)) {
                                                    match = 1;
                                                }
                                                else
                                                    match = 0;

                                                //Matching if they both want dorm or both want suite
                                                if ((document.getData().get("Dorm").toString().equals(userDorm)) && match.equals(1)) {
                                                    match = 1;
                                                }
                                                else
                                                    match = 0;

                                                //Matching if only one of them have an apartment or if both don't have an apartment
                                                if ((document.getData().get("Apartment").toString().equals("Yes")) && userApart.equals("Yes") && match.equals(1)) {
                                                    match = 0;
                                                }

                                                //Points for sleeping traits
                                                if ((document.getData().get("sleep").toString().equals("Night Owl")) && userSleep.equals("Night Owl") && match.equals(1)) {
                                                    points += 3;
                                                }
                                                else if ((document.getData().get("sleep").toString().equals("Depends on the day")) && userSleep.equals("Night Owl") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("sleep").toString().equals("Early Bird")) && userSleep.equals("Night Owl") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("sleep").toString().equals("Night Owl")) && userSleep.equals("Depends on the day") && match.equals(1)) {
                                                    points += 2;
                                                }
                                                else if ((document.getData().get("sleep").toString().equals("Depends on the day")) && userSleep.equals("Depends on the day") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("sleep").toString().equals("Early Bird")) && userSleep.equals("Depends on the day") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("sleep").toString().equals("Night Owl")) && userSleep.equals("Early Bird") && match.equals(1)) {
                                                    points += 1;
                                                }
                                                else if ((document.getData().get("sleep").toString().equals("Depends on the day")) && userSleep.equals("Early Bird") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("sleep").toString().equals("Early Bird")) && userSleep.equals("Early Bird") && match.equals(1)) {
                                                    points+=3;
                                                }

                                                //Points for cleaning traits
                                                if ((document.getData().get("clean").toString().equals("Neat Freak")) && userClean.equals("Neat Freak") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Relatively Neat")) && userClean.equals("Neat Freak") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Relatively Messy")) && userClean.equals("Neat Freak") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Messy")) && userClean.equals("Neat Freak") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Neat Freak")) && userClean.equals("Relatively Neat") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Relatively Neat")) && userClean.equals("Relatively Neat") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Relatively Messy")) && userClean.equals("Relatively Neat") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Messy")) && userClean.equals("Relatively Neat") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Neat Freak")) && userClean.equals("Relatively Messy") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Relatively Neat")) && userClean.equals("Relatively Messy") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Relatively Messy")) && userClean.equals("Relatively Messy") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Messy")) && userClean.equals("Relatively Messy") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Neat Freak")) && userClean.equals("Messy") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Relatively Neat")) && userClean.equals("Messy") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Relatively Messy")) && userClean.equals("Messy") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("clean").toString().equals("Messy")) && userClean.equals("Messy") && match.equals(1)) {
                                                    points+=4;
                                                }

                                                //points for study traits
                                                if ((document.getData().get("Study").toString().equals("With Music")) && userStudy.equals("With Music") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("Quiet")) && userStudy.equals("With Music") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("By Myself")) && userStudy.equals("With Music") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("With Other People")) && userStudy.equals("With Music") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("With Music")) && userStudy.equals("Quiet") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("Quiet")) && userStudy.equals("Quiet") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("By Myself")) && userStudy.equals("Quiet") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("With Other People")) && userStudy.equals("Quiet") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("With Music")) && userStudy.equals("With Other People") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("Quiet")) && userStudy.equals("With Other People") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("By Myself")) && userStudy.equals("With Other People") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("With Other People")) && userStudy.equals("With Other People") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("With Music")) && userStudy.equals("By Myself") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("Quiet")) && userStudy.equals("By Myself") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("By Myself")) && userStudy.equals("By Myself") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Study").toString().equals("With Other People")) && userStudy.equals("By Myself") && match.equals(1)) {
                                                    points+=1;
                                                }

                                                //points for social traits
                                                if ((document.getData().get("Social").toString().equals("Party Animal")) && userSocial.equals("Party Animal") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Social").toString().equals("Depends on my mood")) && userSocial.equals("Party Animal") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Social").toString().equals("Couch Potato")) && userSocial.equals("Party Animal") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("Social").toString().equals("Party Animal")) && userSocial.equals("Depends on my mood") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Social").toString().equals("Depends on my mood")) && userSocial.equals("Depends on my mood") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Social").toString().equals("Couch Potato")) && userSocial.equals("Depends on my mood") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Social").toString().equals("Party Animal")) && userSocial.equals("Couch Potato") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("Social").toString().equals("Depends on my mood")) && userSocial.equals("Couch Potato") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Social").toString().equals("Couch Potato")) && userSocial.equals("Couch Potato") && match.equals(1)) {
                                                    points+=3;
                                                }

                                                //points for temperature traits
                                                if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Freezing") && match.equals(1)) {
                                                    points+=5;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Freezing") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Freezing") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Freezing") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Freezing") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Cold") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Cold") && match.equals(1)) {
                                                    points+=5;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Cold") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Cold") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Cold") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Moderate") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Moderate") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Moderate") && match.equals(1)) {
                                                    points+=5;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Moderate") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Moderate") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Warm") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Warm") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Warm") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Warm") && match.equals(1)) {
                                                    points+=5;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Warm") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Melting") && match.equals(1)) {
                                                    points+=1;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Melting") && match.equals(1)) {
                                                    points+=2;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Melting") && match.equals(1)) {
                                                    points+=3;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Melting") && match.equals(1)) {
                                                    points+=4;
                                                }
                                                else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Melting") && match.equals(1)) {
                                                    points+=5;
                                                }

                                                //Log the total points
                                                Log.d("TAG:", document.getId() + " => " + points.toString());

                                                //Matching if the points exceed 11 points
                                                if (points >= 11)
                                                    match = 1;
                                                else
                                                    match = 0;

                                                //Log if they match
                                                if (match.equals(1)) {
                                                    matchList.add(document.getData().get("fullname").toString());
                                                    matchEmailList.add(document.getData().get("email").toString());
                                                    if(document.getData().containsKey(user.getEmail()))
                                                        swipedRightBy.add(document.getData().get("email").toString());
                                                    count++;
                                                    Log.d("TAG:", user.getEmail() + " is matched with " + document.getId());
                                                }
                                            }
                                        }
                                    }
                                    Intent myIntent2 = new Intent(getBaseContext(), CardStack.class);
                                    myIntent2.putExtra("matchList", matchList);
                                    myIntent2.putExtra("matchEmailList", matchEmailList);
                                    myIntent2.putExtra("swipedRightBy", swipedRightBy);
                                    startActivity(myIntent2);
                                }
                                else {
                                    Log.d("TAG:", "Error getting documents: ", task.getException());
                                }
                            }
                        });
                    } else {
                        Log.d("TAG:", "No such document");
                    }
                } else {
                    Log.d("TAG:", "get failed with ", task.getException());
                }
            }
        });

    }
}
