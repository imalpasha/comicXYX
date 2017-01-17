package com.app.comic.ui.Presenter;

import android.util.Log;

import com.app.comic.MainFragmentActivity;
import com.app.comic.ui.Model.Receive.Comic.AuthReceive;
import com.app.comic.ui.Model.Receive.Comic.ComicReceive;
import com.app.comic.ui.Model.Request.Comic.AuthRequest;
import com.app.comic.ui.Model.Request.Comic.ComicRequest;
import com.app.comic.utils.SharedPrefManager;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class HomePresenter {

    private SharedPrefManager pref;

    public interface PushNotification {

    }

    public interface HomeView {

    }


    public interface ComicView {
        void onComicReceive(ComicReceive obj);
    }

    public interface ComicOptionView {
        void onComicReceive(ComicReceive obj);
    }

    public interface AuthView {
        void onComicReceive(ComicReceive obj);

        void onAuthReceive(AuthReceive obj);
    }

    public interface SplashScreen {
        void onConnectionFailed();

    }

    private HomeView view;
    private ComicView comicView;
    private SplashScreen view2;
    private PushNotification view3;
    private AuthView authView;
    private ComicOptionView comicOptionView;

    private final Bus bus;

    public HomePresenter(HomeView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public HomePresenter(ComicView view, Bus bus) {
        this.comicView = view;
        this.bus = bus;
    }

    public HomePresenter(AuthView view, Bus bus) {
        this.authView = view;
        this.bus = bus;
    }

    public HomePresenter(ComicOptionView view, Bus bus) {
        this.comicOptionView = view;
        this.bus = bus;
    }


    public void onComicRequest(HashMap<String, String> params) {
        bus.post(new HashMap<String, String>(params));
        Log.e("Send?", "Y");
    }

    public void onAuthRequest(AuthRequest data) {
        bus.post(new AuthRequest(data));
    }

    @Subscribe
    public void onAuthReceive(AuthReceive event) {
        authView.onAuthReceive(event);
        Log.e("AuthReceive", "true");
    }

    @Subscribe
    public void onComicReceive(ComicReceive event) {
        if (comicOptionView != null) {
            comicOptionView.onComicReceive(event);
            Log.e("Comic 2", "true");

        }
        if (comicView != null) {
            comicView.onComicReceive(event);
            Log.e("Comic 3", "true");

        }
        if (authView != null) {
            authView.onComicReceive(event);
            Log.e("Comic 3", "true");

        }
        Log.e("Comic 4", "true");

    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }


}
