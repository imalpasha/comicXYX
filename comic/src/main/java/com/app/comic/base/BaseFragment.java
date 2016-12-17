package com.app.comic.base;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.comic.application.MainApplication;
import com.app.comic.MainController;
import com.app.comic.R;
import com.app.comic.ui.Activity.SplashScreen.PassCodeActivity;
import com.app.comic.ui.Activity.SplashScreen.SplashScreenActivity;
//import com.fly.firefly.ui.adapter.CheckInPassengerListAdapter;
import com.app.comic.utils.DropDownItem;
import com.app.comic.utils.DropMenuAdapter;
import com.app.comic.utils.SharedPrefManager;
import com.app.comic.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;


public class BaseFragment extends Fragment {

    public com.app.comic.base.AQuery aq;
    private SharedPreferences pref;
    private int indexForState = -1;
    private String selected;
    private SharedPrefManager prefManager;
    private static SpotsDialog mProgressDialog;
    private static SweetAlertDialog pDialog;
    private static Dialog dialog;
    private static Boolean status;
    Boolean manualValidationStatus = true;
    private static int staticIndex = -1;
    private Activity activityContext;
    private static ProgressDialog progressDialog;
    private static int currentLength = 0;

    /* COMIC */

    static MediaPlayer backgroundMP;

