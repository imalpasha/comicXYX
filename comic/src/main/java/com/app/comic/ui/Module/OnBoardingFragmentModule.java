package com.app.comic.ui.Module;

import com.app.comic.ui.Activity.SplashScreen.Comic.OnBoardingFragment;
import com.app.comic.ui.Presenter.HomePresenter;
import com.app.comic.AppModule;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = OnBoardingFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class OnBoardingFragmentModule {

    private final HomePresenter.ComicOptionView comicOptionView;

    public OnBoardingFragmentModule(HomePresenter.ComicOptionView aboutUsView) {
        this.comicOptionView = aboutUsView;
    }

    @Provides
    @Singleton
    HomePresenter provideAboutUsPresenter(Bus bus) {
        return new HomePresenter(comicOptionView, bus);
    }
}
