package com.example.roomieprototype;


import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import static com.google.android.gms.tasks.Tasks.await;


public class CardStack extends AppCompatActivity implements View.OnClickListener, BottomNavigationView.OnNavigationItemSelectedListener {

    public BottomAppBar bottomAppBar;
    BottomNavigationView bottomNavigationView;

    private FloatingActionButton mBottomFAB;
    private ArrayList<String> matchList;
    private FragmentMatch fragobj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        matchList = getIntent().getStringArrayListExtra("matchList");
        Log.d("TAG","cardstack"+matchList.toString());
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("matchList", matchList);
        // set Fragmentclass Arguments
        fragobj = new FragmentMatch();
        fragobj.setArguments(bundle);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.action_empty);

        // Set initial fragment
        if(savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).replace(R.id.container, fragobj).addToBackStack(null).commitAllowingStateLoss();
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

    FragmentMessages fragmentMessage = new FragmentMessages();
    FragmentMatch fragmentMatch = new FragmentMatch();


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.avatar_icon:
                BottomSheetDialogFragment bottomSheetDialogFragment = BottomNavigationDrawerFragment.newInstance();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
//                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.app_bar_msg:
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.fade_in, R.anim.fade_out).add(R.id.container, fragmentMessage).addToBackStack(null).commitAllowingStateLoss();
//                Toast.makeText(this, "Messages", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
}
