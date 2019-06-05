//Name: User Account
//Purpose: This file displays the user’s profile, which includes the user’s information as well as their profile picture.
//Version 5
//Dependencies: Circular Image View

package com.example.roomieprototype;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomieprototype.messages.Model.User;
import com.example.roomieprototype.userimg.ImgRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.InputStream;
import java.util.ArrayList;

public class UserAccount extends AppCompatActivity {

    private static final String TAG = "UserAccount";
    public User userInfo;
    private FirebaseUser cUser;
    private CircularImageView userDP;
    public ArrayList<String> mImgArr;
    private String imgStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        cUser = mAuth.getCurrentUser();

        mImgArr = new ArrayList<>();

        for (int x = 0; x < 5; x++) {
            imgStr = "drawable/p" + (x + 1);
            Log.d("imgStr", imgStr);
            mImgArr.add(imgStr);
        }
        Log.d("mImgArr", String.valueOf(mImgArr));

        initRecyclerView();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        final String userid = cUser.getUid();
        String email = cUser.getEmail();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot snapshot = (DataSnapshot) dataSnapshot.child(userid);
                userInfo = snapshot.getValue(User.class);
                assert userInfo != null;

                userDP = findViewById(R.id.user_dp);
                if (userInfo.getId() != null) {
                    new DownloadImageTask(userDP)
                            .execute(userInfo.getImageURL());
                }

                TextView fullname = findViewById(R.id.account_fullname);
                fullname.setText(userInfo.getFullname().toLowerCase());

                EditText eName = findViewById(R.id.name_edit);
                eName.setText(userInfo.getFullname());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        ImgRecyclerAdapter adapter = new ImgRecyclerAdapter(this, mImgArr);
        recyclerView.setAdapter(adapter);
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }

}