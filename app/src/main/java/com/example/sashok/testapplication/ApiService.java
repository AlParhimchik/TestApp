package com.example.sashok.testapplication;

import com.example.sashok.testapplication.network.AuthAdapter;
import com.example.sashok.testapplication.network.CommentAdapter;
import com.example.sashok.testapplication.network.ImageAdapter;
import com.example.sashok.testapplication.network.model.auth.request.LoginRequest;
import com.example.sashok.testapplication.network.model.auth.request.RegistrationRequest;
import com.example.sashok.testapplication.network.model.auth.response.LoginResponse;
import com.example.sashok.testapplication.network.model.auth.response.RegistrationResponse;
import com.example.sashok.testapplication.network.model.comment.request.AddCommentRequest;
import com.example.sashok.testapplication.network.model.comment.response.AddCommentResponse;
import com.example.sashok.testapplication.network.model.comment.response.DeleteCommentResponse;
import com.example.sashok.testapplication.network.model.comment.response.GetCommentsResponse;
import com.example.sashok.testapplication.network.model.image.request.AddImageRequest;
import com.example.sashok.testapplication.network.model.image.response.AddImageResponse;
import com.example.sashok.testapplication.network.model.image.response.DeleteImageResponse;
import com.example.sashok.testapplication.network.model.image.response.GetImageResponse;
import com.example.sashok.testapplication.utils.SessionManager;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sashok on 29.10.17.
 */

public class ApiService{
    private static ImageAdapter imageAdapter;
    private static CommentAdapter commentAdapter;
    private static AuthAdapter authAdapter;
    private static Retrofit retrofit;
    private static volatile ApiService sApiService;

    private ApiService(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.REST_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authAdapter = retrofit.create(AuthAdapter.class);
        imageAdapter=retrofit.create(ImageAdapter.class);
        commentAdapter=retrofit.create(CommentAdapter.class);
    }

    public static ApiService getInstance() {
        if (sApiService == null) {
            synchronized (ApiService.class) {
                if (sApiService == null) {
                    sApiService = new ApiService();
                }
            }
        }

        return sApiService;
    }

    public Call<LoginResponse> login(@Body LoginRequest request) {

        return authAdapter.login(request);
    }

    public Call<RegistrationResponse> registration(@Body RegistrationRequest request) {
        return authAdapter.registration(request);
    }

    public static Retrofit getRetrofit(){
        if (retrofit==null){
            getInstance();
        }
        return  retrofit;
    }

    public Call<GetImageResponse> getImages( int page) {
        return imageAdapter.getImages(page, getAccessToken());
    }

    public Call<AddImageResponse> addImage(AddImageRequest request) {
        return imageAdapter.addImage(request,getAccessToken());
    }

    public Call<DeleteImageResponse> deleteImage(int imageId) {
        return imageAdapter.deleteImage(imageId,getAccessToken());
    }

    public Call<GetCommentsResponse> getComments(int imageId,  int page) {
        return commentAdapter.getComments(imageId,page,getAccessToken());
    }

    public Call<AddCommentResponse> addComment(int imageId,  AddCommentRequest request) {
        return commentAdapter.addComment(imageId,request,getAccessToken());
    }

    public Call<DeleteCommentResponse> deleteComment( int imageId,  int commentId) {
        return commentAdapter.deleteComment(imageId,commentId,getAccessToken());
    }

    private String getAccessToken(){
        return SessionManager.getToken(App.getInstance());
    }
}
