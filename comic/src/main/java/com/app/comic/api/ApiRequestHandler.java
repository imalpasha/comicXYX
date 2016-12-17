package com.app.comic.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.app.comic.ui.Model.Receive.Comic.AuthReceive;
import com.app.comic.ui.Model.Receive.Comic.ComicReceive;
import com.app.comic.ui.Model.Request.Comic.AuthRequest;
import com.app.comic.MainFragmentActivity;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Model.Request.Comic.ComicRequest;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiRequestHandler {

    private final Bus bus;
    ApiService apiService2;
    Context context;
    ProgressDialog mProgressDialog;
    private int inc;
    private boolean retry;


    public ApiRequestHandler(Bus bus, ApiService apiService) {
        this.bus = bus;
        this.apiService2 = apiService;

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiEndpoint.getUrl())
                .client(defaultHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService2 = retrofit.create(ApiService.class);
    }

    /*OkHttpClient okClient = new OkHttpClient();
    okClient.interceptors().add(new Interceptor() {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            // if we needed to refresh oAuth tokens, this is where it would happen

            Request original = chain.request();
            // Customize the request
            Request request = original.newBuilder()
                    .header("Accept", "application/json")
                    .header("Authorization", "auth-token")
                    .method(original.method(), original.body())
                    .build();

            Response response = chain.proceed(request); // currently does nothing
            return response;
        }
    });*/


    // ------------------------------------------------------------------------------ //


        /*OkHttpClient okClient = new OkHttpClient();
        okClient.interceptors().add(new Interceptor() {
            @Override
            public Response intercept(Interceptor.Chain chain) throws IOException {
                // if we needed to refresh oAuth tokens, this is where it would happen
                Response response = chain.proceed(chain.request()); // currently does nothing
                return response;
            }
        });*/

    @Subscribe
    public void onAuthRequest(final AuthRequest event) {
        Log.e("Request Handle", "Handler");

        Call<AuthReceive> call = apiService2.auth(event);
        call.enqueue(new Callback<AuthReceive>() {
            @Override
            public void onResponse(Call<AuthReceive> call, Response<AuthReceive> response) {
                // response.isSuccessful() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    int statusCode = response.code();
                    AuthReceive user = response.body();
                    bus.post(new AuthReceive(user));
                    Log.e("Failed", Integer.toString(statusCode));
                } else {
                    int statusCode = response.code();
                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    Log.e("Failed", Integer.toString(statusCode));
                }
            }

            @Override
            public void onFailure(Call<AuthReceive> call, Throwable t) {
                // handle execution failures like no internet connectivity

            }
        });
    }


    @Subscribe
    public void onComicRequest(final HashMap<String, String> params) {
        Log.e("Request Handle", "Handler");

        Call<ComicReceive> call = apiService2.comic(params);
        call.enqueue(new Callback<ComicReceive>() {
            @Override
            public void onResponse(Call<ComicReceive> call, Response<ComicReceive> response) {
                // response.isSuccessful() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    ComicReceive user = response.body();
                    bus.post(new ComicReceive(user));
                    Log.e("Comic 1", "true");

                } else {
                    int statusCode = response.code();
                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                }
            }

            @Override
            public void onFailure(Call<ComicReceive> call, Throwable t) {
                // handle execution failures like no internet connectivity
            }
        });
    }



   /* @Subscribe
    public void onAuthRequest(final AuthRequest event) {

        apiService.onAuthRequest(event, new Callback<AuthReceive>() {

            @Override
            public void success(AuthReceive retroResponse, Response response) {
                Log.e("www", response.getReason().toString());
                if (retroResponse != null) {
                    bus.post(new AuthReceive(retroResponse));
                    // RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {

                Log.e("error", error.getMessage());
                Log.e("error", error.getResponse().getReason());
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }

    @Subscribe
    public void onComicRequest(final ComicRequest event) {

        apiService.onComicRequest(event.getCharacter(),event.getLevel(),event.getOption(),event.getToken(), new Callback<ComicReceive>() {

            @Override
            public void success(ComicReceive retroResponse, Response response) {
              //  Log.e("www", response.getReason().toString());
                if (retroResponse != null) {
                    bus.post(new ComicReceive(retroResponse));
                    // RealmObjectController.cachedResult(MainFragmentActivity.getContext(), (new Gson()).toJson(retroResponse));
                } else {
                    BaseFragment.setAlertNotification(MainFragmentActivity.getContext());
                }
            }

            @Override
            public void failure(RetrofitError error) {

               // Log.e("error", error.getMessage());
               // Log.e("error", error.getResponse().getReason());
                BaseFragment.setAlertNotification(MainFragmentActivity.getContext());

            }

        });
    }*/


}
