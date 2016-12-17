package com.app.comic.ui.Activity.Homepage;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.app.comic.MainFragmentActivity;
import com.app.comic.R;
import com.app.comic.ui.Activity.FragmentContainerActivity;

import butterknife.ButterKnife;


public class HomeActivity extends MainFragmentActivity implements FragmentContainerActivity {

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content, HomeFragment.newInstance(), "Home").commit();

        //setTitle(R.string.TBD_app_name);
    }



    @Override
    public void onResume() {
        super.onResume();
        // presenter.onResume();
    }

    @Override
    public int getFragmentContainerId() {
        return R.id.main_activity_fragment_container;
    }
}
