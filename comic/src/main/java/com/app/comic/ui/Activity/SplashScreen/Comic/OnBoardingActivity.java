package com.app.comic.ui.Activity.SplashScreen.Comic;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.app.comic.MainFragmentActivity;
import com.app.comic.R;
import com.app.comic.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class OnBoardingActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);

        //hideMenuButton();
        // hideTitle();
        //hideTabButton();
        //lockDrawer();
        // BaseFragment.removeLogoHeader(this);

        Bundle bundle = getIntent().getExtras();


        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.splash_content, OnBoardingFragment.newInstance(bundle)).commit();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }

    @Override
    public void onBackPressed() {
        //overrride
    }
}