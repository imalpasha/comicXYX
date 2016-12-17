package com.app.comic.ui.Activity.SplashScreen;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.comic.R;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.FragmentContainerActivity;
import com.app.comic.ui.Realm.RealmObjectController;

import butterknife.ButterKnife;

public class InfoFragment extends BaseFragment {

    private int fragmentContainerId;
    private Dialog dialog;

    View view;

    public static InfoFragment newInstance() {

        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //MainApplication.get(getActivity()).createScopedGraph(new LanguageModule(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.info_screen, container, false);
        ButterKnife.inject(this, view);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fragmentContainerId = ((FragmentContainerActivity) getActivity()).getFragmentContainerId();
    }

    @Override
    public void onResume() {
        super.onResume();
        // presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // presenter.onPause();

    }


}

