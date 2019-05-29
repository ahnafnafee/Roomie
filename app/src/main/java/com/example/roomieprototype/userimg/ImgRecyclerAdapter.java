package com.example.roomieprototype.userimg;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.roomieprototype.R;

import java.util.ArrayList;

public class ImgRecyclerAdapter extends RecyclerView.Adapter<ImgRecyclerAdapter.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private ArrayList<String> mImageStr = new ArrayList<>();
    private Context mContext;

    public ImgRecyclerAdapter(Context context, ArrayList<String> imageStr) {
        mImageStr = imageStr;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.img_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        int imageResource = holder.itemView.getContext().getResources().getIdentifier(mImageStr.get(position), null, "com.example.roomieprototype");
        Drawable imgRes = holder.itemView.getContext().getResources().getDrawable(imageResource);

        holder.image.setImageDrawable(imgRes);
    }

    @Override
    public int getItemCount() {
        return mImageStr.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.user_img);
        }
    }
}
