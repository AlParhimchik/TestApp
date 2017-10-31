package com.example.sashok.testapplication.view.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sashok.testapplication.Abs.AbsFragment;
import com.example.sashok.testapplication.Abs.AbsUtils;
import com.example.sashok.testapplication.ApiService;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Comment;
import com.example.sashok.testapplication.model.Image;
import com.example.sashok.testapplication.model.realm.RealmController;
import com.example.sashok.testapplication.network.ResponseCallBack;
import com.example.sashok.testapplication.network.model.comment.CommentResponse;
import com.example.sashok.testapplication.network.model.comment.response.GetCommentsResponse;
import com.example.sashok.testapplication.view.main.adapter.CommentAdapter;
import com.example.sashok.testapplication.view.main.listener.ImageInfoListener;
import com.example.sashok.testapplication.view.main.listener.LoadLoreListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.sashok.testapplication.network.Constance.COMMENTS_PER_PAGE;

/**
 * Created by sashok on 27.10.17.
 */

public class ImageInfoFragment extends AbsFragment {
    public static String BUNDLE_IMAGE_ID = "IMAGE_ID";
    private Image mImage;
    private int id;
    private ImageView mImageView;
    private ImageView sendBtn;
    private List<Comment> mComments;
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CommentAdapter mCommentAdapter;
    private TextView imageDate;
    private ImageInfoListener mImageInfoListener;
    private RealmController mRealmController;
    private int cur_page;

    public static ImageInfoFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_IMAGE_ID, id);
        ImageInfoFragment fragment = new ImageInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComments = new ArrayList<>();
        mImageInfoListener = (ImageInfoListener) getActivity();
        if (getArguments() != null) {
            id = getArguments().getInt(BUNDLE_IMAGE_ID);
        }
        mRealmController = RealmController.with(getActivity());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_info_fragment, container, false);
        init(view);
        setListeners();
        mImage = RealmController.with(getActivity()).getImageById(id);
        imageDate.setText(new SimpleDateFormat("dd.MM.yyyy").format(new Date(mImage.getDate() * 1000l)));
        Glide.with(getActivity()).load(mImage.getUrl()).centerCrop().thumbnail(0.1f).error(R.drawable.default_image).dontAnimate().into(mImageView);
        if (savedInstanceState == null) {
            cur_page = 0;
            loadComments();
        } else {
            getComments();
        }

        return view;
    }

    public void init(View view) {

        final ScrollView scrollview = view.findViewById(R.id.scroll_view);
        if (scrollview != null)
            scrollview.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                @Override
                public void onScrollChanged() {
                    if (scrollview != null && mRecyclerView.getChildCount() > 0) {
                        if (scrollview.getChildAt(0).getBottom() <= (mRecyclerView.getChildAt(0).getHeight() / 2 + scrollview.getHeight() + scrollview.getScrollY())) {
                            Log.d("TAG", "onScrollChanged: ");
                            mCommentAdapter.onSrollChanged();
                            //scroll view is at bottom
                        }
                    }
                }
            });
        mImageView = view.findViewById(R.id.image_view);
        sendBtn = view.findViewById(R.id.send_btn);
        mEditText = view.findViewById(R.id.edit_text);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        imageDate = view.findViewById(R.id.image_text);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(false);
        mCommentAdapter = new CommentAdapter(mRecyclerView, getActivity(), mComments);
        mCommentAdapter.setLoadLoreListener(new LoadLoreListener() {
            @Override
            public void onLoadMore() {
                loadComments();
            }
        });
        mRecyclerView.setAdapter(mCommentAdapter);
    }

    public void setListeners() {
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mEditText.getText())) {
                    Comment comment = new Comment();
                    comment.setImageId(mImage.getID());
                    comment.setText(mEditText.getText().toString());
                    comment.setDate((int) (Calendar.getInstance().getTimeInMillis() / 1000));
                    AbsUtils.hideKeyboard(mImageView);
                    mEditText.clearFocus();
                    mEditText.setText("");
                    if (mImageInfoListener != null) mImageInfoListener.onCommentAdded(comment);
                }
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealmController.getRealm().removeAllChangeListeners();
    }

    @Override
    public void onUpdateView(Object object) {
        if (object instanceof Comment) {
            if (mComments.contains(object)) {
                int index = mComments.indexOf(object);
                mComments.remove(index);
                mCommentAdapter.notifyItemRemoved(index);
            } else {
                if (mCommentAdapter.isLastPage() == true)
                    mComments.add((Comment) object);
                mCommentAdapter.notifyItemInserted(mComments.size() - 1);
                mRecyclerView.scrollToPosition(mComments.size() - 1);

            }
        }
    }

    public void loadComments() {
        ApiService.getInstance().getComments(id, cur_page, new ResponseCallBack<GetCommentsResponse>() {
            @Override
            public void onResponse(GetCommentsResponse getCommentsResponse) {
                switch (getCommentsResponse.status) {
                    case 200: {
                        if (cur_page == 0) mRealmController.deleteComments(id);
                        for (CommentResponse commentResponse : getCommentsResponse.getData()
                                ) {
                            Comment comment = new Comment(commentResponse);
                            comment.setImageId(id);
                            RealmController.with(getActivity()).addComment(comment);
                        }
                        getComments();
                        break;
                    }
                    default: {
                        getComments();
                    }
                }
            }

            @Override
            public void onError(Throwable t) {
                getComments();
            }
        });
    }

    public void getComments() {
        if (mCommentAdapter.isLoading()) mCommentAdapter.setLoading(false);
        List<Comment> new_comments = RealmController.with(getActivity()).getComments(mImage.getID(), cur_page);
        mComments.addAll(new_comments);
        if (new_comments.size() == COMMENTS_PER_PAGE) {
            cur_page++;
        } else mCommentAdapter.setLastPage(true);
        mCommentAdapter.notifyItemRangeInserted(mComments.size() - new_comments.size(), new_comments.size());


    }
}
