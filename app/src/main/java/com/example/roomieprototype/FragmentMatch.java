package com.example.roomieprototype;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.example.roomieprototype.messages.Model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import link.fls.swipestack.SwipeStack;

public class FragmentMatch extends Fragment implements SwipeStack.SwipeStackListener, View.OnClickListener {

    public String imgStr;
    public ImageView mSkipView, mLikeView, imgView;
    public int count;
    private ArrayList<Drawable> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;
    private ArrayList<String> matchList, matchEmailList, swipedRightBy, swipedRightByID;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference userPicRef;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private Integer i;
    private Integer swipeCount;
    private Integer matchSize;
    private ArrayList<String> imgURL;


    public FragmentMatch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View RootView = inflater.inflate(R.layout.fragment_match, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            matchList = bundle.getStringArrayList("matchList");
            matchEmailList = bundle.getStringArrayList("matchEmailList");
            swipedRightBy = bundle.getStringArrayList("swipedRightBy");
            swipedRightByID = bundle.getStringArrayList("swipedRightByID");
            Log.d("TAG", "fragmentmatch" + matchList.toString());
        }

        db = FirebaseFirestore.getInstance();

        // firebase auth initiated
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        // firebase user initiated
        user = mAuth.getCurrentUser();

        count = 0;
        swipeCount = 0;
        matchSize = matchList.size();
        mSwipeStack = RootView.findViewById(R.id.swipeStack);

        mLikeView = RootView.findViewById(R.id.like_view);
        mSkipView = RootView.findViewById(R.id.skip_view);

        mLikeView.setOnClickListener(this);
        mSkipView.setOnClickListener(this);

        mData = new ArrayList<>();
        imgURL = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData, matchList);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);
        fillWithTestData();
        Log.d("TAG", mData.toString() + "before change");
        // firebase storage initiated
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        i = 2;

        for (int j = 0; j < matchSize; j++) {
            userPicRef = storageReference.child(matchEmailList.get(j));

            userPicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    imgURL.add((String.valueOf(uri)));
                }
            });


            final long ONE_MEGABYTE = 1024 * 1024 * 10;
            final int g = j;
            userPicRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(byte[] bytes) {
                    Drawable image = new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    mData.set(g, image);
                    mAdapter.refresh(mData);
                    mSwipeStack.resetStack();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });


        }


        return RootView;
    }


    public void fillWithTestData() {
        for (int x = 0; x < matchList.size(); x++) {
            Drawable img = ResourcesCompat.getDrawable(getResources(), R.drawable.white_bg, null);
            mData.add(img);
        }
    }


    @Override
    public void onClick(View v) {
        if (v.equals(mSkipView)) {
            mSwipeStack.swipeTopViewToLeft();
        } else if (v.equals(mLikeView)) {
            mSwipeStack.swipeTopViewToRight();
        } else if (v.equals(imgView)) {
            Toast.makeText(getContext(), "Tapped", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onViewSwipedToRight(final int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(getContext(), "Swiped right", Toast.LENGTH_SHORT).show();
        if (swipedRightBy.contains(matchEmailList.get(position))) {
            final DatabaseReference matchChatList = FirebaseDatabase.getInstance().getReference().child("Matched");
            final DatabaseReference uList = FirebaseDatabase.getInstance().getReference().child("Users");
            uList.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User cUser = snapshot.getValue(User.class);

                        for (String uChat : swipedRightBy) {
                            if (cUser.getEmail().equals(uChat)) {
                                matchChatList.child(user.getUid()).child(cUser.getId()).child("id").setValue(user.getUid());
                                matchChatList.child(cUser.getId()).child(user.getUid()).child("id").setValue(cUser.getId());
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DialogFrag.display(getFragmentManager());
        } else {
            Map<String, Object> userRight = new HashMap<>();
            userRight.put(matchEmailList.get(position), matchEmailList.get(position));
            DocumentReference docRef = db.collection("userData").document(user.getEmail());
            docRef.set(userRight, SetOptions.merge());
        }
        //mData.remove(position);
        //matchList.remove(position);
    }

    @Override
    public void onViewSwipedToLeft(final int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(getContext(), "Swiped left", Toast.LENGTH_SHORT).show();
        //mData.remove(position);
        //matchList.remove(position);
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable)drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    @Override
    public void onStackEmpty() {
        Toast.makeText(getContext(), R.string.stack_empty, Toast.LENGTH_SHORT).show();
    }

    public class SwipeStackAdapter extends BaseAdapter {

        private List<Drawable> mPic;
        private List<String> mText;

        public SwipeStackAdapter(List<Drawable> data1, List<String> data2) {
            this.mPic = data1;
            this.mText = data2;
        }

        @Override
        public int getCount() {
            return mText.size();
        }

        @Override
        public String getItem(int position) {
            return mText.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void refresh(List<Drawable> data3) {
            this.mPic = data3;
            notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.swipe_card, parent, false);
            }

            final Bitmap picBitmap = drawableToBitmap(mPic.get(position));
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            picBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            final byte[] byteArray = stream.toByteArray();

            ConstraintLayout userRel = convertView.findViewById(R.id.user_item_rel);
            userRel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getContext(), UserInfo.class);
                    i.putExtra("fullname", mText.get(position));
                    if (picBitmap!=null) {
                        i.putExtra("maindp", byteArray);
                    }
                    startActivity(i);
                }
            });

            ImageView imgViewCard = convertView.findViewById(R.id.imgView);

            TextView textViewCard = convertView.findViewById(R.id.textViewCard);
            textViewCard.setText(mText.get(position));



            imgViewCard.setImageDrawable(mPic.get(position));

            return convertView;
        }
    }
}
