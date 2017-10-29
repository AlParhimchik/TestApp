package com.example.sashok.testapplication.network.model.image;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;

import java.io.Serializable;

/**
 * Created by sashok on 28.10.17.
 */
@JsonObject
public class ImageResponse implements Serializable {
    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @JsonField
    private int date;
    @JsonField
    private double lng;
    @JsonField
    private double lat;
    @JsonField
    private String url;
    @JsonField
    private int id;
}
