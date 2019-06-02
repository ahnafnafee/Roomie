package com.example.roomieprototype;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.roomieprototype.messages.Model.User;
import com.example.roomieprototype.userimg.ImgRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class UserInfo extends AppCompatActivity {

    private static final String TAG = "UserInfo";
    public ArrayList<String> mImgArr;
    private CircularImageView sClose;
    private String imgStr;
    private ImageView mainDP;
    private String userdpURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        sClose = findViewById(R.id.close_btn);
        sClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Dismissed", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        final String userFullname = this.getIntent().getStringExtra("fullname");
        mainDP = findViewById(R.id.profile_pic);

        final DatabaseReference uList = FirebaseDatabase.getInstance().getReference().child("Users");
        uList.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User cUser = snapshot.getValue(User.class);

                    assert cUser != null;
                    if (cUser.getFullname().equals(userFullname)) {
                        userdpURL = cUser.getImageURL();
                        Glide.with(getApplicationContext()).load(userdpURL).into(mainDP);
                        Log.d("userDP_URL", userdpURL);
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        TextView fullName = findViewById(R.id.user_fullname);
        fullName.setText(userFullname);


        mImgArr = new ArrayList<>();

        for (int x = 0; x < 5; x++) {
            imgStr = "drawable/pic" + (x + 1);
            Log.d("imgStr", imgStr);
            mImgArr.add(imgStr);
        }
        Log.d("mImgArr", String.valueOf(mImgArr));

        initRecyclerView();

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: init recyclerview");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        ImgRecyclerAdapter adapter = new ImgRecyclerAdapter(this, mImgArr);
        recyclerView.setAdapter(adapter);
    }


}
