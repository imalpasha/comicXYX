package com.app.comic.ui.Activity.SplashScreen;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.app.comic.MainFragmentActivity;
import com.app.comic.R;
import com.app.comic.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;

public class PassCodeActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);
        //setTitle(getResources().getString(R.string.TBD_app_name));

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.splash_content, PassCodeFragment.newInstance(),"Home").commit();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }

    @Override
    public void onBackPressed() {
        //final FragmentManager manager = getSupportFragmentManager();
        //PassCodeFragment fragment = (PassCodeFragment) manager.findFragmentByTag("Home");
        //fragment.exitApp();
    }
}
