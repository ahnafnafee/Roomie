package com.example.roomieprototype;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import link.fls.swipestack.SwipeStack;

public class CardStack extends AppCompatActivity implements SwipeStack.SwipeStackListener, View.OnClickListener {

    private Button mButtonLeft, mButtonRight;
    private FloatingActionButton mFab;

    private ArrayList<String> mData;
    private SwipeStack mSwipeStack;
    private SwipeStackAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardstack_expt);

        mSwipeStack = findViewById(R.id.swipeStack);
        mButtonLeft = findViewById(R.id.buttonSwipeLeft);
        mButtonRight = findViewById(R.id.buttonSwipeRight);
        mFab = findViewById(R.id.fabAdd);

        mButtonLeft.setOnClickListener(this);
        mButtonRight.setOnClickListener(this);
        mFab.setOnClickListener(this);

        mData = new ArrayList<>();
        mAdapter = new SwipeStackAdapter(mData);
        mSwipeStack.setAdapter(mAdapter);
        mSwipeStack.setListener(this);

        fillWithTestData();
    }

    private void fillWithTestData() {
        for (int x = 0; x < 5; x++) {
            mData.add(getString(R.string.dummy_text) + " " + (x + 1));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(mButtonLeft)) {
            mSwipeStack.swipeTopViewToLeft();
        } else if (v.equals(mButtonRight)) {
            mSwipeStack.swipeTopViewToRight();
        } else if (v.equals(mFab)) {
            mData.add(getString(R.string.dummy_fab));
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuReset:
                mSwipeStack.resetStack();
                Snackbar.make(mFab, R.string.stack_reset, Snackbar.LENGTH_SHORT).show();
                return true;
            case R.id.menuGitHub:
                Intent browserIntent = new Intent(
                        Intent.ACTION_VIEW, Uri.parse("https://github.com/flschweiger/SwipeStack"));
                startActivity(browserIntent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onViewSwipedToRight(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, getString(R.string.view_swiped_right, swipedElement),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onViewSwipedToLeft(int position) {
        String swipedElement = mAdapter.getItem(position);
        Toast.makeText(this, getString(R.string.view_swiped_left, swipedElement),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStackEmpty() {
        Toast.makeText(this, R.string.stack_empty, Toast.LENGTH_SHORT).show();
    }

    public class SwipeStackAdapter extends BaseAdapter {

        private List<String> mData;

        public SwipeStackAdapter(List<String> data) {
            this.mData = data;
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
                convertView = getLayoutInflater().inflate(R.layout.itemspot_expt, parent, false);
            }

            TextView textViewCard = (TextView) convertView.findViewById(R.id.textViewCard);
            textViewCard.setText(mData.get(position));

            return convertView;
        }
    }
}
