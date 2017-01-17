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


    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        this.characters = characters;
    }

    private List<Character> characters;

    public class Character{

        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCharacter() {
            return character;
        }

        public void setCharacter(String character) {
            this.character = character;
        }

        public String getCharacter_name() {
            return character_name;
        }

        public void setCharacter_name(String character_name) {
            this.character_name = character_name;
        }

        public String getImage_path() {
            return image_path;
        }

        public void setImage_path(String image_path) {
            this.image_path = image_path;
        }

        private String character;
        private String character_name;
        private String image_path;

    }

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
        this.characters = data.getCharacters();
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
