package com.app.comic.api;

public class ApiRequestInterceptor {

    private final String apiKey;

    public ApiRequestInterceptor(String apiKey) {
        this.apiKey = apiKey;
    }

   /* @Override
    public void intercept() {
        request.addHeader("Content-Type", "application/json");

        //request.addHeader("X-Mashape-Key", apiKey);
    }*/
}
