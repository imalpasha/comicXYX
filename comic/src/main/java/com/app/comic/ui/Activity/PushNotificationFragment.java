package com.app.comic.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.comic.MainController;
import com.app.comic.MainFragmentActivity;
import com.app.comic.R;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.SplashScreen.PassCodeActivity;
import com.app.comic.ui.Activity.SplashScreen.SplashScreenActivity;

import butterknife.ButterKnife;

public class PushNotificationFragment extends BaseFragment {

    public static PushNotificationFragment newInstance(Bundle bundle) {

        PushNotificationFragment fragment = new PushNotificationFragment();
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

        MainFragmentActivity.setAppStatus("ready_for_notification");

        Log.e("Already Here", "Ready For Notification");

        if (MainController.getHomeStatus()) {
            getActivity().finish();
            Log.e("Finish", "Y");
        } else {
            Log.e("Open App", "Y");
            Intent home = new Intent(getActivity(), SplashScreenActivity.class);
            home.setAction("android.intent.action.MAIN");
            home.addCategory("android.intent.category.LAUNCHER");
            home.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getActivity().startActivity(home);
            getActivity().finish();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}
