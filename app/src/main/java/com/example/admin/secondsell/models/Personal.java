package com.example.admin.secondsell.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Admin on 2017/12/14.
 */

public class Personal {
    @SerializedName("name")
    private String name;

    @SerializedName("email")
    private String email;

    public void setName(String name) {
        this.name = name;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }
}
