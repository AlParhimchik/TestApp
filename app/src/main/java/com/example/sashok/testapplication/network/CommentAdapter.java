package com.example.sashok.testapplication.network;

import com.example.sashok.testapplication.network.model.comment.request.AddCommentRequest;
import com.example.sashok.testapplication.network.model.comment.response.AddCommentResponse;
import com.example.sashok.testapplication.network.model.comment.response.DeleteCommentResponse;
import com.example.sashok.testapplication.network.model.comment.response.GetCommentsResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.example.sashok.testapplication.network.Constance.COMMENT;
import static com.example.sashok.testapplication.network.Constance.IMAGE;

/**
 * Created by sashok on 26.10.17.
 */

public interface CommentAdapter {
    @GET(IMAGE + "/{imageId}" + COMMENT)
    @Headers("Content-type: application/json")
    Call<GetCommentsResponse> getComments(@Path("imageId") int imageId, @Query("page") int page, @Header("Access-Token") String token);
    @POST(IMAGE + "/{imageId}" + COMMENT)
    @Headers("Content-type: application/json")
    Call<AddCommentResponse> addComment(@Path("imageId") int imageId, @Body AddCommentRequest request, @Header("Access-Token") String token);
    @DELETE(IMAGE + "/{imageId}" + COMMENT + "/{commentId}")
    @Headers("Content-type: application/json")
    Call<DeleteCommentResponse> deleteComment(@Path("imageId") int imageId, @Path("commentId") int commentId, @Header("Access-Token") String token);
}
