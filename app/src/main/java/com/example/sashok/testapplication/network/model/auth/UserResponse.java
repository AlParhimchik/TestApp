package com.example.sashok.testapplication.network.model.auth;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

/**
 * Created by sashok on 29.10.17.
 */
@JsonObject
public class UserResponse {
    public void setLogin(String login) {
        this.login = login;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @JsonField
    private String login;
    @JsonField
    private Integer userId;
    @JsonField
    private String token;


    public String getLogin() {
        return login;
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }
}
