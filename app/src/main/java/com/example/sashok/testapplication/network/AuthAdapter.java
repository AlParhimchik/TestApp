package com.example.sashok.testapplication.network;


import com.example.sashok.testapplication.network.model.auth.request.LoginRequest;
import com.example.sashok.testapplication.network.model.auth.request.RegistrationRequest;
import com.example.sashok.testapplication.network.model.auth.response.LoginResponse;
import com.example.sashok.testapplication.network.model.auth.response.RegistrationResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

import static com.example.sashok.testapplication.network.Constance.AUTH;

/**
 * Created by sashok on 26.10.17.
 */


public interface AuthAdapter {

    @POST(AUTH + "signin")
    @Headers("Content-type: application/json")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST(AUTH + "signup")
    @Headers("Content-type: application/json; charset=UTF-8")
    Call<RegistrationResponse> registration(@Body RegistrationRequest request);

}
