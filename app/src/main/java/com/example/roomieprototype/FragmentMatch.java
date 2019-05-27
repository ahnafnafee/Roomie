package com.example.roomieprototype;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import link.fls.swipestack.SwipeStack;

public class FragmentMatch extends Fragment implements SwipeStack.SwipeStackListener, View.OnClickListener {

    public String imgStr;
    private ArrayList<Drawable> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;
    public ImageView mSkipView, mLikeView;
    public int count;
    private ArrayList<String> matchList, matchEmailList;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private StorageReference userPicRef;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private Integer i;
    private Integer swipeCount;
    private Integer matchSize;

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
            Log.d("TAG", "fragmentmatch" + matchList.toString());
        }

        count = 0;
        swipeCount = 0;
        matchSize = matchList.size();
        mSwipeStack = RootView.findViewById(R.id.swipeStack);

        mLikeView = RootView.findViewById(R.id.like_view);
        mSkipView = RootView.findViewById(R.id.skip_view);

        mLikeView.setOnClickListener(this);
        mSkipView.setOnClickListener(this);

        mData = new ArrayList<>();
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
            Log.d("TAG", "0th if statement");
            userPicRef = storageReference.child(matchEmailList.get(j));
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


        //mClose = findViewById(R.id.close_button);

        return RootView;
    }

    public void fillWithTestData() {
        for (int x = 0; x < matchList.size(); x++) {
            imgStr = "drawable/pic1";
            int imageResource = getResources().getIdentifier(imgStr, null, "com.example.roomieprototype");
            Drawable image = getResources().getDrawable(imageResource);
            mData.add(image);
        }
    }

    public String imageSwitch() {
        count++;

        if (count == matchList.size() + 1) {
            count = 1;
        }
        String uri = "drawable/pic" + count;
        return uri;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mSkipView)) {
            mSwipeStack.swipeTopViewToLeft();
        } else if (v.equals(mLikeView)) {
            mSwipeStack.swipeTopViewToRight();
            DialogFrag.display(getFragmentManager());
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipeStack.resetStack();
    }

    @Override
    public void onViewSwipedToRight(final int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(getContext(), "Swiped right", Toast.LENGTH_SHORT).show();
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

            ImageView imgViewCard = convertView.findViewById(R.id.imgView);

            TextView textViewCard = convertView.findViewById(R.id.textViewCard);
            textViewCard.setText(mText.get(position));

            imgViewCard.setImageDrawable(mPic.get(position));

            return convertView;
        }
    }
}
