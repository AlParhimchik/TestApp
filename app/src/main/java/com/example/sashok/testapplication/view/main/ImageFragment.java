package com.example.sashok.testapplication.view.main;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sashok.testapplication.Abs.AbsFragment;
import com.example.sashok.testapplication.ApiService;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Image;
import com.example.sashok.testapplication.model.realm.RealmController;
import com.example.sashok.testapplication.network.model.image.ImageResponse;
import com.example.sashok.testapplication.network.model.image.response.GetImageResponse;
import com.example.sashok.testapplication.view.main.adapter.ImageAdapter;
import com.example.sashok.testapplication.view.main.listener.LoadLoreListener;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.sashok.testapplication.network.Constance.IMAGES_PER_PAGE;
import static com.example.sashok.testapplication.view.auth.AuthActivity.TAG;

/**
 * Created by sashok on 27.10.17.
 */

public class ImageFragment extends AbsFragment {
    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Image> mImages;
    public final int VIEW_ITEM = 1;
    public final int VIEW_PROG = 0;
    GridLayoutManager grid;
    private int cur_page;
    private  boolean isRefreshing=false;
    private int images_on_page;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImages = new ArrayList<>();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_fragment, container, false);
        init(view);
        isRefreshing=true;

        mSwipeRefreshLayout.post(new Runnable() {
            @Override public void run() {
                mSwipeRefreshLayout.setRefreshing(isRefreshing);
            }
        });
        if (savedInstanceState == null) {
            cur_page = 0;
            loadImages();
        } else {
            getImages();
        }

        return view;
    }

    public void init(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cur_page = 0;
                images_on_page=0;
                mImageAdapter.setLastPage(false);
                mImages.clear();
                mImageAdapter.notifyDataSetChanged();
                loadImages();
            }
        });
        mRecyclerView = view.findViewById(R.id.recycler_view);
        grid = new GridLayoutManager(getContext(), 3);
        mRecyclerView.setLayoutManager(grid);
        grid.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (mImageAdapter.getItemViewType(position)) {
                    case VIEW_ITEM:
                        return 1;
                    case VIEW_PROG:
                        return grid.getSpanCount();
                    default:
                        return -1;
                }
            }
        });
        mRecyclerView.setHasFixedSize(true);
        mImageAdapter = new ImageAdapter(mRecyclerView, mImages, getActivity());
        mImageAdapter.setLoadLoreListener(new LoadLoreListener() {
            @Override
            public void onLoadMore() {
                loadImages();
            }

        });
        mRecyclerView.setAdapter(mImageAdapter);
    }

    private void getImages() {

        RealmController controller = RealmController.with(getActivity());
        if (mImageAdapter.isLoading()) mImageAdapter.setLoading(false);
        List<Image> new_pagination_list = controller.getImages(cur_page);
        //if add image and  not end
        if (images_on_page!=IMAGES_PER_PAGE){
            new_pagination_list=new_pagination_list.subList(images_on_page,new_pagination_list.size());
        }
        images_on_page = new_pagination_list.size();
        mImages.addAll(new_pagination_list);
        if (images_on_page == IMAGES_PER_PAGE) cur_page++;
        else mImageAdapter.setLastPage(true);
        isRefreshing=false;
        mImageAdapter.notifyItemRangeInserted(mImages.size() - images_on_page, images_on_page);
        if (mSwipeRefreshLayout.isRefreshing()){
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    public static ImageFragment newInstance() {

        ImageFragment fragment = new ImageFragment();

        return fragment;
    }

    private void loadImages() {

        ApiService.getInstance().getImages(cur_page).enqueue(new Callback<GetImageResponse>() {
            @Override
            public void onResponse(Call<GetImageResponse> call, Response<GetImageResponse> response) {

                if (response.errorBody() != null) {
                    getImages();
                } else {
                    if (cur_page == 0) RealmController.with(getActivity()).deleteAll();
                    for (int i = 0; i < response.body().getData().size(); i++) {
                        ImageResponse imageResponse = response.body().getData().get(i);
                        Image image1 = new Image(imageResponse);
                        RealmController.with(getActivity()).addImage(image1);
                    }
                    getImages();
                }
            }

            @Override
            public void onFailure(Call<GetImageResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: ");
//                Toast.makeText(getContext(), "no internet connection", Toast.LENGTH_LONG).show();
                getImages();
            }
        });
    }

    @Override
    public void onUpdateView(Object object) {
        if (object instanceof Image) {
            if (mImages.contains(object)) {
                int index = mImages.indexOf(object);
                mImages.remove(index);
                mImageAdapter.notifyItemRemoved(index);
            } else {
                if (mImageAdapter.isLastPage()==false){
                    images_on_page++;
                    if (images_on_page>20){
                        images_on_page=1;
                        //cur_page++;
                    }
                }
                mImages.add(0, (Image) object);
                mImageAdapter.notifyItemInserted(0);

            }
        }
    }
}

