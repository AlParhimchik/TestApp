package com.example.sashok.testapplication.view.main.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.sashok.testapplication.Abs.AbsUtils;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Comment;
import com.example.sashok.testapplication.view.main.listener.ImageInfoListener;

import java.util.List;

import static com.example.sashok.testapplication.view.auth.AuthActivity.TAG;

/**
 * Created by sashok on 27.10.17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentHolder> {

    private Context mContext;
    private List<Comment> mComments;
    private ImageInfoListener mImageInfoListener;

    public CommentAdapter(Context mContext, List<Comment> mComments) {
        this.mContext = mContext;
        this.mComments = mComments;
        mImageInfoListener = (ImageInfoListener) mContext;
    }

    @Override
    public CommentAdapter.CommentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.comment_item, parent, false);

        // Return a new holder instance
        CommentHolder viewHolder = new CommentHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: ");
        final Comment comment = mComments.get(position);
        holder.comment_name.setText(comment.getText());
        holder.date.setText(AbsUtils.getFormatTimeFromSec(comment.getDate(),"dd.MM.yyyy kk:mm"));
        holder.comment_layout.setOnLongClickListener(new View.OnLongClickListener() {
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

    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "getItemCount: ");
        return mComments.size();
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
