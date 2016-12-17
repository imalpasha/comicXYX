package com.app.comic.ui.Model.JSON;

import io.realm.RealmObject;

/**
 * Created by Dell on 8/24/2016.
 */
public class BookmarkJSON extends RealmObject {

    private String bookmark;

    public String getBookmark() {
        return bookmark;
    }

    public void setBookmark(String bookmark) {
        this.bookmark = bookmark;
    }

}
