package com.example.roomieprototype;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomieprototype.imgcards.SliderAdapter;
import com.example.roomieprototype.imgutils.DecodeBitmapTask;
import com.example.roomieprototype.messages.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.ramotion.cardslider.CardSliderLayoutManager;
import com.ramotion.cardslider.CardSnapHelper;

import java.io.InputStream;

public class UserAccount extends AppCompatActivity {

    private final int[] pics = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5};
    private final SliderAdapter sliderAdapter = new SliderAdapter(pics, 5, new OnCardClickListener());
    public User userInfo;
    private CardSliderLayoutManager layoutManger;
    private RecyclerView recyclerView;
    private int currentPosition;
    private DecodeBitmapTask decodeMapBitmapTask;
    private FirebaseUser cUser;
    private CircularImageView userDP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_account);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        cUser = mAuth.getCurrentUser();

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
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();

        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing() && decodeMapBitmapTask != null) {
            decodeMapBitmapTask.cancel(true);
        }
    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[]{R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[]{R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }

        currentPosition = pos;
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

    private class ImageViewFactory implements ViewSwitcher.ViewFactory {
        @Override
        public View makeView() {
            final ImageView imageView = new ImageView(UserAccount.this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            final ViewGroup.LayoutParams lp = new ImageSwitcher.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            imageView.setLayoutParams(lp);

            return imageView;
        }
    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm = (CardSliderLayoutManager) recyclerView.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
                final Intent intent = new Intent(UserAccount.this, DetailsActivity.class);
                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, pics[activeCardPosition % pics.length]);

                final CardView cardView = (CardView) view;
                final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
                final ActivityOptions options = ActivityOptions
                        .makeSceneTransitionAnimation(UserAccount.this, sharedView, "shared");
                startActivity(intent, options.toBundle());
            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }


}