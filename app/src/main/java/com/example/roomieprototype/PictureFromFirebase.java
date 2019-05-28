package com.example.roomieprototype;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PictureFromFirebase extends AppCompatActivity {

    private ArrayList<String> matchList, matchEmailList;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference userPicRef;
    private Bitmap bitmap;
    private Drawable d;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_from_firebase);
        matchList = getIntent().getStringArrayListExtra("matchList");
        matchEmailList = getIntent().getStringArrayListExtra("matchEmailList");
        // firebase storage initiated
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        for (Integer i = 0; i < matchList.size(); i++) {
            userPicRef = storageReference.child(matchEmailList.get(i));
            final long ONE_MEGABYTE = 1024 * 1024;
            userPicRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    // Data for "images/island.jpg" is returns, use this as needed
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
    }
}
