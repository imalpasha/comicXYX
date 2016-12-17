package com.app.comic.ui.Model.JSON;

import io.realm.RealmObject;

/**
 * Created by Dell on 8/24/2016.
 */
public class TokenInfoJSON extends RealmObject {

    private String tokenInfo;

    public String getTokenInfo() {
        return tokenInfo;
    }

    public void setTokenInfo(String tokenInfo) {
        this.tokenInfo = tokenInfo;
    }


}
