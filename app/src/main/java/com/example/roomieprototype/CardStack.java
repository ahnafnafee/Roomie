package com.example.roomieprototype;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.example.roomieprototype.messages.Fragments.FragmentMessages;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class CardStack extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public BottomAppBar bottomAppBar;
    BottomNavigationView bottomNavigationView;
    DatabaseReference reference;
    FragmentMessages fragmentMessage = new FragmentMessages();
    private FloatingActionButton mBottomFAB;
    private ArrayList<String> matchList, matchEmailList, swipedRightBy;
    private FragmentMatch fragmentMatch;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference userPicRef;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // firebase storage initiated
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        matchList = getIntent().getStringArrayListExtra("matchList");
        matchEmailList = getIntent().getStringArrayListExtra("matchEmailList");
        swipedRightBy = getIntent().getStringArrayListExtra("swipedRightBy");
        Log.d("TAG", "cardstack" + matchList.toString());
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("matchList", matchList);
        bundle.putStringArrayList("matchEmailList", matchEmailList);
        bundle.putStringArrayList("swipedRightBy", swipedRightBy);
        // set Fragmentclass Arguments
        fragmentMatch = new FragmentMatch();
        fragmentMatch.setArguments(bundle);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_empty);


        // Set initial fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, fragmentMatch).addToBackStack(null).commitAllowingStateLoss();
        }

        mBottomFAB = findViewById(R.id.appbar_fab);
        mBottomFAB.setOnClickListener(this);

        bottomAppBar = findViewById(R.id.bottom_appbar);
        setSupportActionBar(bottomAppBar);

    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

    }

    @Override
    public void onClick(View v) {
        if (v.equals(mBottomFAB)) {
            Log.d("Test: ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
            if (getSupportFragmentManager().getBackStackEntryCount() > 1)
                getSupportFragmentManager().popBackStack();

        }

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();

        if (getSupportFragmentManager().getBackStackEntryCount() < 2) {
            FragmentManager manager = getSupportFragmentManager();
            boolean fragmentPopped = manager.popBackStackImmediate(backStateName, 0);

            if (!fragmentPopped) { //fragment not in back stack, create it.
                FragmentTransaction ft = manager.beginTransaction();
                ft.replace(R.id.container, fragment);
                ft.addToBackStack(backStateName);
                ft.commitAllowingStateLoss();
            }
        }

        Log.d("Test: ", String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.avatar_icon:
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomNavigationDrawerFragment.newInstance();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
//                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.app_bar_msg:
                replaceFragment(fragmentMessage);
                return true;
        }
        return false;
    }

    private void status(String status) {
        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }

}
