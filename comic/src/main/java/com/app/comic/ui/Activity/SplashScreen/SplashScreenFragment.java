package com.app.comic.ui.Activity.SplashScreen;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.app.comic.application.MainApplication;
import com.app.comic.MainController;
import com.app.comic.R;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.FragmentContainerActivity;
import com.app.comic.ui.Presenter.HomePresenter;
import com.app.comic.utils.SharedPrefManager;


import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class SplashScreenFragment extends BaseFragment implements HomePresenter.SplashScreen {

    @Inject
    HomePresenter presenter;

    private int fragmentContainerId;
    private SharedPrefManager pref;
    private ProgressDialog progress;

    public static SplashScreenFragment newInstance() {

        SplashScreenFragment fragment = new SplashScreenFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.splash_screen, container, false);
        ButterKnife.inject(this, view);

        pref = new SharedPrefManager(getActivity());
        HashMap<String, String> initAppData = pref.getFirstTimeUser();
        String firstTime = initAppData.get(SharedPrefManager.FIRST_TIME_USER);

        if (firstTime != null && firstTime.equals("N")) {

        }else{

        }

        return view;
    }


    /*public void connectionRetry(String msg) {

        pDialog.setTitleText("Connection Error");
        pDialog.setCancelable(false);
        pDialog.setContentText(msg);
        pDialog.setConfirmText("Retry");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sendDeviceInformationToServer(info);
            }
        })
                .show();

    }*/
   /*public void update(InitialLoadReceive obj) {

        String signature = obj.getObj().getSignature();
        String bannerUrl = obj.getObj().getBanner_default();
        String promoBannerUrl = obj.getObj().getBanner_promo();
        String bannerModule = obj.getObj().getBanner_module();
        String dataVersion = obj.getObj().getData_version();
        String appVersion = obj.getObj().getData_version_mobile().getVersion();
        String bannerRedirectURL = obj.getObj().getBanner_redirect_url();

        InitialLoadReceive.SocialMedia socialMediaObj = obj.getObj().getSocial_media();

        Gson gson = new Gson();
        String title = gson.toJson(obj.getObj().getData_title());
        pref.setUserTitle(title);

        String country = gson.toJson(obj.getObj().getData_country());
        pref.setCountry(country);

        String state = gson.toJson(obj.getObj().getData_state());
        pref.setState(state);

        String flight = gson.toJson(obj.getObj().getData_market());
        pref.setFlight(flight);

        String socialMedia = gson.toJson(socialMediaObj);
        pref.setSocialMedia(socialMedia);

        pref.setSignatureToLocalStorage(signature);
        pref.setBannerUrl(bannerUrl);
        pref.setPromoBannerUrl(promoBannerUrl);
        pref.setBannerModule(bannerModule);
        pref.setBannerRedirectURL(bannerRedirectURL);
        pref.setDataVersion(dataVersion);
        pref.setAppVersion(appVersion);

        //thru homepage
        goHomepage();

    }*/

    public void goHomepage() {
        //go to on boarding first

        //first time user
        MainController.setHomeStatus();
        HashMap<String, String> initAppData = pref.getFirstTimeUser();
        String firstTime = initAppData.get(SharedPrefManager.FIRST_TIME_USER);
    }

    @Override
    public void onConnectionFailed() {
        // connectionRetry("Unable to connect to server");
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }

}
