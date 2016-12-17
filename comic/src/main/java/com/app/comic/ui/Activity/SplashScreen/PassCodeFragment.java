package com.app.comic.ui.Activity.SplashScreen;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.comic.Controller;
import com.app.comic.MainController;
import com.app.comic.R;
import com.app.comic.application.MainApplication;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.FragmentContainerActivity;
import com.app.comic.ui.Activity.Homepage.HomeActivity;
import com.app.comic.ui.Activity.SplashScreen.Comic.OnBoardingActivity;
import com.app.comic.ui.Model.JSON.Bookmark;
import com.app.comic.ui.Model.JSON.BookmarkJSON;
import com.app.comic.ui.Model.Receive.Comic.AuthReceive;
import com.app.comic.ui.Model.Receive.Comic.ComicReceive;
import com.app.comic.ui.Model.Request.Comic.AuthRequest;
import com.app.comic.ui.Module.AuthModule;
import com.app.comic.ui.Presenter.HomePresenter;
import com.app.comic.ui.Realm.RealmObjectController;
import com.app.comic.utils.SharedPrefManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;

public class PassCodeFragment extends BaseFragment implements HomePresenter.AuthView {

    private int fragmentContainerId;
    private Dialog dialog;

    @Inject
    HomePresenter presenter;

    @InjectView(R.id.btnStart)
    ImageView btnStart;

    @InjectView(R.id.txtNo1Main)
    TextView txtNo1Main;
    @InjectView(R.id.txtNo2Main)
    TextView txtNo2Main;
    @InjectView(R.id.txtNo3Main)
    TextView txtNo3Main;
    @InjectView(R.id.txtNo4Main)
    TextView txtNo4Main;
    @InjectView(R.id.txtNo5Main)
    TextView txtNo5Main;
    @InjectView(R.id.txtNo6Main)
    TextView txtNo6Main;

    @InjectView(R.id.btnSetting)
    LinearLayout btnSetting;

    @InjectView(R.id.btnInfo)
    LinearLayout btnInfo;

    @InjectView(R.id.passcodeLayout)
    LinearLayout passcodeLayout;

    @InjectView(R.id.btnExit)
    LinearLayout btnExit;

    TextView txtNo1, txtNo2, txtNo3, txtNo4, txtNo5, txtNo6;
    LinearLayout lineNo1, lineNo2, lineNo3, lineNo4, lineNo5, lineNo6;
    String position, character, level, option, token;

    int currentPosition = 0;

    ArrayList<String> test = new ArrayList<String>();
    View view;
    SharedPrefManager pref;

