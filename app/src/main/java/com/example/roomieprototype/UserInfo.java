package com.example.roomieprototype;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomieprototype.userimg.ImgRecyclerAdapter;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;

public class UserInfo extends AppCompatActivity {

    private static final String TAG = "UserInfo";
    public ArrayList<String> mImgArr;
    public Bundle uBundle;
    private CircularImageView sClose;
    private String imgStr;
    private ImageView mainDP;

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

        byte[] byteArray = this.getIntent().getByteArrayExtra("maindp");
        Bitmap userDP = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        String userFullname = this.getIntent().getStringExtra("fullname");

        mainDP = findViewById(R.id.profile_pic);
        mainDP.setImageBitmap(userDP);

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
