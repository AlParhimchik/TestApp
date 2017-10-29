package com.example.sashok.testapplication.view.main.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sashok.testapplication.Abs.AbsUtils;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Image;
import com.example.sashok.testapplication.view.main.listener.ImageListener;

import java.util.List;

import static com.example.sashok.testapplication.view.auth.AuthActivity.TAG;

/**
 * Created by sashok on 27.10.17.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageHolder> {

    private Context mContext;
    private List<Image> mImageList;
    private ImageListener mImageListener;

    public ImageAdapter(Context mContext, List<Image> mImageList) {
        this.mContext = mContext;
        this.mImageList = mImageList;
        mImageListener = (ImageListener) mContext;
    }

    @Override
    public ImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.image_item, parent, false);

        // Return a new holder instance
        ImageHolder viewHolder = new ImageHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ImageHolder holder, int position) {
        final Image cur_image = mImageList.get(position);

        holder.image_name.setText(AbsUtils.getFormatTimeFromSec(cur_image.getDate(), "dd.MM.yyyy"));
        Glide.with(mContext).load(cur_image.getUrl()).thumbnail(0.1f).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImageListener.onImageClicked(cur_image);
            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                createConfirmDialog("delete image?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (mImageListener != null) mImageListener.onImageLongClicked(cur_image);
                    }
                });
                return true;
            }
        });

    }

    @Override
    public int getItemCount() {
        return mImageList == null ? 0 : mImageList.size();
    }

    public class ImageHolder extends RecyclerView.ViewHolder {
        private TextView image_name;
        private ImageView image;
        private CardView cardView;

        public ImageHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            image_name = (TextView) itemView.findViewById(R.id.folder_name);
            image = (ImageView) itemView.findViewById(R.id.folder_image);
        }
    }

    public void createConfirmDialog(String title, final DialogInterface.OnClickListener positiveListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle(title)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        positiveListener.onClick(dialogInterface, i);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        builder.show();
    }

}