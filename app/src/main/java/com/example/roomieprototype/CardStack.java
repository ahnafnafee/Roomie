package com.example.roomieprototype;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import android.util.DisplayMetrics;

import android.util.Log;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomappbar.BottomAppBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import link.fls.swipestack.SwipeStack;

import static com.google.android.gms.tasks.Tasks.await;

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
    private ArrayList<String> matchList;

    public BottomAppBar bottomAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        matchList = getIntent().getStringArrayListExtra("matchList");
        count = 0;

        mSwipeStack = findViewById(R.id.swipeStack);
        mButtonLeft = findViewById(R.id.skip_button);
        mButtonRight = findViewById(R.id.like_button);
        mRewind = findViewById(R.id.rewind_button);

        mButtonLeft.setOnClickListener(this);
        mButtonRight.setOnClickListener(this);
        mRewind.setOnClickListener(this);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData,matchList);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

        //mClose = findViewById(R.id.close_button);

        fillWithTestData();

//        drawerLayout = findViewById(R.id.drawer_layout);
//        ActionBar actionbar = getSupportActionBar();
//        actionbar.setDisplayHomeAsUpEnabled(true);
//        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        bottomAppBar = findViewById(R.id.bottom_appbar);
        setSupportActionBar(bottomAppBar);

//        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(
//                new NavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(MenuItem menuItem) {
//                        // set item as selected to persist highlight
//                        menuItem.setChecked(true);
//                        // close drawer when item is tapped
//                        drawerLayout.closeDrawers();
//                        if (menuItem.getItemId() == R.id.nav_sign_out) {
//                            FirebaseAuth.getInstance().signOut();
//                            Toast.makeText(getApplicationContext(), "Successfully logged out.", Toast.LENGTH_SHORT).show();
//                            Intent myIntent = new Intent(getBaseContext(), LoginActivity.class);
//                            startActivity(myIntent);
//                        } else if (menuItem.getItemId() == R.id.nav_profile) {
//
//                        }
//
//                        return true;
//                    }
//                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipeStack.resetStack();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

    }

    public void fillWithTestData() {
        for (int x = 0; x < matchList.size(); x++) {
            imgStr = "drawable/pic" + (x + 1);
            mData.add(imgStr);
        }
    }

    public String imageSwitch() {
        count++;

        if (count == matchList.size()+1) {
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
            DialogFrag.display(getSupportFragmentManager());
//            startActivity(new Intent(CardStack.this, MatchPopUp.class));
        } else if (v.equals(mRewind)) {
            mData.add(imageSwitch());
            mAdapter.notifyDataSetChanged();
        }


    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.app_bar_msg:
                Toast.makeText(this, "Test 1", Toast.LENGTH_SHORT).show();
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
        private List<String> mText;

        public SwipeStackAdapter(List<String> data1,List<String> data2 ) {
            this.mData = data1;
            this.mText = data2;
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

            TextView textViewCard = (TextView) convertView.findViewById(R.id.textViewCard);
            textViewCard.setText(matchList.get(position));

            imgViewCard.setImageDrawable(image);

            return convertView;
        }
    }
}
