package com.example.sashok.testapplication.view.main.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.sashok.testapplication.Abs.AbsUtils;
import com.example.sashok.testapplication.R;
import com.example.sashok.testapplication.model.Image;
import com.example.sashok.testapplication.view.main.listener.ImageListener;
import com.example.sashok.testapplication.view.main.listener.LoadLoreListener;

import java.util.List;

import static com.example.sashok.testapplication.network.Constance.IMAGES_PER_PAGE;

/**
 * Created by sashok on 27.10.17.
 */

public class ImageAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Image> mImageList;
    private ImageListener mImageListener;
    private LoadLoreListener mLoadLoreListener;
    private RecyclerView mRecyclerView;
    private boolean loading;
    private  int progressPos;
    private boolean isLastPage;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    public ImageAdapter(RecyclerView recyclerView, final List<Image> mImageList, Context context) {
        this.mRecyclerView = recyclerView;
        this.mImageList = mImageList;
        this.mContext = context;
        mImageListener = (ImageListener) mContext;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView,newState);
                if (newState==RecyclerView.SCROLL_STATE_IDLE){
                    GridLayoutManager gridLayoutManager= (GridLayoutManager) recyclerView.getLayoutManager();
                    if (0==0){
                        if (!loading && gridLayoutManager.findLastCompletelyVisibleItemPosition() == mImageList.size() - 1&&!isLastPage) {
                            mLoadLoreListener.onLoadMore();
                            loading = true;
                            mImageList.add(null);
                            progressPos = mImageList.size() - 1;
                            notifyDataSetChanged();
                            recyclerView.scrollToPosition(progressPos);
                        }
                    }

                }

            }

        });
    }

    @Override
    public int getItemViewType(int position) {
        return mImageList.get(position) == null ? VIEW_PROG : VIEW_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder;
        if (viewType==VIEW_ITEM) {
            View contactView = inflater.inflate(R.layout.image_item, parent, false);

            // Return a new holder instance
            viewHolder = new ImageHolder(contactView);
        }
        else  {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progressbar, parent, false);

             viewHolder = new ProgressBarViewHolder(v);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Image cur_image = mImageList.get(position);
        if (holder instanceof ImageHolder) {
            ImageHolder imageHolder=(ImageHolder)holder;
            imageHolder.image_name.setText(AbsUtils.getFormatTimeFromSec(cur_image.getDate(), "dd.MM.yyyy"));
            Glide.with(mContext).load(cur_image.getUrl()).thumbnail(0.1f).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).error(R.drawable.default_image).into(imageHolder.image);

            imageHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mImageListener.onImageClicked(cur_image);
                }
            });
            imageHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    createConfirmDialog("delete image?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (mImageListener != null)
                                mImageListener.onImageLongClicked(cur_image);
                        }
                    });
                    return true;
                }
            });
        }
        if (holder instanceof ProgressBarViewHolder){

            ((ProgressBarViewHolder) holder).mProgressBar.setIndeterminate(true);
        }


    }

    @Override
    public int getItemCount() {
        return mImageList == null ? 0 : mImageList.size();
    }

    public void setLoadLoreListener(LoadLoreListener loadLoreListener) {
        mLoadLoreListener = loadLoreListener;
    }

    public void setLoading(boolean loading) {
       mImageList.remove(progressPos);
        notifyItemRemoved(progressPos);
//        if (progressPos == IMAGES_PER_PAGE) progressPos = 0;
//        notifyItemRemoved(progressPos);
        this.loading = loading;
    }

    public boolean isLoading(){
        return loading;
    }

    public boolean isLastPage() {
        return isLastPage;
    }

    public void setLastPage(boolean lastPage) {
        isLastPage = lastPage;
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

    public static class ProgressBarViewHolder extends RecyclerView.ViewHolder {


        public ProgressBar mProgressBar;
        public LinearLayout container;

        public ProgressBarViewHolder(View v) {
            super(v);
            container = v.findViewById(R.id.container);
            mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);

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