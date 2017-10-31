package com.example.sashok.testapplication;

import com.example.sashok.testapplication.network.AuthAdapter;
import com.example.sashok.testapplication.network.CommentAdapter;
import com.example.sashok.testapplication.network.ImageAdapter;
import com.example.sashok.testapplication.network.ResponseCallBack;
import com.example.sashok.testapplication.network.model.ResponseConverter;
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
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

/**
 * Created by sashok on 29.10.17.
 */

public class ApiService {
    private static ImageAdapter imageAdapter;
    private static CommentAdapter commentAdapter;
    private static AuthAdapter authAdapter;
    private static Retrofit retrofit;
    private static volatile ApiService sApiService;

    private ApiService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.REST_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        authAdapter = retrofit.create(AuthAdapter.class);
        imageAdapter = retrofit.create(ImageAdapter.class);
        commentAdapter = retrofit.create(CommentAdapter.class);
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

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            getInstance();
        }
        return retrofit;
    }

    public void login(@Body LoginRequest request, final ResponseCallBack<LoginResponse> callBack) {

        authAdapter.login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                LoginResponse loginResponse = response.body();
                if (loginResponse == null) {
                    loginResponse = (LoginResponse) ResponseConverter.convertErrorResponse(response.errorBody(), LoginResponse.class);
                }
                callBack.onResponse(loginResponse);
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }

    public void registration(@Body RegistrationRequest request, final ResponseCallBack<RegistrationResponse> callBack) {
        authAdapter.registration(request).enqueue(new Callback<RegistrationResponse>() {
            @Override
            public void onResponse(Call<RegistrationResponse> call, Response<RegistrationResponse> response) {
                RegistrationResponse registrationResponse = response.body();
                if (registrationResponse == null) {
                    registrationResponse = (RegistrationResponse) ResponseConverter.convertErrorResponse(response.errorBody(), RegistrationResponse.class);
                }
                callBack.onResponse(registrationResponse);
            }

            @Override
            public void onFailure(Call<RegistrationResponse> call, Throwable t) {
                callBack.onError(t);
            }
        });
    }


    public void getImages(int page, final ResponseCallBack<GetImageResponse> responseCallBack) {
        imageAdapter.getImages(page, getAccessToken()).enqueue(new Callback<GetImageResponse>() {
            @Override
            public void onResponse(Call<GetImageResponse> call, Response<GetImageResponse> response) {
                GetImageResponse getImageResponse = response.body();
                if (getImageResponse == null) {
                    getImageResponse = (GetImageResponse) ResponseConverter.convertErrorResponse(response.errorBody(), GetImageResponse.class);
                }
                responseCallBack.onResponse(getImageResponse);
            }

            @Override
            public void onFailure(Call<GetImageResponse> call, Throwable t) {
                responseCallBack.onError(t);
            }
        });
    }

    public void addImage(AddImageRequest request, final ResponseCallBack<AddImageResponse> responseCallBack) {
        imageAdapter.addImage(request, getAccessToken()).enqueue(new Callback<AddImageResponse>() {
            @Override
            public void onResponse(Call<AddImageResponse> call, Response<AddImageResponse> response) {
                AddImageResponse addImageResponse = response.body();
                if (addImageResponse == null) {
                    addImageResponse = (AddImageResponse) ResponseConverter.convertErrorResponse(response.errorBody(), AddImageResponse.class);
                }
                responseCallBack.onResponse(addImageResponse);
            }

            @Override
            public void onFailure(Call<AddImageResponse> call, Throwable t) {
                responseCallBack.onError(t);
            }
        });
    }

    public void deleteImage(int imageId, final ResponseCallBack<DeleteImageResponse> responseCallBack) {
        imageAdapter.deleteImage(imageId, getAccessToken()).enqueue(new Callback<DeleteImageResponse>() {
            @Override
            public void onResponse(Call<DeleteImageResponse> call, Response<DeleteImageResponse> response) {
                DeleteImageResponse deleteImageResponse = response.body();
                if (deleteImageResponse == null) {
                    deleteImageResponse = (DeleteImageResponse) ResponseConverter.convertErrorResponse(response.errorBody(), DeleteImageResponse.class);
                }
                responseCallBack.onResponse(deleteImageResponse);
            }

            @Override
            public void onFailure(Call<DeleteImageResponse> call, Throwable t) {
                responseCallBack.onError(t);
            }
        });
    }

    public void getComments(int imageId, int page, final ResponseCallBack<GetCommentsResponse> responseCallBack) {
        commentAdapter.getComments(imageId, page, getAccessToken()).enqueue(new Callback<GetCommentsResponse>() {
            @Override
            public void onResponse(Call<GetCommentsResponse> call, Response<GetCommentsResponse> response) {
                GetCommentsResponse getCommentsResponse = response.body();
                if (getCommentsResponse == null) {
                    getCommentsResponse = (GetCommentsResponse) ResponseConverter.convertErrorResponse(response.errorBody(), GetCommentsResponse.class);
                }
                responseCallBack.onResponse(getCommentsResponse);
            }

            @Override
            public void onFailure(Call<GetCommentsResponse> call, Throwable t) {
                responseCallBack.onError(t);
            }
        });
    }

    public void addComment(int imageId, AddCommentRequest request, final ResponseCallBack<AddCommentResponse> responseCallBack) {
        commentAdapter.addComment(imageId, request, getAccessToken()).enqueue(new Callback<AddCommentResponse>() {
            @Override
            public void onResponse(Call<AddCommentResponse> call, Response<AddCommentResponse> response) {
                AddCommentResponse addCommentResponse = response.body();
                if (addCommentResponse == null) {
                    addCommentResponse = (AddCommentResponse) ResponseConverter.convertErrorResponse(response.errorBody(), AddCommentResponse.class);
                }
                responseCallBack.onResponse(addCommentResponse);
            }

            @Override
            public void onFailure(Call<AddCommentResponse> call, Throwable t) {
                responseCallBack.onError(t);
            }
        });
    }

    public void deleteComment(int imageId, int commentId, final ResponseCallBack<DeleteCommentResponse> responseCallBack) {
        commentAdapter.deleteComment(imageId, commentId, getAccessToken()).enqueue(new Callback<DeleteCommentResponse>() {
            @Override
            public void onResponse(Call<DeleteCommentResponse> call, Response<DeleteCommentResponse> response) {
                DeleteCommentResponse deleteCommentResponse = response.body();
                if (deleteCommentResponse == null) {
                    deleteCommentResponse = (DeleteCommentResponse) ResponseConverter.convertErrorResponse(response.errorBody(), DeleteCommentResponse.class);
                }
                responseCallBack.onResponse(deleteCommentResponse);
            }

            @Override
            public void onFailure(Call<DeleteCommentResponse> call, Throwable t) {
                responseCallBack.onError(t);
            }
        });
    }

    private String getAccessToken() {
        return SessionManager.getToken(App.getInstance());
    }
}
