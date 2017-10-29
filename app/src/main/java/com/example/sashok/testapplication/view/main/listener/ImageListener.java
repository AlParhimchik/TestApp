package com.example.sashok.testapplication.view.main.listener;

import com.example.sashok.testapplication.model.Image;

public interface ImageListener {
    public void onImageClicked(Image image);
    public void onImageAdded(Image image);
    public void onImageLongClicked(Image image);
}