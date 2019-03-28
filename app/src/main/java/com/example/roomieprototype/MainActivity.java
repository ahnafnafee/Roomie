package com.example.roomieprototype;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public ImageView mImage;

    public FloatingActionButton mLike;
    public FloatingActionButton mSkip;

    public int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        mImage = findViewById(R.id.people_image);

        //Button ids
        mLike = findViewById(R.id.like_button);
        mSkip = findViewById(R.id.skip_button);

        count = 0;

        mLike.setOnClickListener(this);
        mSkip.setOnClickListener(this);
    }

    public void imageSwitch() {

        count++;
        if (count == 6) {
            count = 1;
        }

        String uri = "drawable/pic" + count;

        int imageResource = getResources().getIdentifier(uri, null, getPackageName());

        Drawable image = getResources().getDrawable(imageResource);
        mImage.setImageDrawable(image);

    }

    @Override
    public void onClick(View v) {
        if (v.equals(mLike)) {
            imageSwitch();
        } else if (v.equals(mSkip)) {
            Intent cardView = new Intent(getBaseContext(), CardStack.class);
            startActivity(cardView);
        }

    }
}
