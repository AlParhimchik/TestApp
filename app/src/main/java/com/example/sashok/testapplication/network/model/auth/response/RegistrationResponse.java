package com.example.sashok.testapplication.network.model.auth.response;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.example.sashok.testapplication.network.model.ObjectResponse;
import com.example.sashok.testapplication.network.model.auth.UserResponse;

import java.io.Serializable;

/**
 * Created by sashok on 26.10.17.
 */
@JsonObject
public class RegistrationResponse extends ObjectResponse<UserResponse> implements Serializable {

}