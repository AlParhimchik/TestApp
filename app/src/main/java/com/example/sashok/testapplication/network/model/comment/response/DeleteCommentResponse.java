package com.example.sashok.testapplication.network.model.comment.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.example.sashok.testapplication.network.model.ObjectResponse;
import com.example.sashok.testapplication.network.model.comment.CommentResponse;

import java.io.Serializable;

/**
 * Created by sashok on 28.10.17.
 */

@JsonObject
public class DeleteCommentResponse extends ObjectResponse<CommentResponse> implements Serializable {

}