    public static PassCodeFragment newInstance() {

        PassCodeFragment fragment = new PassCodeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new AuthModule(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.passcode, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());

        MainController.setHomeStatus();

        HashMap<String, String> initPassword = pref.getMusic();
        String music = initPassword.get(SharedPrefManager.MUSIC);

        if (music != null) {
            if (music.equals("Y")) {
                comic_backgroundMusic(getActivity());
            }
        }

        txtNo1Main.setTag("position0");
        txtNo2Main.setTag("position1");
        txtNo3Main.setTag("position2");
        txtNo4Main.setTag("position3");
        txtNo5Main.setTag("position4");
        txtNo6Main.setTag("position5");


        btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exitApp();
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                comic_btnClicked(getActivity());
                Intent profilePage = new Intent(getActivity(), SettingActivity.class);
                getActivity().startActivity(profilePage);
            }
        });


        btnInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                comic_btnClicked(getActivity());
                Intent profilePage = new Intent(getActivity(), InfoActivity.class);
                getActivity().startActivity(profilePage);
            }
        });


        // txtNo1.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //Intent profilePage = new Intent(getActivity(), HomeActivity.class);
                //getActivity().startActivity(profilePage);
                validatePasscode();
                //getActivity().finish();

            }
        });

        passcodeLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //popup.
                customNumeric();
            }
        });


        return view;
    }

    public void validatePasscode() {
        Log.e("Size", Integer.toString(test.size()));
        if (test.size() < 6) {
            popupAlert("Please fill your 6 character passcode");
        } else {

            String a1 = test.get(0);
            String a2 = test.get(1);
            String a3 = test.get(2);
            String a4 = test.get(3);
            String a5 = test.get(4);
            String a6 = test.get(5);

            String passcode = new StringBuilder(a1).append(a2).append(a3).append(a4).append(a5).append(a6).toString();

            AuthRequest authRequest = new AuthRequest();
            authRequest.setPasscode(passcode);
            authRequest.setPlatform("Android");
            authRequest.setUdid("UDID");
            authRequest.setPush_token("null");

            initiateLoading(getActivity());
            presenter.onAuthRequest(authRequest);
        }
    }

    @Override
    public void onAuthReceive(AuthReceive obj) {


        Boolean status = Controller.getRequestStatus(obj.getStatus(), "test", getActivity());
        if (status) {

            Gson tokenInfo = new Gson();
            String tokenInfo2 = tokenInfo.toJson(obj);
            RealmObjectController.setTokenInfo(getActivity(), tokenInfo2);
            token = obj.getToken();

            Realm realm = RealmObjectController.getRealmInstance(getActivity());
            final RealmResults<BookmarkJSON> bookmarkResult = realm.where(BookmarkJSON.class).findAll();
            if (bookmarkResult.size() > 0) {
                Bookmark bookmark = (new Gson()).fromJson(bookmarkResult.get(0).getBookmark(), Bookmark.class);
                character = bookmark.getCharacter();
                level = bookmark.getLevel();
                option = bookmark.getOption();
                position = bookmark.getPosition();

                loadBookmark();

            } else {

                dismissLoading();

                Intent profilePage = new Intent(getActivity(), HomeActivity.class);
                getActivity().startActivity(profilePage);

            }

        } else {
            dismissLoading();
        }
    }

    public void customNumeric() {
        currentPosition = 0;
        test = new ArrayList<String>();


        //popup = 1;
        //SCREEN_NAME = "Book FLight: Flight Details(Login PopupActivity)";

        LayoutInflater li = LayoutInflater.from(getActivity());
        final View myView = li.inflate(R.layout.custom_numeric, null);

        Button btn1 = (Button) myView.findViewById(R.id.btn1);
        Button btn2 = (Button) myView.findViewById(R.id.btn2);
        Button btn3 = (Button) myView.findViewById(R.id.btn3);
        Button btn4 = (Button) myView.findViewById(R.id.btn4);
        Button btn5 = (Button) myView.findViewById(R.id.btn5);
        Button btn6 = (Button) myView.findViewById(R.id.btn6);
        Button btn7 = (Button) myView.findViewById(R.id.btn7);
        Button btn8 = (Button) myView.findViewById(R.id.btn8);
        Button btn9 = (Button) myView.findViewById(R.id.btn9);
        Button btn0 = (Button) myView.findViewById(R.id.btn0);
        Button btnCancel = (Button) myView.findViewById(R.id.btnCancel);
        Button btnDelete = (Button) myView.findViewById(R.id.btnDelete);

        txtNo1 = (TextView) myView.findViewById(R.id.txtNo1);
        txtNo2 = (TextView) myView.findViewById(R.id.txtNo2);
        txtNo3 = (TextView) myView.findViewById(R.id.txtNo3);
        txtNo4 = (TextView) myView.findViewById(R.id.txtNo4);
        txtNo5 = (TextView) myView.findViewById(R.id.txtNo5);
        txtNo6 = (TextView) myView.findViewById(R.id.txtNo6);

        lineNo1 = (LinearLayout) myView.findViewById(R.id.lineNo1);
        lineNo2 = (LinearLayout) myView.findViewById(R.id.lineNo2);
        lineNo3 = (LinearLayout) myView.findViewById(R.id.lineNo3);
        lineNo4 = (LinearLayout) myView.findViewById(R.id.lineNo4);
        lineNo5 = (LinearLayout) myView.findViewById(R.id.lineNo5);
        lineNo6 = (LinearLayout) myView.findViewById(R.id.lineNo6);

        txtNo1.setTag("position0");
        txtNo2.setTag("position1");
        txtNo3.setTag("position2");
        txtNo4.setTag("position3");
        txtNo5.setTag("position4");
        txtNo6.setTag("position5");

        btn1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("1");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("2");
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("3");
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("4");
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("5");
            }
        });
        btn6.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("6");
            }
        });
        btn7.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("7");
            }
        });
        btn8.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("8");
            }
        });
        btn9.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("9");
            }
        });
        btn0.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setPasscode("0");
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (currentPosition > 0) {
                    currentPosition--;
                    TextView txt = (TextView) myView.findViewWithTag("position" + currentPosition);
                    txt.setText("");

                    test.remove(currentPosition);

                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                test = new ArrayList<String>();
            }
        });


        dialog = new Dialog(getActivity(), R.style.DialogTheme);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(myView);

        dialog.setContentView(myView);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#4DFFFFFF")));
        dialog.setCancelable(false);
        //dialog = builder.create();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = 570;
        dialog.getWindow().setAttributes(lp);
        dialog.show();


    }


    public void setPasscode(String passcode) {
        currentPosition++;
        if (currentPosition < 7) {

            test.add(passcode);

            if (currentPosition == 1) {
                txtNo1.setText(passcode);
                layoutParam(lineNo1);
            } else if (currentPosition == 2) {
                txtNo2.setText(passcode);
                layoutParam(lineNo2);
            } else if (currentPosition == 3) {
                txtNo3.setText(passcode);
                layoutParam(lineNo3);
            } else if (currentPosition == 4) {
                txtNo4.setText(passcode);
                layoutParam(lineNo4);
            } else if (currentPosition == 5) {
                txtNo5.setText(passcode);
                layoutParam(lineNo5);
            } else if (currentPosition == 6) {
                txtNo6.setText(passcode);
                layoutParam(lineNo6);
                dialog.dismiss();
                refillPasscode();
            }
        }

    }

    public void layoutParam(LinearLayout line) {
        ViewGroup.LayoutParams params = line.getLayoutParams();
        params.height = 2;
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        line.setLayoutParams(params);
    }

    public void refillPasscode() {
        if (test.size() > 0) {
            //Log.e("Size",Integer.toString(test.size()));
            for (int x = 0; x < test.size(); x++) {
                TextView txt = (TextView) view.findViewWithTag("position" + x);
                txt.setText(test.get(x));
            }
        }
    }

    public void loadBookmark() {


        HashMap<String, String> params = new HashMap<String, String>();
        params.put("character", character);
        params.put("level", level);
        params.put("option", option);
        params.put("token", token);

        initiateLoading(getActivity());
        Log.e("Send", "true");

        presenter.onComicRequest(params);

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
            onBoard.putExtra("POSITION", position);
            getActivity().startActivity(onBoard);

        }
    }

    public void exitApp() {

        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getResources().getString(R.string.comic_exit_alpha))
                .setContentText(getResources().getString(R.string.comic_confirm_exit))
                .showCancelButton(true)
                .setCancelText(getResources().getString(R.string.comic_cancel_exit))
                .setConfirmText(getResources().getString(R.string.comic_exit))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        getActivity().finish();
                        getActivity().finishAffinity();
                        System.exit(0);

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.cancel();
                    }
                })
                .show();

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

