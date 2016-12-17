package com.app.comic.ui.Module;

import com.app.comic.ui.Activity.Homepage.HomeFragment;
import com.app.comic.ui.Presenter.HomePresenter;
import com.app.comic.AppModule;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = HomeFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class HomeModule {

    private final HomePresenter.ComicView comicView;

    public HomeModule(HomePresenter.ComicView view) {
        this.comicView = view;
    }

    @Provides
    @Singleton
    HomePresenter provideAboutUsPresenter(Bus bus) {
        return new HomePresenter(comicView, bus);
    }
}