    public static void setPushNotificationAlert(Activity act, String message, String title) {

        LayoutInflater li = LayoutInflater.from(act);
        final View myView = li.inflate(R.layout.push_notification_alert, null);
        Button cont = (Button) myView.findViewById(R.id.push_close_btn);
        TextView pushTitle = (TextView) myView.findViewById(R.id.push_content);
        TextView pushMessage = (TextView) myView.findViewById(R.id.push_title);

        pushTitle.setText(message);
        pushMessage.setText(title);


        //AlertDialog.Builder builder = new AlertDialog.Builder(act);
        //builder.setView(myView);
        dialog = new Dialog(act, R.style.DialogThemePush);
        //dialog = builder.create();
        dialog.setContentView(myView);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        //lp.height = 570;
        dialog.getWindow().setAttributes(lp);
        dialog.show();

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }

        });

    }

    public void comic_btnClicked(Activity act) {
        final MediaPlayer mp = MediaPlayer.create(act, R.raw.gets_in_the_way);
        mp.start();
    }

    public static void comic_backgroundMusic(Activity act) {
        if (backgroundMP != null) {
            backgroundMP.stop();
        }
        backgroundMP = MediaPlayer.create(act, R.raw.payon);
        backgroundMP.setLooping(true);
        backgroundMP.setVolume(80, 80);
        backgroundMP.start();
    }

    public static void comic_stopBackgroundMusic() {
        if (backgroundMP != null) {
            backgroundMP.stop();
        }
    }

    public static void comic_pauseBackgroundMusic() {
        if (backgroundMP != null) {
            backgroundMP.pause();
            currentLength = backgroundMP.getCurrentPosition();


        }
    }

    public static void comic_playBackgroundMusic() {
        if (backgroundMP != null) {
            backgroundMP.seekTo(currentLength);
            backgroundMP.start();
        }
    }

    /* ------------------- */


    public void initiateDefaultLoading(ProgressDialog progress, Activity act) {
        progress.setTitle("Loading");
        progress.setMessage("Please wait...");
        progress.show();
    }

    public void dismissDefaultLoading(ProgressDialog progress, Activity act) {
        progress.dismiss();
    }


    public String splitCountryDialingCode(String data, String codeToSplit) {

        String code;

        String string = codeToSplit;
        String[] parts = string.split("/");

        if (data.equals("CountryCode")) {
            code = parts[0];
        } else {
            code = parts[1];
        }
        return code;
    }

    //check added fragment
    public boolean checkFragmentAdded() {

        boolean addFragment = false;

        Fragment fragment = getFragmentManager().findFragmentByTag("DATEPICKER_TAG");
        if (fragment != null) {
            addFragment = false;
        } else {
            addFragment = true;
        }

        return addFragment;

    }

    public boolean flightDuration2(String arrivalTime, String returnDepartureTime) {

        boolean status = true;

        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mma");

        Date arrival = null;
        Date departureReturn = null;

        try {
            arrival = parseFormat.parse(arrivalTime);
            departureReturn = parseFormat.parse(returnDepartureTime);
        } catch (Exception e) {
        }

        long count60minute = arrival.getTime() - departureReturn.getTime();

        if (Math.abs(TimeUnit.MILLISECONDS.toMinutes(count60minute)) <= 60) {
            status = false;
        }

        return status;
    }

    public boolean flightDuration(String arrivalTime, String returnDepartureTime) {

        boolean status = true;

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");

        Log.e("Flight Duration", "True.");
        Log.e(arrivalTime, returnDepartureTime);

        try {

            Date arrival = parseFormat.parse(arrivalTime);
            Date departureReturn = parseFormat.parse(returnDepartureTime);

            if (arrival != null && departureReturn != null) {
                long count60minute = arrival.getTime() - departureReturn.getTime();

                if (Math.abs(TimeUnit.MILLISECONDS.toMinutes(count60minute)) <= 60) {
                    status = false;
                }
            }

        } catch (Exception e) {

            Log.e("getTime()", e.toString());
            status = false;

        }

        return status;
    }

    public void blinkText(TextView txt) {

        //try set blinking textview
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(120); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        txt.startAnimation(anim);

    }

    public String getDialingCode(String coutryCode, Activity act) {

        String dialingCode = null;

        JSONArray jsonCountry = getCountry(act);
        for (int x = 0; x < jsonCountry.length(); x++) {

            JSONObject row = (JSONObject) jsonCountry.opt(x);
            if (coutryCode.equals(row.optString("country_code"))) {
                dialingCode = row.optString("dialing_code");
            }
        }

        return dialingCode;
    }

    public boolean validateDialingCode(String dialingCode, String mobilePhone) {

        boolean status = false;

        String twoChar = mobilePhone.substring(0, 2);
        if (!dialingCode.equals(twoChar)) {
            status = true;
        }
        return status;
    }

    public boolean timeCompare(String arrivalTime, String returnDepartureTime) {

        boolean status = false;

        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
        arrivalTime = "8:40 PM";
        returnDepartureTime = "1:40 PM";
        Date arrival = null;
        Date departureReturn = null;

        try {
            arrival = parseFormat.parse(arrivalTime);
            departureReturn = parseFormat.parse(returnDepartureTime);

            Log.e(arrival.toString(), departureReturn.toString());

        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        if (arrival.getTime() > departureReturn.getTime()) {
            status = true;
        }

        return status;
    }


    public boolean compare90Minute(String arrivalTime, String returnDepartureTime) {

        boolean status = false;

        SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
        SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");

        Date arrival = null;
        Date departureReturn = null;

        try {
            arrival = parseFormat.parse(arrivalTime);
            departureReturn = parseFormat.parse(returnDepartureTime);
        } catch (Exception e) {
        }

        long count90minute = arrival.getTime() - departureReturn.getTime();

        if (Math.abs(TimeUnit.MILLISECONDS.toMinutes(count90minute)) < 90) {
            status = true;
        }

        return status;
    }

    public boolean travellerAgeValidation(ArrayList<Integer> ageOfTraveller) {

        boolean lessThan12 = true;
        //checkAgeOfTraveller
        for (int y = 0; y < ageOfTraveller.size(); y++) {
            if (ageOfTraveller.get(y) > 12) {
                lessThan12 = false;
            }
        }
        return lessThan12;

    }

    public int travellerAge(String dob) {

        int age;

        String[] splitDOB = dob.split("/");
        String dobYear = splitDOB[2];

        boolean status = false;

        Calendar now = Calendar.getInstance();   // Gets the current date and time
        int currentYear = now.get(Calendar.YEAR);

        age = currentYear - Integer.parseInt(dobYear);

        return age;
    }


    public static void staticPopup(final ArrayList<DropDownItem> array, Activity act, final TextView txt, final Boolean tagStatus, final LinearLayout txt2, final String indicate) {

        final ArrayList<DropDownItem> a = array;
        DropMenuAdapter dropState = new DropMenuAdapter(act);
        dropState.setItems(a);

        AlertDialog.Builder alertStateCode = new AlertDialog.Builder(act);

        alertStateCode.setSingleChoiceItems(dropState, staticIndex, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selected = a.get(which).getText();
                String selectedCode = a.get(which).getCode();

                txt.setText(selected);
                if (selectedCode.equals(indicate)) {
                    txt2.setVisibility(View.VISIBLE);
                    //adapt.returnNotifyDataChanged(selectedCode);
                } else {
                    txt2.setVisibility(View.GONE);
                    //adapt.returnNotifyDataChanged(selectedCode);
                }

                staticIndex = which;

                dialog.dismiss();
            }
        });

        AlertDialog mDialog = alertStateCode.create();
        mDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = 600;
        mDialog.getWindow().setAttributes(lp);
    }

    public static String[] getCharAt(List<String> countryChar) {

        String[] charToBeFilter = new String[countryChar.size()];
        for (int i = 0; i < countryChar.size(); i++) {
            charToBeFilter[i] = countryChar.get(i);
        }

        String[] newFilter = removeDuplicates(charToBeFilter);

        return newFilter;
    }

    public static Integer[] headerPosition(List<String> countryChar) {

        String[] charToBeFilter = new String[countryChar.size()];
        for (int i = 0; i < countryChar.size(); i++) {
            charToBeFilter[i] = countryChar.get(i);
        }

        Set<String> alreadyPresent = new HashSet<>();
        Integer[] whitelist = new Integer[charToBeFilter.length];
        int b = 0;
        int loop = 0;
        for (String element : charToBeFilter) {
            if (alreadyPresent.add(element)) {
                whitelist[b++] = loop;
            }
            loop++;
        }

        return Arrays.copyOf(whitelist, b);
    }

    public static String[] removeDuplicates(String[] arr) {
        Set<String> alreadyPresent = new HashSet<>();
        String[] whitelist = new String[arr.length];
        int i = 0;

        for (String element : arr) {
            if (alreadyPresent.add(element)) {
                whitelist[i++] = element;
            }
        }

        return Arrays.copyOf(whitelist, i);
    }


    public void setShake(View view) {
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        view.startAnimation(shake);
    }

    public String getFlightPurpose(String travel_purpose) {

        String purpose;

        if (travel_purpose.equals("1")) {
            purpose = "Leisure";
        } else {
            purpose = "Business";
        }

        return purpose;
    }


    public static void setSuccessDialog(final Activity act, String msg, final Class<?> cls, String message) {

        if (cls == null) {
            new SweetAlertDialog(act, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(message)
                    .setContentText(msg)
                    .show();
        } else {
            new SweetAlertDialog(act, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(message)
                    .setContentText(msg)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            Intent explicitIntent = new Intent(act, cls);
                            explicitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            explicitIntent.putExtra("AlertDialog", "Y");
                            act.startActivity(explicitIntent);
                            act.finish();
                            sDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    public static void setSuccessDialogNoClear(final Activity act, String msg, final Class<?> cls, String message) {

        if (cls == null) {
            new SweetAlertDialog(act, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(message)
                    .setContentText(msg)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            act.finish();
                            sDialog.dismiss();
                        }
                    })
                    .show();
        }
    }

    public static void setSuccessDialogNoFinish(final Activity act, String msg, final Class<?> cls, String message) {

        if (cls == null) {
            new SweetAlertDialog(act, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText(message)
                    .setContentText(msg)
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                        }
                    })
                    .show();
        }
    }


    public static void setAlertMaintance(final Activity act, String msg, final Class<?> cls, String message) {


        new SweetAlertDialog(act, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(message)
                .setContentText(msg)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Intent explicitIntent = new Intent(act, cls);
                        explicitIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        //explicitIntent.putExtra("AlertDialog", "Y");
                        act.startActivity(explicitIntent);
                        act.finish();

                    }
                })
                .show();

    }

    public static void setAlertDialog(Activity act, String msg, String title) {

        if (act != null) {
            if (!((Activity) act).isFinishing()) {
                new SweetAlertDialog(act, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText(title)
                        .setContentText(msg)
                        .show();
            } else {

            }
        }
    }

    public static void setNormalDialog(Context act, String msg, String title) {
        new SweetAlertDialog(act, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(msg)
                .show();

        //RealmObjectController.clearNotificationMessage(act);

    }

    public static boolean setConfirmDialog(final Activity act) {

        new SweetAlertDialog(act, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure want to update?")
                .setConfirmText("Confirm!")
                .setCancelText("Cancel!")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        status = true;
                        sDialog.dismiss();
                    }
                })
                .showCancelButton(true)
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        status = false;
                        sDialog.dismiss();
                    }
                })
                .show();

        return status;
    }

    public ArrayList<DropDownItem> getListOfYear(Activity act) {
        int totalMonth = 12;
        String monthToDisplay;
        ArrayList<DropDownItem> yearList = new ArrayList<DropDownItem>();

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);


        for (int yearX = year; yearX < year + 15; yearX++) {

            DropDownItem itemYear = new DropDownItem();
            itemYear.setText(Integer.toString(yearX));
            itemYear.setCode(Integer.toString(yearX));
            itemYear.setTag("Year");
            yearList.add(itemYear);
        }


        return yearList;

    }

    public ArrayList<DropDownItem> getListOfMonth(Activity act) {
        int totalMonth = 12;
        String monthToDisplay;
        ArrayList<DropDownItem> monthList = new ArrayList<DropDownItem>();

        for (int month = 1; month < totalMonth + 1; month++) {
            if (month < 10) {
                monthToDisplay = "0" + Integer.toString(month);
            } else {
                monthToDisplay = Integer.toString(month);
            }

            DropDownItem itemTitle = new DropDownItem();
            itemTitle.setText(monthToDisplay);
            itemTitle.setCode(monthToDisplay);
            itemTitle.setTag("Month");
            monthList.add(itemTitle);
        }

        return monthList;

    }

    public static void initiateLoading(Activity act) {

        if (progressDialog != null) {
            progressDialog.dismiss();
        }

        progressDialog = new ProgressDialog(act);
        progressDialog.show();
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        /*if (dialog != null) {
            dialog.dismiss();
        }

        dialog = new Dialog(act, R.style.DialogTheme);

        LayoutInflater li = LayoutInflater.from(act);
        final View myView = li.inflate(R.layout.loading_screen, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(act);
        builder.setView(myView);

        dialog.setContentView(myView);
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#CC000000")));
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(lp);
        dialog.show();
*/
    }

    public static void dismissLoading() {

        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                Log.e("Dismiss", "Y");
            }
        }
        Log.e("Dismiss", "N");
    }

    public void popupAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                });

        // Create the AlertDialog object and return it
        builder.create();
        builder.show();
    }


    /*Global PoPup*/
    public void popupSelection(final ArrayList array, final Activity act, final TextView txt, final Boolean tagStatus, View v) {

        prefManager = new SharedPrefManager(act);
        Utils.hideKeyboard(act, v);
        final ArrayList<DropDownItem> a = array;
        DropMenuAdapter dropState = new DropMenuAdapter(act);
        dropState.setItems(a);

        AlertDialog.Builder alertStateCode = new AlertDialog.Builder(act);

        alertStateCode.setSingleChoiceItems(dropState, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selected = a.get(which).getText();
                String selectedCode = a.get(which).getCode();

                txt.setText(selected);
                if (tagStatus) {
                    txt.setTag(selectedCode);
                }

                prefManager.setLanguageCode(selectedCode);

                Intent intent = new Intent(getActivity(), PassCodeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                getActivity().startActivity(intent);

                indexForState = which;

                dialog.dismiss();
            }
        });


        AlertDialog mDialog = alertStateCode.create();
        mDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        //lp.horizontalMargin = 100;
        //lp.verticalMargin = 100;
        mDialog.getWindow().setAttributes(lp);
    }

    /*Global PoPup*/
    public void popupSelectionExtra(final ArrayList array, Activity act, final TextView txt, final Boolean tagStatus, final LinearLayout txt2, final String indicate, final LinearLayout country) {

        prefManager = new SharedPrefManager(act);

        final ArrayList<DropDownItem> a = array;
        DropMenuAdapter dropState = new DropMenuAdapter(act);
        dropState.setItems(a);

        AlertDialog.Builder alertStateCode = new AlertDialog.Builder(act);

        alertStateCode.setSingleChoiceItems(dropState, indexForState, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String selected = a.get(which).getText();
                String selectedCode = a.get(which).getCode();
                txt.setText(selected);
                if (selectedCode.equals(indicate)) {
                    txt2.setVisibility(View.VISIBLE);
                    if (country != null) {
                        country.setVisibility(View.GONE);
                    }

                } else {
                    txt2.setVisibility(View.GONE);
                    if (country != null) {
                        country.setVisibility(View.VISIBLE);
                    }
                }
                if (tagStatus) {
                    txt.setTag(selectedCode);
                } else {
                }

                indexForState = which;

                dialog.dismiss();
            }
        });

        //Utils.hideKeyboard(getActivity(), v);
        AlertDialog mDialog = alertStateCode.create();
        mDialog.show();

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(mDialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = 600;
        mDialog.getWindow().setAttributes(lp);
    }

    public String getTitleCode(Activity act, String title, String data) {

        String titleCode = null;
        JSONArray json = null;

        prefManager = new SharedPrefManager(act);
        HashMap<String, String> init = prefManager.getTitle();
        String dataTitle = init.get(SharedPrefManager.TITLE);

        try {
            json = new JSONArray(dataTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < json.length(); i++) {
            if (data.equals("code")) {
                JSONObject row = (JSONObject) json.opt(i);
                if (title.equals(row.optString("title_name"))) {
                    titleCode = row.optString("title_code");
                }
            } else {
                JSONObject row = (JSONObject) json.opt(i);
                if (title.equals(row.optString("title_code"))) {
                    titleCode = row.optString("title_name");
                }
            }

        }

        return titleCode;
    }


    public String getCountryCode(Activity act, String countryData) {

        String countryCode = null;
        JSONArray json = null;

        prefManager = new SharedPrefManager(act);
        HashMap<String, String> init = prefManager.getCountry();
        String dataCountry = init.get(SharedPrefManager.COUNTRY);

        try {
            json = new JSONArray(dataCountry);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < json.length(); i++) {
            JSONObject row = (JSONObject) json.opt(i);

            if (countryData.equals(row.optString("country_name"))) {
                countryCode = row.optString("country_code");
            }
        }

        return countryCode;

    }

    public JSONArray getState(Activity act) {

        JSONArray json = null;

        prefManager = new SharedPrefManager(act);
        HashMap<String, String> init = prefManager.getState();
        String dataState = init.get(SharedPrefManager.STATE);

        try {
            json = new JSONArray(dataState);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    /*Return month in alphabet*/
    public static String getMonthAlphabet(int month) {
        return new DateFormatSymbols().getShortMonths()[month];
    }

    /*Get All Country From OS*/
    public JSONArray getCountry(Activity act) {
        JSONArray json = null;

        prefManager = new SharedPrefManager(act);
        HashMap<String, String> init = prefManager.getCountry();
        String dataCountry = init.get(SharedPrefManager.COUNTRY);

        try {
            json = new JSONArray(dataCountry);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

		/*Locale[] locales = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();

		for (Locale locale : locales) {
			String country = locale.getDisplayCountry();
			String countryCode = locale.getCountry();


			if (country.trim().length()>0 && !countries.contains(country)) {
				countries.add(country+"-"+countryCode);
			}
		}

		Collections.sort(countries);
		return countries;*/
    }

    /*Get All User Info From OS*/
    public JSONObject getUserInfo(Activity act) {
        JSONObject json = null;

        prefManager = new SharedPrefManager(act);
        HashMap<String, String> init = prefManager.getUserInfo();
        String userInfo = init.get(SharedPrefManager.USER_INFO);

        try {
            json = new JSONObject(userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }

    public static String reformatDOB(String dob) {
        String date;

        if (dob != "") {
            String string = dob;
            String[] parts = string.split("-");
            String year = parts[0];
            String month = parts[1];
            String day = parts[2];
            date = day + "/" + (month) + "/" + year;
        } else {
            date = "";
        }
        return date;
    }

    public static String reformatDOB3(String dob) {
        String date;

        if (dob != "") {
            String string = dob;
            String[] parts = string.split("-");

            String year = parts[2];
            Log.e(dob, year);

            String month = parts[1];
            String day = parts[0];
            date = day + "/" + (month) + "/" + year;
        } else {
            date = "";
        }
        return date;
    }

    public String reformatDOB2(String dob) {
        String date;
        String string = dob;
        String[] parts = string.split("/");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];
        date = year + "-" + month + "-" + day;

        return date;
    }

    public String reformatDOB4(String dob) {
        String date;
        String string = dob;
        String[] parts = string.split("/");
        String day = parts[0];
        String month = parts[1];
        String year = parts[2];
        date = day + "-" + month + "-" + year;

        return date;
    }


    /*Get All User Info From OS*/
    public JSONObject getCheckinInfo(Activity act) {
        JSONObject json = null;

        prefManager = new SharedPrefManager(act);
        HashMap<String, String> init = prefManager.getCheckinInfo();
        String checkinInfo = init.get(SharedPrefManager.CHECKIN_INFO);


        try {
            json = new JSONObject(checkinInfo);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        aq = new com.app.comic.base.AQuery(getActivity());
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());


    }

}
