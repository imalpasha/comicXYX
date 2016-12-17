package com.app.comic.ui.Activity.SplashScreen;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.app.comic.R;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.FragmentContainerActivity;
import com.app.comic.ui.Realm.RealmObjectController;
import com.app.comic.utils.DropDownItem;
import com.app.comic.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingFragment extends BaseFragment {

    private int fragmentContainerId;
    private Dialog dialog;

    View view;
    SharedPrefManager pref;

    @InjectView(R.id.musicSwitch)
    Switch musicSwitch;

    @InjectView(R.id.txtLanguageSelection)
    TextView txtLanguageSelection;

    ArrayList<DropDownItem> languageList = new ArrayList<DropDownItem>();
    private Locale myLocale;

    public static SettingFragment newInstance() {

        SettingFragment fragment = new SettingFragment();
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

        view = inflater.inflate(R.layout.setting_screen, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());

        HashMap<String, String> initPassword = pref.getMusic();
        String music = initPassword.get(SharedPrefManager.MUSIC);

        HashMap<String, String> code = pref.getLanguageCode();
        String languageCode = code.get(SharedPrefManager.LANGUAGE_CODE);

        if (languageCode == null) {
            txtLanguageSelection.setText("English");
        } else {
            if (languageCode.equals("en")) {
                txtLanguageSelection.setText("English");
            } else {
                txtLanguageSelection.setText("Malay");
            }
        }

        if (music != null) {
            if (music.equals("Y")) {
                musicSwitch.setChecked(true);
            } else {
                musicSwitch.setChecked(false);
            }
        }

        musicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    comic_backgroundMusic(getActivity());
                    pref.setMusic("Y");
                } else {
                    comic_stopBackgroundMusic();
                    pref.setMusic("N");

                }
            }
        });

        languageList = getLanguage(getActivity());

        txtLanguageSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupSelection(languageList, getActivity(), txtLanguageSelection, true, view);
            }
        });

        return view;
    }

    public ArrayList<DropDownItem> getLanguage(Activity act) {

		/*Travelling Purpose*/
        ArrayList<DropDownItem> languageList = new ArrayList<DropDownItem>();

		/*Travel Doc*/
        final String[] doc = act.getResources().getStringArray(R.array.language_list);
        for (int i = 0; i < doc.length; i++) {
            String travelDoc = doc[i];
            String[] splitDoc = travelDoc.split("-");

            DropDownItem itemDoc = new DropDownItem();
            itemDoc.setText(splitDoc[0]);
            itemDoc.setCode(splitDoc[1]);
            languageList.add(itemDoc);
        }

        return languageList;

    }


    public void changeLanguage(String selectedLanguage) {
        String lang = "en";

        if (selectedLanguage.equalsIgnoreCase("en")) {
            lang = "en";
        } else if (selectedLanguage.equalsIgnoreCase("ms")) {
            lang = "ms";
        }

        changeLang(lang);
    }

    public void changeLang(String lang) {
        if (lang.equalsIgnoreCase(""))
            return;
        myLocale = new Locale(lang);
        saveLocale(lang);
        Locale.setDefault(myLocale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.locale = myLocale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, getActivity().getBaseContext().getResources().getDisplayMetrics());
        //updateTexts();
    }

    public void saveLocale(String lang) {
        String langPref = "Language";
        SharedPreferences prefs = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(langPref, lang);
        editor.commit();
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (myLocale != null) {
            newConfig.locale = myLocale;
            Locale.setDefault(myLocale);
            getActivity().getBaseContext().getResources().updateConfiguration(newConfig, getActivity().getBaseContext().getResources().getDisplayMetrics());
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
        // presenter.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        // presenter.onPause();

    }


}

