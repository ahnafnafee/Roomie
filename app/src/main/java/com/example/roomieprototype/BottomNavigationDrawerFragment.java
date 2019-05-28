package com.example.roomieprototype;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mikhaellopez.circularimageview.CircularImageView;

public class BottomNavigationDrawerFragment extends BottomSheetDialogFragment {


    private ImageView closeButton;
    //Bottom Sheet Callback
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }

        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            //check the slide offset and change the visibility of close button
            if (slideOffset > 0.5) {
                closeButton.setVisibility(View.VISIBLE);
            } else {
                closeButton.setVisibility(View.GONE);
            }
        }
    };
    private TextView fullName;
    private CircularImageView userImage;
    private FirebaseUser user;

    public static BottomNavigationDrawerFragment newInstance() {

        Bundle args = new Bundle();

        BottomNavigationDrawerFragment fragment = new BottomNavigationDrawerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        //Get the content View
        View contentView = View.inflate(getContext(), R.layout.fragment_bottom_navigation_drawer, null);
        dialog.setContentView(contentView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        fullName = contentView.findViewById(R.id.user_fullname);
        userImage = contentView.findViewById(R.id.user_profile_pic);
        Log.d("TAG", "name" + user.getDisplayName());
        fullName.setText(user.getDisplayName());

        // Reference to an image file in Cloud Storage
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(user.getEmail());

        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Drawable image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                userImage.setImageDrawable(image);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


        NavigationView navigationView = contentView.findViewById(R.id.navigation_view);

        //implement navigation menu item click event
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav1_account:
                        startActivity(new Intent(getContext(), UserAccount.class));
                        Toast.makeText(getContext(), "My Profile", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.nav2_logout:
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(getContext(), "Successfully logged out.", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(getContext(), LoginActivity.class);
                        startActivity(myIntent);
                        getActivity().finish();
                }
                return false;
            }
        });
        closeButton = contentView.findViewById(R.id.close_imageview);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //dismiss bottom sheet
                dismiss();
            }
        });

        //Set the coordinator layout behavior
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        //Set callback
        if (behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

}
