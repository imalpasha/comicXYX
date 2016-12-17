package com.app.comic.ui.Presenter;

import android.util.Log;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

public class LanguagePresenter {

    public interface LanguageView {

    }

    private final LanguageView view;
    private final Bus bus;

    public LanguagePresenter(LanguageView view, Bus bus) {
        this.view = view;
        this.bus = bus;
    }

    public void onResume() {
        bus.register(this);
    }

    public void onPause() {
        bus.unregister(this);
    }

}
