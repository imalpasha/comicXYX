package com.app.comic.ui.Activity.Homepage;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.comic.Controller;
import com.app.comic.R;
import com.app.comic.application.MainApplication;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.FragmentContainerActivity;
import com.app.comic.ui.Activity.SplashScreen.Comic.OnBoardingActivity;
import com.app.comic.ui.Model.JSON.TokenInfoJSON;
import com.app.comic.ui.Model.Receive.Comic.AuthReceive;
import com.app.comic.ui.Model.Receive.Comic.ComicReceive;
import com.app.comic.ui.Module.HomeModule;
import com.app.comic.ui.Presenter.HomePresenter;
import com.app.comic.ui.Realm.RealmObjectController;
import com.app.comic.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.realm.Realm;
import io.realm.RealmResults;


public class HomeFragment extends BaseFragment implements HomePresenter.ComicView {

    @Inject
    HomePresenter presenter;

    @InjectView(R.id.firstStory)
    LinearLayout firstStory;

    @InjectView(R.id.firstStory2)
    LinearLayout firstStory2;

    @InjectView(R.id.firstStory3)
    LinearLayout firstStory3;

    @InjectView(R.id.firstStory4)
    LinearLayout firstStory4;

    @InjectView(R.id.imageU)
    ImageView imageU;

    @InjectView(R.id.imageL)
    ImageView imageL;

    @InjectView(R.id.imageD)
    ImageView imageD;

    @InjectView(R.id.imageM)
    ImageView imageM;


    private int fragmentContainerId;
    private SharedPrefManager pref;
    View view;
    private static String token;

    public static HomeFragment newInstance() {

        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new HomeModule(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.home, container, false);
        ButterKnife.inject(this, view);

        setData();
        //getActivity().setTitle("Choose Story");

        firstStory.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    imageU.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.image_border));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageU.setBackground(null);
                }
                return false;
            }
        });

        firstStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestComic(getActivity(), "1", "0", "U");
            }
        });

        firstStory2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    imageL.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.image_border));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageL.setBackground(null);
                    requestComic(getActivity(), "1", "0", "L");
                }

                return true;
            }
        });

        firstStory3.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    imageD.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.image_border));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageD.setBackground(null);
                    requestComic(getActivity(), "1", "0", "D");
                }

                return true;
            }
        });

        firstStory4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    imageM.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.image_border));
                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    imageM.setBackground(null);
                    requestComic(getActivity(), "1", "0", "M");
                }

                return true;
            }
        });

        return view;
    }

    public void requestComic(Activity act, String level, String option, String character) {

        /*ComicRequest comicRequest = new ComicRequest();
        comicRequest.setCharacter("d");
        comicRequest.setLevel("1");
        comicRequest.setOption("0");
        comicRequest.setToken(token);*/
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("character", character);
        params.put("level", level);
        params.put("option", option);
        params.put("token", token);

        initiateLoading(act);
        Log.e("Send", "true");

        presenter.onComicRequest(params);

    }

    public void setData() {

        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<TokenInfoJSON> result2 = realm.where(TokenInfoJSON.class).findAll();
        AuthReceive authReceive = (new Gson()).fromJson(result2.get(0).getTokenInfo(), AuthReceive.class);
        token = authReceive.getToken();
        Log.e("Token", authReceive.getToken());
    }

    @Override
    public void onComicReceive(ComicReceive obj) {

        Log.e("Receive", "true");
        dismissLoading();

        Boolean status = Controller.getRequestStatus(obj.getStatus(), "test", getActivity());
        if (status) {

            Log.e("Status", "Success");

            Gson tokenInfo = new Gson();
            String comicD = tokenInfo.toJson(obj);
            RealmObjectController.setComicD(getActivity(), comicD);

            Intent onBoard = new Intent(getActivity(), OnBoardingActivity.class);
            getActivity().startActivity(onBoard);

        }
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

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
