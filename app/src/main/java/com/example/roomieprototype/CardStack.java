package com.example.roomieprototype;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import link.fls.swipestack.SwipeStack;

public class CardStack extends AppCompatActivity implements SwipeStack.SwipeStackListener, View.OnClickListener {

    public String imgStr;

    private ArrayList<String> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;
    public int count;
    private FloatingActionButton mButtonLeft, mButtonRight, mRewind;
    private DrawerLayout drawerLayout;
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

    public ImageView mClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

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
                        firebaseUsers.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for(QueryDocumentSnapshot document : task.getResult()) {
                                        if(!(user.getEmail().equals(document.getData().get("email").toString()))) {
                                            Integer points = 0;
                                            Integer match = 1;
                                            if (document.getData().get("sleep") != null) {
                                                Log.d("TAG:", document.getId() + " => " + document.getData().get("sleep"));
                                                Log.d("TAG:", document.getId() + " userSleep when compare =  " + userSleep);
                                                Log.d("TAG:", document.getId() + " => " + document.getData().get("clean"));
                                                Log.d("TAG:", document.getId() + " userSleep when compare =  " + userClean);

                                                if ((document.getData().get("Eat").toString().equals(userEat)) && match.equals(1)) {
                                                    match=1;
                                                }
                                                else
                                                    match=0;

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

                                                Log.d("TAG:", document.getId() + " => " + points.toString());

                                            }
                                        }
                                    }
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



        count = 0;

        mSwipeStack = findViewById(R.id.swipeStack);
        mButtonLeft = findViewById(R.id.skip_button);
        mButtonRight = findViewById(R.id.like_button);
        mRewind = findViewById(R.id.rewind_button);

        mButtonLeft.setOnClickListener(this);
        mButtonRight.setOnClickListener(this);
        mRewind.setOnClickListener(this);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

        //mClose = findViewById(R.id.close_button);

        fillWithTestData();

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();
                        if(menuItem.getItemId()==R.id.nav_sign_out) {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(getApplicationContext(),"Successfully logged out.",Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(getBaseContext(), LoginActivity.class);
                            startActivity(myIntent);
                        }
                        else if(menuItem.getItemId()==R.id.nav_profile) {

                        }

                        return true;
                    }
                });
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
        if (v.equals(mButtonLeft)) {
            mSwipeStack.swipeTopViewToLeft();
        } else if (v.equals(mButtonRight)) {
            mSwipeStack.swipeTopViewToRight();
            startActivity(new Intent(CardStack.this, MatchPopUp.class));
        } else if (v.equals(mRewind)) {
            mData.add(imageSwitch());
            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuReset:
                mSwipeStack.resetStack();
                Snackbar.make(mRewind, R.string.stack_reset, Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.menuGitHub:
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://github.com/flschweiger/SwipeStack"));
                startActivity(browserIntent);
                return true;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
