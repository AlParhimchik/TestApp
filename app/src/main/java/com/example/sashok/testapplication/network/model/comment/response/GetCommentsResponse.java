package com.example.sashok.testapplication.network.model.comment.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.example.sashok.testapplication.network.model.ObjectResponse;
import com.example.sashok.testapplication.network.model.comment.CommentResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sashok on 28.10.17.
 */

@JsonObject
public class GetCommentsResponse extends ObjectResponse<List<CommentResponse>> implements Serializable {

}
