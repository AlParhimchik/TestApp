package com.example.sashok.testapplication.view.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sashok.testapplication.Abs.AbsFragment;
import com.example.sashok.testapplication.ApiService;
import com.example.sashok.testapplication.App;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Image;
import com.example.sashok.testapplication.model.realm.RealmController;
import com.example.sashok.testapplication.network.model.image.ImageResponse;
import com.example.sashok.testapplication.network.model.image.response.GetImageResponse;
import com.example.sashok.testapplication.view.main.adapter.ImageAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sashok.testapplication.view.auth.AuthActivity.TAG;

/**
 * Created by sashok on 27.10.17.
 */

public class ImageFragment extends AbsFragment {
    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private List<Image> mImages;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImages = new ArrayList<>();
        if (savedInstanceState == null) {
            loadImages();
        } else {
            getImages();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        init(view);
        return view;
    }

    public void init(View view) {
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRecyclerView.setHasFixedSize(true);
        mImageAdapter = new ImageAdapter(getActivity(), mImages);
        mRecyclerView.setAdapter(mImageAdapter);
    }

    private void getImages() {

        RealmController controller = RealmController.with(getActivity());
        mImages.clear();
        mImages.addAll(controller.getImages());
    }

    public static ImageFragment newInstance() {

        ImageFragment fragment = new ImageFragment();

        return fragment;
    }

    private void loadImages() {
        ApiService.getInstance().getImages(0).enqueue(new Callback<GetImageResponse>() {
            @Override
            public void onResponse(Call<GetImageResponse> call, Response<GetImageResponse> response) {

                if (response.errorBody() != null) {
                    getImages();
                } else {
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        Image image1 = new Image();
                        ImageResponse imageResponse = response.body().getData().get(i);
                        image1.setID(imageResponse.getId());
                        image1.setDate(imageResponse.getDate());
                        image1.setUrl(imageResponse.getUrl());
                        image1.setLat(imageResponse.getLat());
                        image1.setLng(imageResponse.getLng());
                        RealmController.with(getActivity()).addImage(image1);

                    }
                    onUpdateView();
                }
            }

            @Override
            public void onFailure(Call<GetImageResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
                getImages();
            }
        });
    }

    @Override
    public void onUpdateView() {
        getImages();
        mImageAdapter.notifyDataSetChanged();
    }
}

