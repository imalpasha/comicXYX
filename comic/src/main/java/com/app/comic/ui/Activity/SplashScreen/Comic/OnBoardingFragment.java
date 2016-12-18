package com.app.comic.ui.Activity.SplashScreen.Comic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.comic.Controller;
import com.app.comic.R;
import com.app.comic.application.MainApplication;
import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Activity.FragmentContainerActivity;
import com.app.comic.ui.Activity.SplashScreen.PassCodeActivity;
import com.app.comic.ui.Model.JSON.Bookmark;
import com.app.comic.ui.Model.JSON.ComicInfoD;
import com.app.comic.ui.Model.JSON.TokenInfoJSON;
import com.app.comic.ui.Model.Receive.Comic.AuthReceive;
import com.app.comic.ui.Model.Receive.Comic.ComicReceive;
import com.app.comic.ui.Module.OnBoardingFragmentModule;
import com.app.comic.ui.Presenter.HomePresenter;
import com.app.comic.ui.Realm.RealmObjectController;
import com.app.comic.utils.SharedPrefManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.google.gson.Gson;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.androidquery.util.AQUtility.post;

public class OnBoardingFragment extends BaseFragment implements HomePresenter.ComicOptionView {

    @Inject
    HomePresenter presenter;

    @InjectView(R.id.pager)
    ViewPager pager;

    @InjectView(R.id.btnExit)
    ImageView btnExit;

    @InjectView(R.id.btnHome)
    ImageView btnHome;

    @InjectView(R.id.btnShare)
    ImageView btnShare;

    @InjectView(R.id.share_able)
    ImageView share_able;

    private int fragmentContainerId;
    private SharedPrefManager pref;
    private static Boolean lastPosition = false;
    ComicReceive comicReceive;
    private String optionLevel = "0";
    private int close = 0, secondaryClose = 0;
    String bookmarkPosition;
    int b = 0;
    String token;
    Boolean fromBookmark = false;
    String checkFromBookMark = "N";
    private Boolean reachingFirst = false;
    int intBookmarkPosition = 0;
    int l = 0;
    private Boolean proceed = false;
    int pagesDownloaded = 0;
    int totalToDownload = 0;
    //PhotoViewAttacher mAttacher;

