package com.app.comic;

import android.util.Log;

import com.app.comic.api.ApiEndpoint;
import com.app.comic.api.ApiRequestInterceptor;
import com.app.comic.api.ApiService;
import com.app.comic.application.MainApplication;
import com.squareup.otto.Bus;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(
        injects = MainApplication.class
)
public class AppModule {

    private final String apiKey;

    public AppModule(String apiKey) {
        this.apiKey = apiKey;
    }

    @Provides
    @Singleton
    ApiRequestInterceptor provideRequestInterceptor() {
        return new ApiRequestInterceptor(apiKey);
    }

    @Provides
    @Singleton
    ApiEndpoint provideEndpoint() {
        return new ApiEndpoint();
    }


    @Provides
    @Singleton
    ApiService provideApiServices(ApiRequestInterceptor apiRequestInterceptor, ApiEndpoint endpoint) {

        final ApiService service;

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

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

       /* OkHttpClient defaultHttpClient = new OkHttpClient.Builder()
                .addInterceptor(
                        new Interceptor() {
                            @Override
                            public Response intercept(Interceptor.Chain chain) throws IOException {
                                Request request = chain.request().newBuilder()
                                        .addHeader("Accept", "application/json").build();
                                return chain.proceed(request);
                            }
                        }).build();*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(endpoint.getUrl())
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return service = retrofit.create(ApiService.class);
    }

    /*@Provides
    @Singleton
    ApiService provideApiService(RequestInterceptor requestInterceptor, Endpoint endpoint) {

        int MAX_IDLE_CONNECTIONS = 30 * 60 * 1000;
        int KEEP_ALIVE_DURATION_MS = 3 * 60 * 1000;

        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(120, TimeUnit.SECONDS);
        okHttpClient.setReadTimeout(120, TimeUnit.SECONDS);

        //okHttpClient.setWriteTimeout(60, TimeUnit.SECONDS);
        //okHttpClient.setConnectionPool(new com.squareup.okhttp.ConnectionPool(MAX_IDLE_CONNECTIONS, KEEP_ALIVE_DURATION_MS));


        return new RestAdapter.Builder()
                .setEndpoint(endpoint)
                .setRequestInterceptor(requestInterceptor)
                .setClient(new OkClient(okHttpClient))
                .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new RestAdapter.Log() {
                    public void log(String msg) {
                        Log.i("retrofit", msg);
                    }
                })
                .build()
                .create(ApiService.class);
    }*/


   /* builder.setLogLevel(LogLevel.FULL).setLog(new RestAdapter.Log() {
        public void log(String msg) {
            Log.i("retrofit", msg);
        }
    });*/

    @Provides
    @Singleton
    Bus provideBus() {
        return new Bus();
    }
}


