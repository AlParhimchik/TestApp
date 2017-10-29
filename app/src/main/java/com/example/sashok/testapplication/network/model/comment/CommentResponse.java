package com.example.sashok.testapplication.network.model.comment;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * Created by sashok on 28.10.17.
 */
@JsonObject
public class CommentResponse implements Serializable {

    @JsonField
    private int date;
    @JsonField
    private String text;
    @JsonField
    private int id;

    public CommentResponse() {
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}