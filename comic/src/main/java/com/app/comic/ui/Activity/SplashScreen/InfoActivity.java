package com.app.comic.ui.Activity.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.app.comic.MainFragmentActivity;
import com.app.comic.R;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class InfoActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);

        hideTabButton();
        //BaseFragment.removeLogoHeader(this);

        //Intent intent = new Intent(this, PassCodeActivity.class);
        //startActivity(intent);
        //finish();

        // Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.splash_content, InfoFragment.newInstance()).commit();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
