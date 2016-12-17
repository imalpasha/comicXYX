package com.app.comic.ui.Model.Receive.Comic;

import com.app.comic.ui.Model.Receive.TBD.LoginReceive;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 10/9/2016.
 */

public class AuthReceive {

    private String status;
    //private String message;
    private String token;
    private String expiry;

    public String getExpiry() {
        return expiry;
    }

    public void setExpiry(String expiry) {
        this.expiry = expiry;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public AuthReceive(AuthReceive data) {
        this.status = data.getStatus();
        //this.message = data.getMessage();
        this.expiry = data.getExpiry();
        this.token = data.getToken();
    }

    /*public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }*/

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
