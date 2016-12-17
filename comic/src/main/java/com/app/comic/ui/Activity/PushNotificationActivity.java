package com.app.comic.ui.Activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;


import com.app.comic.MainFragmentActivity;
import com.app.comic.R;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class PushNotificationActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);

        Bundle bundle = getIntent().getExtras();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.splash_content, PushNotificationFragment.newInstance(bundle)).commit();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
