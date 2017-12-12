package com.example.admin.secondsell.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2017/11/28.
 */

public class Commodity {

    @SerializedName("name")
    private String name;

    @SerializedName("category")
    private String category;

    @SerializedName("image_name")
    private String image_name;

    @SerializedName("user")
    private String user;

    @SerializedName("price")
    private String price;

    @SerializedName("content")
    private String content;

    @SerializedName("place")
    private String place;

    @SerializedName("time")
    private String time;

    @SerializedName("other")
    private String other;

    @SerializedName("url")
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Commodity{" +
                "name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", content='" + content + '\'' +
                ", place='" + place + '\'' +
                ", time='" + time + '\'' +
                ", other='" + other + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}