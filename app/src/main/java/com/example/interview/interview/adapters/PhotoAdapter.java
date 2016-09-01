package com.example.interview.interview.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.interview.interview.MainActivityFragment;
import com.example.interview.interview.R;

import java.util.ArrayList;

/**
 * Created by interview on 2/18/16.
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
    private ArrayList<MainActivityFragment.ImageData> mPhotos = new ArrayList<MainActivityFragment.ImageData>();
    Context mContext;
    public PhotoAdapter(Context context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {// create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_photo, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position < mPhotos.size()) {
            String url = mPhotos.get(position).image_url;
            Glide.with(mContext).load(url).into(holder.mImage);
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View mView;
        public ImageView mImage;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mImage = (ImageView) mView.findViewById(R.id.childImage);
        }
    }

    public void setPhotos(ArrayList<MainActivityFragment.ImageData> photos){
        mPhotos = photos;
    }
}
