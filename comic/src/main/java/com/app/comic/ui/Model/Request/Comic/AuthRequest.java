package com.app.comic.ui.Model.Request.Comic;

/**
 * Created by Dell on 11/4/2015.
 */
public class AuthRequest {

    /*Local Data Send To Server*/
    String udid;
    String platform;
    String passcode;
    String push_token;

    /*Initiate Class*/
    public AuthRequest() {

    }

    public AuthRequest(AuthRequest data) {

        udid = data.getUdid();
        platform = data.getPlatform();
        passcode = data.getPasscode();
        push_token = data.getPush_token();

    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPasscode() {
        return passcode;
    }

    public void setPasscode(String passcode) {
        this.passcode = passcode;
    }

    public String getPush_token() {
        return push_token;
    }

    public void setPush_token(String push_token) {
        this.push_token = push_token;
    }
}
