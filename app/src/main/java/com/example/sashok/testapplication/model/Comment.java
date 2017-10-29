package com.example.sashok.testapplication.model;

import com.example.sashok.testapplication.network.model.comment.CommentResponse;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sashok on 27.10.17.
 */

public class Comment extends RealmObject {

    private String text;
    private int date;
    @PrimaryKey
    private int ID;
    private int imageId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Comment(CommentResponse commentResponse) {
        this.ID = commentResponse.getId();
        this.text = commentResponse.getText();
        this.date = commentResponse.getDate();

    }

    public Comment() {

    }
}
