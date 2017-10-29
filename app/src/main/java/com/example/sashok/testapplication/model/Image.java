package com.example.sashok.testapplication.model;

import com.example.sashok.testapplication.network.model.image.ImageResponse;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sashok on 27.10.17.
 */

public class Image extends RealmObject {
    private String Url;
    private int date;
    private double lat;
    private double lng;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    private int UserId;
    @PrimaryKey
    private int ID;
    private RealmList<Comment> mComments;

    public Image() {
        mComments = new RealmList<>();
    }

    public Image(ImageResponse imageResponse){
        mComments=new RealmList<>();
        this.ID = imageResponse.getId();
        this.lat=imageResponse.getLat();
        this.lng=imageResponse.getLng();
        this.date=imageResponse.getDate();
        this.Url=imageResponse.getUrl();
    }

    public String getUrl() {
        return Url;
    }


    public void setUrl(String url) {
        Url = url;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public RealmList<Comment> getComments() {
        return mComments;
    }

    public void setComment(Comment comment) {
        if (!mComments.contains(comment)) mComments.add(comment);
    }

    public void setComments(RealmList<Comment> comments) {
        this.mComments=comments;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void removeComment(Comment comment) {
        if (mComments.contains(comment)) mComments.remove(comment);
    }

}