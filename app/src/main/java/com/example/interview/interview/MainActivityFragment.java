package com.example.interview.interview;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.interview.interview.adapters.PhotoAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//https://api.500px.com/v1/photos?feature=fresh_today&sort=created_at&image_size=4&include_store=store_download&include_states=voted&consumer_key=mSDECDmxoEEEw32OgaNxZxhUFuwiZetUaK9xTyTW

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static final String TAG = "MAIN_ACTIVITY_FRAGMENT";
    private RecyclerView mRecyclerView;
    private PhotoAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ImageData> mPhotos = new ArrayList<ImageData>();

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new PhotoAdapter(getActivity());
        makeNetworkCall();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    //Makes network call
    public void makeNetworkCall() {
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse("https://api.500px.com/v1/photos?feature=fresh_today&sort=created_at&page=1&limit=10&image_size=4&include_store=store_download&include_states=voted&consumer_key=mSDECDmxoEEEw32OgaNxZxhUFuwiZetUaK9xTyTW").newBuilder();
        String url = urlBuilder.build().toString();
        Request request = new Request.Builder()
                .url(url)
                .build();
        try {
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("Unexpected code " + response);
                    }
                    String responseData = response.body().string();
                    Log.d(TAG, responseData);
                    JSONObject json;
                    try {
                        json = new JSONObject(responseData);
                        JSONArray photos = json.getJSONArray("photos");
                        JSONObject photo;
                        String imageUrl;
                        String url;
                        for (int i = 0; i < photos.length(); i++) {
                            photo = photos.getJSONObject(i);
                            imageUrl = photo.getString("image_url");
                            url = photo.getString("url");
                            mPhotos.add(new ImageData(imageUrl, url));
                        }
                        mAdapter.setPhotos(mPhotos);
                        notifyAdapter();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Notify adapter of new dataset in main thread
    private void notifyAdapter() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    //Holds image data
    public static class ImageData {
        public String image_url;
        public String url;

        public ImageData(String image_url, String url) {
            this.image_url = image_url;
            this.url = url;
        }
    }
}


