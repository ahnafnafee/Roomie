package com.example.roomieprototype;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.sax.RootElement;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import link.fls.swipestack.SwipeStack;

public class FragmentMatch extends Fragment implements SwipeStack.SwipeStackListener, View.OnClickListener {

    public String imgStr;
    private ArrayList<String> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;
    public ImageView mSkipView, mLikeView;
    private FloatingActionButton mBottomFAB;
    private View mAdd;
    public int count;
    private DrawerLayout drawerLayout;
    private ArrayList<String> matchList;

    public FragmentMatch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RootView = inflater.inflate(R.layout.fragment_match, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            matchList = bundle.getStringArrayList("matchList");
            Log.d("TAG","fragmentmatch"+matchList.toString());
        }

        count = 0;
        mSwipeStack = RootView.findViewById(R.id.swipeStack);

        mLikeView = RootView.findViewById(R.id.like_view);
        mSkipView = RootView.findViewById(R.id.skip_view);
        mAdd = RootView.findViewById(R.id.divider);

        mBottomFAB = RootView.findViewById(R.id.appbar_fab);

        mLikeView.setOnClickListener(this);
        mSkipView.setOnClickListener(this);
        mAdd.setOnClickListener(this);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData,matchList);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

        //mClose = findViewById(R.id.close_button);

        fillWithTestData();

        return RootView;
    }

    public void fillWithTestData() {
        for (int x = 0; x < matchList.size(); x++) {
            imgStr = "drawable/pic" + (x + 1);
            mData.add(imgStr);
        }
    }

    public String imageSwitch() {
        count++;

        if (count == matchList.size()+1) {
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
        } else if (v.equals(mAdd)) {
            mData.add(imageSwitch());
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        mSwipeStack.resetStack();
    }

    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(getContext(), "Swiped right", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(getContext(), "Swiped left", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStackEmpty() {
        Toast.makeText(getContext(), R.string.stack_empty, Toast.LENGTH_SHORT).show();
    }

    public class SwipeStackAdapter extends BaseAdapter {

        private List<String> mData;
        private List<String> mText;

        public SwipeStackAdapter(List<String> data1,List<String> data2 ) {
            this.mData = data1;
            this.mText = data2;
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public String getItem(int position) {
            return mData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.swipe_card, parent, false);
            }

            ImageView imgViewCard = convertView.findViewById(R.id.imgView);

            int imageResource = getResources().getIdentifier(mData.get(position), null, "com.example.roomieprototype");
            Drawable image = getResources().getDrawable(imageResource);

            TextView textViewCard = (TextView) convertView.findViewById(R.id.textViewCard);
            textViewCard.setText(matchList.get(position));

            imgViewCard.setImageDrawable(image);

            return convertView;
        }
    }
}
