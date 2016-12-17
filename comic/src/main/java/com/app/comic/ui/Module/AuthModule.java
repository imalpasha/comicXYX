package com.app.comic.ui.Module;

import com.app.comic.ui.Activity.SplashScreen.PassCodeFragment;
import com.app.comic.ui.Presenter.HomePresenter;
import com.app.comic.AppModule;
import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module(
        injects = PassCodeFragment.class,
        addsTo = AppModule.class,
        complete = false
)
public class AuthModule {

    private final HomePresenter.AuthView authView;

    public AuthModule(HomePresenter.AuthView aboutUsView) {
        this.authView = aboutUsView;
    }

    @Provides
    @Singleton
    HomePresenter provideAboutUsPresenter(Bus bus) {
        return new HomePresenter(authView, bus);
    }
}
