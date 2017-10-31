package com.example.sashok.testapplication.view.main.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sashok.testapplication.Abs.AbsUtils;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Comment;
import com.example.sashok.testapplication.view.main.listener.ImageInfoListener;
import com.example.sashok.testapplication.view.main.listener.LoadLoreListener;

import java.util.List;

/**
 * Created by sashok on 27.10.17.
 */

public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private Context mContext;
    private List<Comment> mComments;
    private ImageInfoListener mImageInfoListener;
    private RecyclerView mRecyclerView;
    private boolean loading;
    private int progressPos;
    private boolean isLastPage;
    private LoadLoreListener mLoadLoreListener;

    public CommentAdapter(RecyclerView recyclerView, Context mContext, final List<Comment> mComments) {
        mRecyclerView = recyclerView;
        this.mContext = mContext;
        this.mComments = mComments;
        mImageInfoListener = (ImageInfoListener) mContext;

    }

    public void onSrollChanged() {
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        if (0 == 0) {
            if (!loading && linearLayoutManager.findLastCompletelyVisibleItemPosition() == mComments.size() - 1 && !isLastPage) {
                mLoadLoreListener.onLoadMore();
                loading = true;
                mComments.add(null);
                progressPos = mComments.size() - 1;
                notifyItemInserted(progressPos);
                //mRecyclerView.scrollToPosition(progressPos);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mComments.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;
        if (viewType == VIEW_ITEM) {
            View commentView = inflater.inflate(R.layout.comment_item, parent, false);
            viewHolder = new CommentAdapter.CommentHolder(commentView);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

            viewHolder = new CommentAdapter.ProgressBarViewHolder(v);
        }
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CommentHolder) {
            final Comment comment = mComments.get(position);
            CommentHolder commentHolder = (CommentHolder) holder;
            commentHolder.comment_name.setText(comment.getText());
            commentHolder.date.setText(AbsUtils.getFormatTimeFromSec(comment.getDate(), "dd.MM.yyyy kk:mm"));
            commentHolder.comment_layout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                    alertDialogBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                            .setPositiveButton("delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (mImageInfoListener != null)
                                        mImageInfoListener.onCommentDelete(comment);
                                }
                            })
                            .setTitle("Delete comment?")
                            .create();
                    alertDialogBuilder.show();
                    return false;
                }
            });
        } else {
            ((CommentAdapter.ProgressBarViewHolder) holder).mProgressBar.setIndeterminate(true);
        }

    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public void setLoadLoreListener(LoadLoreListener loadLoreListener) {
        mLoadLoreListener = loadLoreListener;
    }

    public boolean isLoading() {
        return loading;
    }

    public void setLoading(boolean loading) {
        mComments.remove(progressPos);
        notifyItemRemoved(progressPos);
//        if (progressPos == IMAGES_PER_PAGE) progressPos = 0;
//        notifyItemRemoved(progressPos);
        this.loading = loading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
    }

    public static class ProgressBarViewHolder extends RecyclerView.ViewHolder {


        public ProgressBar mProgressBar;
        public LinearLayout container;

        public ProgressBarViewHolder(View v) {
            super(v);
            container = v.findViewById(R.id.container);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);

        }
    }

    public class CommentHolder extends RecyclerView.ViewHolder {
        private TextView comment_name;
        private TextView date;
        private RelativeLayout comment_layout;

        public CommentHolder(View itemView) {
            super(itemView);
            comment_layout = itemView.findViewById(R.id.comment_layout);
            comment_name = itemView.findViewById(R.id.comment_text);
            date = itemView.findViewById(R.id.text_date);
        }
    }
}
