package com.example.sashok.testapplication.network.model.comment.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.example.sashok.testapplication.network.model.Request;

/**
 * Created by sashok on 28.10.17.
 */

@JsonObject
public class AddCommentRequest extends Request {
    @JsonField
    private String text;


    public AddCommentRequest(String text) {
        this.text = text;
    }

    public AddCommentRequest() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}