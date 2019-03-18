package com.example.roomieprototype;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    public ImageView mImage;

    public FloatingActionButton mLike;
    public FloatingActionButton mSkip;

    public int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mImage = findViewById(R.id.people_image);
        mLike = findViewById(R.id.like_button);

        count = 0;

        imageSwitch();
    }

    public void imageSwitch() {

        mLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                count++;
                if (count == 6) {
                    count = 1;
                }

                String uri = "drawable/pic" + count;

                int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                Drawable image = getResources().getDrawable(imageResource);
                mImage.setImageDrawable(image);

            }
        });

//        mSkip.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                count--;
//                if (count == 0) {
//                    count = 5;
//                }
//
//                String uri = "drawable/pic" + count;
//
//                int imageResource = getResources().getIdentifier(uri, null, getPackageName());
//
//                Drawable image = getResources().getDrawable(imageResource);
//                mImage.setImageDrawable(image);
//
//            }
//        });

    }

}