    public static OnBoardingFragment newInstance(Bundle bundle) {

        OnBoardingFragment fragment = new OnBoardingFragment();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(getActivity()).createScopedGraph(new OnBoardingFragmentModule(this)).inject(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.on_boarding, container, false);
        ButterKnife.inject(this, view);
        pref = new SharedPrefManager(getActivity());
        Bundle bundle = getArguments();

        try {
            if (bundle.containsKey("OPTION_LEVEL")) {
                optionLevel = bundle.getString("OPTION_LEVEL");
            }
        } catch (Exception e) {

        }

        try {
            if (bundle.containsKey("POSITION")) {
                bookmarkPosition = bundle.getString("POSITION");
            }
        } catch (Exception e) {

        }

        try {
            if (bundle.containsKey("FROM_BOOKMARK")) {
                checkFromBookMark = bundle.getString("FROM_BOOKMARK");
            }
        } catch (Exception e) {

        }


        //getActivity().setTitle("STORY MORRY");
        btnHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                home();
                getActivity().finish();
            }
        });

        //getActivity().setTitle("STORY MORRY");
        btnExit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exitApp();
            }
        });

        //loadImage
        //loadImage();
        Realm realm = RealmObjectController.getRealmInstance(getActivity());
        final RealmResults<ComicInfoD> result2 = realm.where(ComicInfoD.class).findAll();
        comicReceive = (new Gson()).fromJson(result2.get(0).getComicD(), ComicReceive.class);

        final RealmResults<TokenInfoJSON> result3 = realm.where(TokenInfoJSON.class).findAll();
        AuthReceive authReceive = (new Gson()).fromJson(result3.get(0).getTokenInfo(), AuthReceive.class);
        token = authReceive.getToken();

        totalToDownload = comicReceive.getData().getPages().size() + comicReceive.getData().getNext_level_options().size();

        //startPagination(comicReceive);
        //loadImage();

        //chech shareable
        if (comicReceive.getData().getPages().size() > 0) {
            if (comicReceive.getData().getPages().get(0).getIs_shareable().equals("1")) {
                btnShare.setVisibility(View.VISIBLE);
            } else {
                btnShare.setVisibility(View.GONE);
            }
        }

        startPagination(comicReceive);

        return view;
    }

    public void loadImage() {
        //get current comic

        initiateLoading(getActivity());

        for (int x = 0; x < comicReceive.getData().getPages().size(); x++) {
            Log.e("Download pages image", comicReceive.getData().getPages().get(x).getImage_name());
            String path = comicReceive.getData().getPages().get(x).getImage_name();
            proceed = false;
            download(path);
        }

        if (comicReceive.getData().getNext_level_options().size() > 0) {
            for (int x = 0; x < comicReceive.getData().getNext_level_options().size(); x++) {
                String path = comicReceive.getData().getNext_level_options().get(x).getImage_name();
                proceed = false;
                download(path);

            }
        }

    }

    public void download(String path) {

        Glide.with(this)
                .load(path)
                .downloadOnly(new SimpleTarget<File>() {
                    @Override
                    public void onResourceReady(File resource, GlideAnimation<? super File> glideAnimation) {
                        // LOGGER.debug("Photo downloaded");
                        // dismissLoading();
                        Log.e("Done", "Done");
                        proceed = true;
                        pagesDownloaded++;
                        checkAllDownLoaded();
                    }
                });
    }

    public void checkAllDownLoaded() {
        Log.e(Integer.toString(pagesDownloaded), Integer.toString(totalToDownload));
        if (pagesDownloaded == totalToDownload) {
            startPagination(comicReceive);
            dismissLoading();
        }
    }

    public void home() {

        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(".HOMEPAGE.")
                .setContentText("Back to homepage?")
                .showCancelButton(true)
                .setCancelText("Cancel")
                .setConfirmText("Confirm")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {

                        getActivity().finish();
                        //getActivity().finishAffinity();
                        //System.exit(0);

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

    public void exitApp() {

        new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getResources().getString(R.string.comic_exit))
                .setContentText(getResources().getString(R.string.bookmark_confirm))
                .showCancelButton(true)
                .setCancelText(getResources().getString(R.string.bookmark_no))
                .setConfirmText(getResources().getString(R.string.bookmark_yes))
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {


                        Bookmark bookmark = new Bookmark();
                        bookmark.setCharacter(comicReceive.getData().getPages().get(b).getCharacter());
                        bookmark.setLevel(comicReceive.getData().getPages().get(b).getLevel());
                        bookmark.setOption(comicReceive.getData().getPages().get(b).getOption());
                        bookmark.setPosition(Integer.toString(intBookmarkPosition));

                        Log.e("Level", comicReceive.getData().getPages().get(b).getLevel());
                        Log.e("Character", comicReceive.getData().getPages().get(b).getCharacter());
                        Log.e("Option", comicReceive.getData().getPages().get(b).getOption());
                        Log.e("Position", Integer.toString(intBookmarkPosition));

                        Gson gsonUserInfo = new Gson();
                        String bookmarkInfo = gsonUserInfo.toJson(bookmark);
                        RealmObjectController.saveBookmark(getActivity(), bookmarkInfo);


                        //Intent intent = new Intent(getActivity(), PassCodeActivity.class);
                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                        //getActivity().startActivity(intent);
                        //getActivity().finish();

                        getActivity().finish();
                        getActivity().finishAffinity();
                        System.exit(0);

                    }
                })
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        getActivity().finish();
                        getActivity().finishAffinity();
                        System.exit(0);
                    }
                })
                .show();


    }

    public void startPagination(final ComicReceive comicReceiveSP) {

        //use vector view
        LayoutInflater innerLayout = LayoutInflater.from(getActivity());

        final Vector<View> innerPages = new Vector<View>();
        for (int c = 0; c < comicReceiveSP.getData().getPages().size(); c++) {
            l = c;
            View insideComic = innerLayout.inflate(R.layout.comic_view, null);

            final ImageView onboarding_hidden = (ImageView) insideComic.findViewById(R.id.onboarding_hidden);
            //final RelativeLayout storyLayout = (RelativeLayout) insideComic.findViewById(R.id.storyLayout);
            TextView txtID = (TextView) insideComic.findViewById(R.id.txtID);
            final ProgressBar comicLoading = (ProgressBar) insideComic.findViewById(R.id.comicLoading);
            //final SubsamplingScaleImageView onboarding_image = (SubsamplingScaleImageView) insideComic.findViewById(R.id.onboarding_image);
            //final ImageView btnBookmark = (ImageView) insideComic.findViewById(R.id.btnBookmark);
            //final ImageView btnShare = (ImageView) insideComic.findViewById(R.id.btnShare);
            //final ImageView share_able = (ImageView) insideComic.findViewById(R.id.share_able);
            //btnBookmark.setTag(Integer.toString(c));

            /*Glide.with(this)
                    .load(comicReceiveSP.getData().getPages().get(c).getImage_name())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(new BitmapImageViewTarget(onboarding_hidden) {
                        @Override
                        public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            onboarding_image.setImage(ImageSource.bitmap(drawable));
                            onboarding_image.setVisibility(View.VISIBLE);

                            onboarding_hidden.setVisibility(View.GONE);
                            Uri uri = test(onboarding_hidden);
                            Log.e("Uri", uri.toString());
                        }
                    });*/
            Glide.with(this)
                    .load(comicReceiveSP.getData().getPages().get(c).getImage_name())
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .fitCenter()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            comicLoading.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(onboarding_hidden);


            // mAttacher = new PhotoViewAttacher(onboarding_hidden);
            // mAttacher.update();

            txtID.setText(comicReceiveSP.getData().getPages().get(c).getPage_id());

            if (comicReceiveSP.getData().getPages().get(c).getIs_shareable().equals("1")) {

                Log.e("This Page Shareable" + Integer.toString(c), comicReceiveSP.getData().getPages().get(c).getShareable_image());
                /*Glide.with(this)
                        .load(comicReceiveSP.getData().getPages().get(c).getShareable_image())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(share_able);*/


                /*btnShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Glide.with(getActivity())
                                .load(comicReceiveSP.getData().getPages().get(l).getShareable_image())
                                .asBitmap()
                                //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .into(new BitmapImageViewTarget(share_able) {
                                    @Override
                                    public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                        super.onResourceReady(drawable, anim);
                                        share_able.setVisibility(View.GONE);

                                        Uri uri = test(share_able);
                                        Log.e("URI123", uri.toString());

                                        Intent shareIntent = new Intent();
                                        shareIntent.setAction(Intent.ACTION_SEND);
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                        shareIntent.setType("image/*");
                                        // Launch sharing dialog for image
                                        startActivity(Intent.createChooser(shareIntent, "Share Image"));
                                    }
                                });


                    }

                });

            } else {
                btnShare.setVisibility(View.GONE);
            }*/

            /*btnBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(getResources().getString(R.string.bookmark))
                            .setContentText(getResources().getString(R.string.bookmark_confirm))
                            .showCancelButton(true)
                            .setCancelText(getResources().getString(R.string.bookmark_no))
                            .setConfirmText(getResources().getString(R.string.bookmark_yes))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {


                                    Bookmark bookmark = new Bookmark();
                                    bookmark.setCharacter(comicReceiveSP.getData().getPages().get(b).getCharacter());
                                    bookmark.setLevel(comicReceiveSP.getData().getPages().get(b).getLevel());
                                    bookmark.setOption(comicReceiveSP.getData().getPages().get(b).getOption());
                                    bookmark.setPosition(btnBookmark.getTag().toString());

                                    Log.e("Level", comicReceiveSP.getData().getPages().get(b).getLevel());
                                    Log.e("Character", comicReceiveSP.getData().getPages().get(b).getCharacter());
                                    Log.e("Option", comicReceiveSP.getData().getPages().get(b).getOption());
                                    Log.e("Position", btnBookmark.getTag().toString());

                                    Gson gsonUserInfo = new Gson();
                                    String bookmarkInfo = gsonUserInfo.toJson(bookmark);
                                    RealmObjectController.saveBookmark(getActivity(), bookmarkInfo);


                                    Intent intent = new Intent(getActivity(), PassCodeActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                    getActivity().startActivity(intent);
                                    getActivity().finish();
                                    //getActivity().finish();
                                    //getActivity().finishAffinity();
                                    //System.exit(0);

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

            });*/

            }
            innerPages.add(insideComic);
        }


        if (comicReceiveSP.getData().getNext_level_options().size() > 0) {

            LinearLayout.LayoutParams matchParent = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);

            View insideComicOption = innerLayout.inflate(R.layout.comic_option_view, null);
            LinearLayout appendImage = (LinearLayout) insideComicOption.findViewById(R.id.appendImage);

            for (int x = 0; x < comicReceiveSP.getData().getNext_level_options().size(); x++) {

                final LinearLayout flightType = new LinearLayout(getActivity());
                flightType.setWeightSum(1);
                flightType.setPadding(10, 5, 10, 5);
                flightType.setLayoutParams(matchParent);
                flightType.setGravity(Gravity.CENTER | Gravity.TOP);
                flightType.setTag(comicReceiveSP.getData().getNext_level_options().get(x).getOption() + "/" + comicReceiveSP.getData().getNext_level_options().get(x).getLevel() + "/" + comicReceiveSP.getData().getNext_level_options().get(x).getCharacter());


                final ImageView optionImage = new ImageView(getActivity());
                //optionImage.setPadding(4, 4, 4, 4);
                //optionImage.setImageResource(optionList[x]);
                Log.e("Comic Name", comicReceiveSP.getData().getNext_level_options().get(x).getImage_name());
                Glide.with(this)
                        .load(comicReceiveSP.getData().getNext_level_options().get(x).getImage_name())
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .fitCenter()
                        .into(optionImage);

                //set border
               /* optionImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                            optionImage.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.image_option_border));
                        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                            optionImage.setBackground(null);
                        }

                        return false;
                    }
                });*/

                flightType.addView(optionImage);
                flightType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        new SweetAlertDialog(getActivity(), SweetAlertDialog.NORMAL_TYPE)
                                .setTitleText(getResources().getString(R.string.comic_proceed))
                                //.setContentText(getResources().getString(R.string.))
                                .showCancelButton(true)
                                .setCancelText(getResources().getString(R.string.bookmark_no))
                                .setConfirmText(getResources().getString(R.string.bookmark_yes))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {

                                        String string = flightType.getTag().toString();
                                        String[] parts = string.split("/");

                                        //HomeFragment.requestComic(getActivity(), level, option);
                                        HashMap<String, String> params = new HashMap<String, String>();
                                        params.put("character", parts[2]);
                                        params.put("level", parts[1]);
                                        params.put("option", parts[0]);
                                        params.put("token", token);

                                        initiateLoading(getActivity());
                                        presenter.onComicRequest(params);
                                        sDialog.dismiss();

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

                });

                appendImage.addView(flightType);

            }

            innerPages.add(insideComicOption);
        } else {

            View insideComicDone = innerLayout.inflate(R.layout.comic_view_done, null);
            innerPages.add(insideComicDone);

        }

        BaggageInnerListAdapter innerAdapter = new BaggageInnerListAdapter(getActivity(), innerPages);
        pager.setAdapter(innerAdapter);

        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {

            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                comic_page_flip(getActivity());
                Log.e("onPageScrollState", Integer.toString(position) + "-" + Float.toString(positionOffset) + "-" + Integer.toString(positionOffsetPixels));
                close++;
                if (close > 1 && !optionLevel.equals("0") && position == 0 && positionOffsetPixels == 0) {
                    if (bookmarkPosition == null) {

                        Log.e("Previous page","False_1");
                        /*
                        if (!comicReceive.getData().getPages().get(0).getLevel().equals("1")) {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("character", comicReceive.getData().getPages().get(0).getCharacter());
                            params.put("level", comicReceive.getData().getPrevious_level());
                            params.put("option", comicReceive.getData().getPrevious_option());
                            params.put("token", token);
                            fromBookmark = true;
                            initiateLoading(getActivity());
                            presenter.onComicRequest(params);
                        } else {
                            //getActivity().finish();
                        }
                        */
                    } else {

                        Log.e("Previous page","False_2");
                        /*
                        HashMap<String, String> params = new HashMap<String, String>();
                        params.put("character", comicReceive.getData().getPages().get(0).getCharacter());
                        params.put("level", comicReceive.getData().getPrevious_level());
                        params.put("option", comicReceive.getData().getPrevious_option());
                        params.put("token", token);
                        fromBookmark = true;
                        initiateLoading(getActivity());
                        presenter.onComicRequest(params);
                        */
                    }
                } else {
                    Log.e("secondaryClose inc", Integer.toString(secondaryClose));
                    Log.e("secondaryClose", Integer.toString(secondaryClose));

                    if (reachingFirst && position == 0 && positionOffset == 0 && positionOffsetPixels == 0) {
                        secondaryClose++;
                    }

                    if (secondaryClose > 2 && bookmarkPosition != null && optionLevel.equals("0") && position == 0 && positionOffset == 0 && positionOffsetPixels == 0) {

                        Log.e("Previous page","False_3");
                        /*
                        if (!comicReceive.getData().getPages().get(0).getLevel().equals("1")) {
                            HashMap<String, String> params = new HashMap<String, String>();
                            params.put("character", comicReceive.getData().getPages().get(0).getCharacter());
                            params.put("level", comicReceive.getData().getPrevious_level());
                            params.put("option", comicReceive.getData().getPrevious_option());
                            params.put("token", token);
                            fromBookmark = true;
                            initiateLoading(getActivity());
                            presenter.onComicRequest(params);
                        }
                        */

                    }
                }
            }

            public void onPageSelected(final int position) {
                Log.e("onPageSelected", Integer.toString(position));
                if (position == 0) {
                    reachingFirst = true;
                    secondaryClose++;
                }

                if (position == comicReceive.getData().getPages().size()) {
                    lastPosition = true;
                    Log.e("LastPosition", "true");
                }

                intBookmarkPosition = position;

                try {
                    if (comicReceiveSP.getData().getPages().size() > 0) {
                        if (comicReceiveSP.getData().getPages().get(position).getIs_shareable().equals("1")) {
                            btnShare.setVisibility(View.VISIBLE);
                            btnShare.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.e("BTN SHARE", "CLick");
                                    initiateLoading(getActivity());
                                    Glide.with(getActivity())
                                            .load(comicReceiveSP.getData().getPages().get(position).getShareable_image())
                                            .asBitmap()
                                            //.diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                            .into(new BitmapImageViewTarget(share_able) {
                                                @Override
                                                public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                                                    super.onResourceReady(drawable, anim);
                                                    Log.e("Done Download", "Yes");
                                                    dismissLoading();

                                                    share_able.setVisibility(View.GONE);
                                                    Uri uri = test(share_able);
                                                    Log.e("URI123", uri.toString());

                                                    Intent shareIntent = new Intent();
                                                    shareIntent.setAction(Intent.ACTION_SEND);
                                                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                                    shareIntent.setType("image/*");
                                                    // Launch sharing dialog for image
                                                    startActivity(Intent.createChooser(shareIntent, "Share Image"));
                                                }
                                            });


                                }

                            });
                        } else {
                            btnShare.setVisibility(View.GONE);
                        }
                    }
                } catch (Exception e) {
                    Log.e("e", e.getMessage());
                }


            }
        });

        if (bookmarkPosition != null) {
            pager.setCurrentItem(Integer.parseInt(bookmarkPosition));
            //clear bookmark
            RealmObjectController.clearBookmark(getActivity());
        }

        if (checkFromBookMark.equals("Y")) {
            pager.setCurrentItem(innerPages.size() - 1);
        }

    }

    public Uri test(ImageView img) {
        //ImageView siv = (ImageView) findViewById(R.id.ivResult);
        Drawable mDrawable = img.getDrawable();
        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(),
                mBitmap, "Image Description", null);

        Uri uri = Uri.parse(path);
        return uri;
    }

    public static Boolean getLastPosition() {
        return lastPosition;
    }

    @Override
    public void onComicReceive(ComicReceive obj) {

        dismissLoading();
        Log.e("XX", "XX");

        Boolean status = Controller.getRequestStatus(obj.getStatus(), "test", getActivity());
        if (status) {

            Gson tokenInfo = new Gson();
            String comicD = tokenInfo.toJson(obj);
            RealmObjectController.setComicD(getActivity(), comicD);

            Intent onBoard = new Intent(getActivity(), OnBoardingActivity.class);
            if (fromBookmark) {
                onBoard.putExtra("FROM_BOOKMARK", "Y");
            }
            onBoard.putExtra("OPTION_LEVEL", "10");
            getActivity().startActivity(onBoard);
            getActivity().finish();


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
}
