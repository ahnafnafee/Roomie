package com.example.roomieprototype;


import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.util.DisplayMetrics;

import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.bottomappbar.BottomAppBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import link.fls.swipestack.SwipeStack;

public class CardStack extends AppCompatActivity implements SwipeStack.SwipeStackListener, View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public String imgStr;
    public int count;
    public ImageView mSkipView, mLikeView;
    public BottomAppBar bottomAppBar;
    BottomNavigationView bottomNavigationView;
    private ArrayList<String> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;
    private FloatingActionButton mBottomFAB;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_empty);


        // firebase database initiated
        db = FirebaseFirestore.getInstance();

        // firebase auth initiated
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // firebase user initiated
        user = mAuth.getCurrentUser();

        firebaseUsers = db.collection("userData");

        ((CollectionReference) firebaseUsers).document(user.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
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
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (!(user.getEmail().equals(document.getData().get("email").toString()))) {
                                            Integer points = 0;
                                            Integer match = 1;
                                            if (document.getData().get("sleep") != null) {

                                                //Matching the eating traits
                                                if ((document.getData().get("Eat").toString().equals(userEat)) && match.equals(1)) {
                                                    match = 1;
                                                } else
                                                    match = 0;

                                                //Matching if they both want dorm or both want suite
                                                if ((document.getData().get("Dorm").toString().equals(userDorm)) && match.equals(1)) {
                                                    match = 1;
                                                } else
                                                    match = 0;

                                                //Matching if only one of them have an apartment or if both don't have an apartment
                                                if ((document.getData().get("Apartment").toString().equals("Yes")) && userApart.equals("Yes") && match.equals(1)) {
                                                    match = 0;
                                                }

                                                //Points for sleeping traits
                                                if ((document.getData().get("sleep").toString().equals("Night Owl")) && userSleep.equals("Night Owl") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("sleep").toString().equals("Depends on the day")) && userSleep.equals("Night Owl") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("sleep").toString().equals("Early Bird")) && userSleep.equals("Night Owl") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("sleep").toString().equals("Night Owl")) && userSleep.equals("Depends on the day") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("sleep").toString().equals("Depends on the day")) && userSleep.equals("Depends on the day") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("sleep").toString().equals("Early Bird")) && userSleep.equals("Depends on the day") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("sleep").toString().equals("Night Owl")) && userSleep.equals("Early Bird") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("sleep").toString().equals("Depends on the day")) && userSleep.equals("Early Bird") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("sleep").toString().equals("Early Bird")) && userSleep.equals("Early Bird") && match.equals(1)) {
                                                    points += 3;
                                                }

                                                //Points for cleaning traits
                                                if ((document.getData().get("clean").toString().equals("Neat Freak")) && userClean.equals("Neat Freak") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("clean").toString().equals("Relatively Neat")) && userClean.equals("Neat Freak") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("clean").toString().equals("Relatively Messy")) && userClean.equals("Neat Freak") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("clean").toString().equals("Messy")) && userClean.equals("Neat Freak") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("clean").toString().equals("Neat Freak")) && userClean.equals("Relatively Neat") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("clean").toString().equals("Relatively Neat")) && userClean.equals("Relatively Neat") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("clean").toString().equals("Relatively Messy")) && userClean.equals("Relatively Neat") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("clean").toString().equals("Messy")) && userClean.equals("Relatively Neat") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("clean").toString().equals("Neat Freak")) && userClean.equals("Relatively Messy") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("clean").toString().equals("Relatively Neat")) && userClean.equals("Relatively Messy") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("clean").toString().equals("Relatively Messy")) && userClean.equals("Relatively Messy") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("clean").toString().equals("Messy")) && userClean.equals("Relatively Messy") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("clean").toString().equals("Neat Freak")) && userClean.equals("Messy") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("clean").toString().equals("Relatively Neat")) && userClean.equals("Messy") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("clean").toString().equals("Relatively Messy")) && userClean.equals("Messy") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("clean").toString().equals("Messy")) && userClean.equals("Messy") && match.equals(1)) {
                                                    points += 4;
                                                }

                                                //points for study traits
                                                if ((document.getData().get("Study").toString().equals("With Music")) && userStudy.equals("With Music") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Study").toString().equals("Quiet")) && userStudy.equals("With Music") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("Study").toString().equals("By Myself")) && userStudy.equals("With Music") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Study").toString().equals("With Other People")) && userStudy.equals("With Music") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Study").toString().equals("With Music")) && userStudy.equals("Quiet") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("Study").toString().equals("Quiet")) && userStudy.equals("Quiet") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Study").toString().equals("By Myself")) && userStudy.equals("Quiet") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Study").toString().equals("With Other People")) && userStudy.equals("Quiet") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Study").toString().equals("With Music")) && userStudy.equals("With Other People") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Study").toString().equals("Quiet")) && userStudy.equals("With Other People") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Study").toString().equals("By Myself")) && userStudy.equals("With Other People") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("Study").toString().equals("With Other People")) && userStudy.equals("With Other People") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Study").toString().equals("With Music")) && userStudy.equals("By Myself") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Study").toString().equals("Quiet")) && userStudy.equals("By Myself") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Study").toString().equals("By Myself")) && userStudy.equals("By Myself") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Study").toString().equals("With Other People")) && userStudy.equals("By Myself") && match.equals(1)) {
                                                    points += 1;
                                                }

                                                //points for social traits
                                                if ((document.getData().get("Social").toString().equals("Party Animal")) && userSocial.equals("Party Animal") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Social").toString().equals("Depends on my mood")) && userSocial.equals("Party Animal") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Social").toString().equals("Couch Potato")) && userSocial.equals("Party Animal") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("Social").toString().equals("Party Animal")) && userSocial.equals("Depends on my mood") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Social").toString().equals("Depends on my mood")) && userSocial.equals("Depends on my mood") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Social").toString().equals("Couch Potato")) && userSocial.equals("Depends on my mood") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Social").toString().equals("Party Animal")) && userSocial.equals("Couch Potato") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("Social").toString().equals("Depends on my mood")) && userSocial.equals("Couch Potato") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Social").toString().equals("Couch Potato")) && userSocial.equals("Couch Potato") && match.equals(1)) {
                                                    points += 3;
                                                }

                                                //points for temperature traits
                                                if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Freezing") && match.equals(1)) {
                                                    points += 5;
                                                } else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Freezing") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Freezing") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Freezing") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Freezing") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Cold") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Cold") && match.equals(1)) {
                                                    points += 5;
                                                } else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Cold") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Cold") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Cold") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Moderate") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Moderate") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Moderate") && match.equals(1)) {
                                                    points += 5;
                                                } else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Moderate") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Moderate") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Warm") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Warm") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Warm") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Warm") && match.equals(1)) {
                                                    points += 5;
                                                } else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Warm") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Temperature").toString().equals("Freezing")) && userSocial.equals("Melting") && match.equals(1)) {
                                                    points += 1;
                                                } else if ((document.getData().get("Temperature").toString().equals("Cold")) && userTemperature.equals("Melting") && match.equals(1)) {
                                                    points += 2;
                                                } else if ((document.getData().get("Temperature").toString().equals("Moderate")) && userTemperature.equals("Melting") && match.equals(1)) {
                                                    points += 3;
                                                } else if ((document.getData().get("Temperature").toString().equals("Warm")) && userTemperature.equals("Melting") && match.equals(1)) {
                                                    points += 4;
                                                } else if ((document.getData().get("Temperature").toString().equals("Melting")) && userTemperature.equals("Melting") && match.equals(1)) {
                                                    points += 5;
                                                }

                                                //Log the total points
                                                Log.d("TAG:", document.getId() + " => " + points.toString());

                                                //Matching if the points exceed 11 points
                                                if (points >= 11)
                                                    match = 1;
                                                else
                                                    match = 0;

                                                //Log if they match
                                                if (match.equals(1))
                                                    Log.d("TAG:", user.getEmail() + " is matched with " + document.getId());

                                            }
                                        }
                                    }
                                } else {
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


        count = 0;

        mSwipeStack = findViewById(R.id.swipeStack);

        mLikeView = findViewById(R.id.like_view);
        mSkipView = findViewById(R.id.skip_view);
        mBottomFAB = findViewById(R.id.appbar_fab);

        mLikeView.setOnClickListener(this);
        mSkipView.setOnClickListener(this);
        mBottomFAB.setOnClickListener(this);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

        fillWithTestData();


        bottomAppBar = findViewById(R.id.bottom_appbar);
        setSupportActionBar(bottomAppBar);

    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipeStack.resetStack();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

    }

    public void fillWithTestData() {
        for (int x = 0; x < 5; x++) {
            imgStr = "drawable/pic" + (x + 1);
            mData.add(imgStr);
        }
    }

    public String imageSwitch() {
        count++;

        if (count == 6) {
            count = 1;
        }
        String uri = "drawable/pic" + count;
        return uri;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mSkipView)) {
            mSwipeStack.swipeTopViewToLeft();
        } else if (v.equals(mLikeView)) {
            mSwipeStack.swipeTopViewToRight();
            DialogFrag.display(getSupportFragmentManager());
//            startActivity(new Intent(CardStack.this, MatchPopUp.class));
        } else if (v.equals(mBottomFAB)) {
            getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, fragmentMatch).commit();
            mData.add(imageSwitch());
            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    FragmentMessages fragmentMessage = new FragmentMessages();
    FragmentMatch fragmentMatch = new FragmentMatch();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.avatar_icon:
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomNavigationDrawerFragment.newInstance();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.app_bar_msg:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, fragmentMessage).commit();
                Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }


    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, "Swiped right",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, "Swiped left",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStackEmpty() {
        Toast.makeText(this, R.string.stack_empty, Toast.LENGTH_SHORT).show();
    }

    public class SwipeStackAdapter extends BaseAdapter {

        private List<String> mData;

        public SwipeStackAdapter(List<String> data) {
            this.mData = data;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.swipe_card, parent, false);
            }

            ImageView imgViewCard = convertView.findViewById(R.id.imgView);

            int imageResource = getResources().getIdentifier(mData.get(position), null, getPackageName());
            Drawable image = getResources().getDrawable(imageResource);

            imgViewCard.setImageDrawable(image);

            return convertView;
        }
    }
}
