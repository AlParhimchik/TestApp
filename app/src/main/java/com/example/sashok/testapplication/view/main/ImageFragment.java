package com.example.sashok.testapplication.view.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.sashok.testapplication.Abs.AbsFragment;
import com.example.sashok.testapplication.ApiService;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Image;
import com.example.sashok.testapplication.model.realm.RealmController;
import com.example.sashok.testapplication.network.ResponseCallBack;
import com.example.sashok.testapplication.network.model.image.ImageResponse;
import com.example.sashok.testapplication.network.model.image.response.GetImageResponse;
import com.example.sashok.testapplication.view.main.adapter.ImageAdapter;
import com.example.sashok.testapplication.view.main.listener.LoadLoreListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.sashok.testapplication.network.Constance.IMAGES_PER_PAGE;

/**
 * Created by sashok on 27.10.17.
 */

public class ImageFragment extends AbsFragment {
    public final int VIEW_ITEM = 1;
    public final int VIEW_PROG = 0;
    GridLayoutManager grid;
    private RecyclerView mRecyclerView;
    private ImageAdapter mImageAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Image> mImages;
    private int cur_page;
    private boolean isRefreshing = false;
    private int images_on_page;
    private ProgressBar mProgressBar;

    public static ImageFragment newInstance() {

        ImageFragment fragment = new ImageFragment();

        return fragment;
    }

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
        startLoad();

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
        mProgressBar = view.findViewById(R.id.progress_bar);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                cur_page = 0;
                images_on_page = 0;
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
        if (images_on_page != IMAGES_PER_PAGE) {
            new_pagination_list = new_pagination_list.subList(images_on_page, new_pagination_list.size());
        }
        images_on_page = new_pagination_list.size();
        mImages.addAll(new_pagination_list);
        if (images_on_page == IMAGES_PER_PAGE) cur_page++;
        else mImageAdapter.setLastPage(true);
        mImageAdapter.notifyItemRangeInserted(mImages.size() - images_on_page, images_on_page);
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            });
        }
        finishLoad();
    }

    private void loadImages() {

        ApiService.getInstance().getImages(cur_page, new ResponseCallBack<GetImageResponse>() {
            @Override
            public void onResponse(GetImageResponse getImageResponse) {
                switch (getImageResponse.status) {
                    case 200: {
                        if (cur_page == 0) RealmController.with(getActivity()).deleteAll();
                        for (int i = 0; i < getImageResponse.getData().size(); i++) {
                            ImageResponse imageResponse = getImageResponse.getData().get(i);
                            Image image1 = new Image(imageResponse);
                            RealmController.with(getActivity()).addImage(image1);
                        }
                        getImages();
                        break;
                    }
                    default: {
                        getImages();
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
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
                if (mImageAdapter.isLastPage() == false) {
                    images_on_page++;
                    if (images_on_page > 20) {
                        images_on_page = 1;
                        //cur_page++;
                    }
                }
                mImages.add(0, (Image) object);
                mImageAdapter.notifyItemInserted(0);

            }
        }
    }

    public void startLoad() {
        isRefreshing = false;
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.post(new Runnable() {
            @Override
            public void run() {
                if (isRefreshing) mProgressBar.setVisibility(View.VISIBLE);
            }
        });
        mRecyclerView.setVisibility(View.GONE);
    }

    public void finishLoad() {
        isRefreshing = false;
        mProgressBar.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

}

