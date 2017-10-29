package com.example.sashok.testapplication.network.model.image.response;

import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.example.sashok.testapplication.network.model.ObjectResponse;
import com.example.sashok.testapplication.network.model.image.ImageResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by sashok on 28.10.17.
 */

@JsonObject
public class GetImageResponse extends ObjectResponse<List<ImageResponse>> implements Serializable {

}
