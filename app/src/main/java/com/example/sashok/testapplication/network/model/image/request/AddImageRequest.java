package com.example.sashok.testapplication.network.model.image.request;

import com.bluelinelabs.logansquare.annotation.JsonField;
import com.bluelinelabs.logansquare.annotation.JsonObject;
import com.example.sashok.testapplication.network.model.Request;

/**
 * Created by sashok on 28.10.17.
 */

@JsonObject
public class AddImageRequest extends Request {
    @JsonField
    private String base64Image;
    @JsonField
    private double lat;
    @JsonField
    private double lng;

    public AddImageRequest(String base64Image, int date, double lat, double lng) {
        this.base64Image = base64Image;
        this.lat = lat;
        this.lng = lng;
        this.date = date;
    }

    @JsonField
    private int date;

    public String getBase64Image() {
        return base64Image;
    }

    public void setBase64Image(String base64Image) {
        this.base64Image = base64Image;
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public AddImageRequest() {
    }


}