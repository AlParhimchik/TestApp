package com.example.sashok.testapplication.network.model.image.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.example.sashok.testapplication.network.model.ObjectResponse;
import com.example.sashok.testapplication.network.model.image.ImageResponse;

import java.io.Serializable;

/**
 * Created by sashok on 28.10.17.
 */

@JsonObject
public class DeleteImageResponse extends ObjectResponse<ImageResponse> implements Serializable {

}