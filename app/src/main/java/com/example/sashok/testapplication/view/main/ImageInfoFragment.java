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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.sashok.testapplication.Abs.AbsFragment;
import com.example.sashok.testapplication.Abs.AbsUtils;
import com.example.sashok.testapplication.ApiService;
import com.example.sashok.testapplication.App;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Comment;
import com.example.sashok.testapplication.model.Image;
import com.example.sashok.testapplication.model.realm.RealmController;
import com.example.sashok.testapplication.network.model.comment.CommentResponse;
import com.example.sashok.testapplication.network.model.comment.response.GetCommentsResponse;
import com.example.sashok.testapplication.view.main.adapter.CommentAdapter;
import com.example.sashok.testapplication.view.main.listener.ImageInfoListener;

import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mComments = new RealmList<>();
        mImageInfoListener = (ImageInfoListener) getActivity();
        if (getArguments() != null) {
            id = getArguments().getInt(BUNDLE_IMAGE_ID);
        }
        mRealmController = RealmController.with(getActivity());
        mRealmController.getRealm().addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm realm) {
                Log.d("TAG", "gsef");
            }
        });
        loadData();
    }

    private void loadData() {
        mImage = RealmController.with(getActivity()).getImageById(id);
        loadComments();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_info_fragment, container, false);
        init(view);
        setListeners();
        imageDate.setText(AbsUtils.getFormatTimeFromSec(mImage.getDate(), "dd.MM.yyyy"));
        Glide.with(getActivity()).load(mImage.getUrl()).thumbnail(0.1f).error(R.drawable.default_image).dontAnimate().into(mImageView);
        return view;
    }

    public void init(View view) {
        mImageView = view.findViewById(R.id.image_view);
        sendBtn = view.findViewById(R.id.send_btn);
        mEditText = view.findViewById(R.id.edit_text);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        imageDate = view.findViewById(R.id.image_text);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mCommentAdapter = new CommentAdapter(getActivity(), mComments);
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

    public static ImageInfoFragment newInstance(int id) {
        Bundle bundle = new Bundle();
        bundle.putInt(BUNDLE_IMAGE_ID, id);
        ImageInfoFragment fragment = new ImageInfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onUpdateView() {
        getComments();

    }
    public void loadComments(){
        ApiService.getInstance().getComments(id,0).enqueue(new Callback<GetCommentsResponse>() {
            @Override
            public void onResponse(Call<GetCommentsResponse> call, Response<GetCommentsResponse> response) {
                if (response.body()==null) getComments();
                else {
                    for (CommentResponse commentResponse: response.body().getData()
                         ) {
                        Comment comment=new Comment(commentResponse);
                        comment.setImageId(id);
                        RealmController.with(getActivity()).addComment(comment);
                    }
                    getComments();
                }
            }

            @Override
            public void onFailure(Call<GetCommentsResponse> call, Throwable t) {
                getComments();
            }
        });
    }
    public void getComments(){
        mComments.clear();
        mComments.addAll(RealmController.with(getActivity()).getComments(mImage.getID()));
        mCommentAdapter.notifyDataSetChanged();

    }
}
