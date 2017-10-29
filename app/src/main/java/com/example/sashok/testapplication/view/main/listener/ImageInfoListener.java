package com.example.sashok.testapplication.view.main.listener;

import com.example.sashok.testapplication.model.Comment;
import com.example.sashok.testapplication.model.Image;

/**
 * Created by sashok on 27.10.17.
 */

public interface ImageInfoListener {
    public void onCommentDelete(Comment comment);

    public void onCommentAdded(Comment comment);

}
