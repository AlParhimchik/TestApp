package com.example.sashok.testapplication.network.model.auth.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.example.sashok.testapplication.network.model.Request;


/**
 * Created by sashok on 26.10.17.
 */
@JsonObject
public class RegistrationRequest extends Request {
    @JsonField
    private String login;
    @JsonField
    private String password;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
