package com.app.comic.api;

import com.app.comic.ui.Model.Receive.Comic.AuthReceive;
import com.app.comic.ui.Model.Receive.Comic.ComicReceive;
import com.app.comic.ui.Model.Request.Comic.AuthRequest;

import java.io.IOException;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface ApiService {


    @POST("auth")
    Call<AuthReceive> auth(@Body AuthRequest obj);

    @GET("comic")
    Call<ComicReceive> comic(@QueryMap Map<String, String> params);

    //void onComicRequest(@QueryMap Map<String, String> params, Callback<ComicReceive> callback);

    //comic
/* Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(ApiEndpoint.getUrl())
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    ApiService service = retrofit.create(ApiService.class);

    //@POST("/auth")
    //void onAuthRequest(@Body AuthRequest obj, Callback<AuthReceive> callback);




    //onRetrieveFlightSummary*/

    /*@POST("/auth")
    void onAuthRequest(@Body AuthRequest obj, Callback<AuthReceive> callback);

    //@POST("/comic")
   // void onComicRequest(@Body ComicRequest obj, Callback<ComicReceive> callback);

    @GET("/comic?character={character}&level={level}&option={option}&token={token}")
    void onComicRequest(@Query("character") String character, @Query("level") String level, @Query("option") String option, @Query("token") String token, Callback<ComicReceive> callback);
*/
    //onRetrieveFlightSummary

}


