package com.app.comic.ui.Activity.SplashScreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.app.comic.MainController;
import com.app.comic.MainFragmentActivity;
import com.app.comic.R;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.FragmentContainerActivity;
import com.app.comic.ui.Activity.PushNotificationActivity;
import com.app.comic.ui.Realm.RealmObjectController;

import butterknife.ButterKnife;

//import android.view.WindowManager;

public class SplashScreenActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);

        hideTabButton();
        //BaseFragment.removeLogoHeader(this);
        Boolean proceed = true;

        try {
            String action = getIntent().getExtras().getString("action");
            String message = getIntent().getExtras().getString("message");
            String title = getIntent().getExtras().getString("title");


            if (action.equals("NOTIFICATION")) {
                Log.e("Forward", "y");

                //save message to realm object
                RealmObjectController.clearNotificationMessage(getContext());
                RealmObjectController.saveNotificationMessage(getContext(), message , title);

                proceed = false;

                Intent intent = new Intent(this, PushNotificationActivity.class);
                startActivity(intent);
                finish();


            } else {
                Log.e("notForward", action);
            }

        } catch (Exception e) {
            Log.e("notForward", "ERROR");

        }

        if (proceed) {
            Log.e("Passcode", "Passcode Activity");
            MainController.setHomeStatus();
            Intent intent = new Intent(this, PassCodeActivity.class);
            startActivity(intent);
            finish();
        }


        // Bundle bundle = getIntent().getExtras();

        //FragmentManager fragmentManager = getSupportFragmentManager();
        //fragmentManager.beginTransaction().replace(R.id.splash_content, SplashScreenFragment.newInstance()).commit();

    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
