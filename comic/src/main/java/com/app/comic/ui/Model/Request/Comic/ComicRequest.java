package com.app.comic.ui.Model.Request.Comic;

/**
 * Created by Dell on 11/4/2015.
 */
public class ComicRequest {

    /*Local Data Send To Server*/
    String character;
    String level;
    String option;
    String token;

    /*Initiate Class*/
    public ComicRequest() {

    }

    public ComicRequest(ComicRequest data) {

        character = data.getCharacter();
        level = data.getLevel();
        option = data.getOption();
        token = data.getToken();

    }

    public String getCharacter() {
        return character;
    }

    public void setCharacter(String character) {
        this.character = character;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
