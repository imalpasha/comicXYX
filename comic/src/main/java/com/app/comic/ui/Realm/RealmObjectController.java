package com.app.comic.ui.Realm;

import android.app.Activity;
import android.content.Context;

import com.app.comic.base.BaseFragment;
import com.app.comic.ui.Model.JSON.BookmarkJSON;
import com.app.comic.ui.Model.JSON.ComicInfoD;
import com.app.comic.ui.Model.JSON.NotificationMessage;
import com.app.comic.ui.Model.JSON.TokenInfoJSON;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by Dell on 2/11/2016.
 */
public class RealmObjectController extends BaseFragment {


    public static Realm getRealmInstance(Activity act) {

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(act).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e) {
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex) {
                throw ex;
                //No Realm file to remove.
            }
        }
    }

    public static Realm getRealmInstanceContext(Context act) {

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(act).deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);

        try {
            return Realm.getInstance(realmConfiguration);
        } catch (RealmMigrationNeededException e) {
            try {
                Realm.deleteRealm(realmConfiguration);
                //Realm file has been deleted.
                return Realm.getInstance(realmConfiguration);
            } catch (Exception ex) {
                throw ex;
                //No Realm file to remove.
            }
        }
    }

    public static RealmResults<NotificationMessage> getNotificationMessage(Activity act) {

        Realm realm = getRealmInstance(act);
        final RealmResults<NotificationMessage> result = realm.where(NotificationMessage.class).findAll();

        return result;
    }

    public static void saveNotificationMessage(Context act, String message ,String title) {
        Realm realm = getRealmInstanceContext(act);
        realm.beginTransaction();
        NotificationMessage realmObject = realm.createObject(NotificationMessage.class);
        realmObject.setMessage(message);
        realmObject.setTitle(title);
        realm.commitTransaction();

    }

    public static void clearNotificationMessage(Context act) {

        Realm realm = getRealmInstanceContext(act);

        final RealmResults<NotificationMessage> result = realm.where(NotificationMessage.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

    }

    /*Save user info*/
    public static void setTokenInfo(Activity act, String stringfyObj) {

        Realm realm = getRealmInstance(act);

        //clear user info in realm first.
        final RealmResults<TokenInfoJSON> result = realm.where(TokenInfoJSON.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

        //add new user info in realm
        realm.beginTransaction();
        TokenInfoJSON realmObject = realm.createObject(TokenInfoJSON.class);
        realmObject.setTokenInfo(stringfyObj);
        realm.commitTransaction();
        realm.close();
    }


    /*Save user info*/
    public static void saveBookmark(Activity act, String stringfyObj) {

        Realm realm = getRealmInstance(act);

        //clear user info in realm first.
        final RealmResults<BookmarkJSON> result = realm.where(BookmarkJSON.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

        //add new user info in realm
        realm.beginTransaction();
        BookmarkJSON realmObject = realm.createObject(BookmarkJSON.class);
        realmObject.setBookmark(stringfyObj);
        realm.commitTransaction();
        realm.close();
    }

    public static void clearBookmark(Activity act) {

        Realm realm = getRealmInstance(act);

        //clear user info in realm first.
        final RealmResults<BookmarkJSON> result = realm.where(BookmarkJSON.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();
        realm.close();
    }





   /* public static void cachedResult(Activity act, String cachedResult) {

        Realm realm = getRealmInstance(act);

        final RealmResults<CachedResult> result = realm.where(CachedResult.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

        realm.beginTransaction();
        CachedResult realmObject = realm.createObject(CachedResult.class);
        //realmObject.setCachedAPI(cachedApi);
        realmObject.setCachedResult(cachedResult);
        realm.commitTransaction();

    }*/

    //comic D

    /*Save user info*/
    public static void setComicD(Activity act, String stringfyObj) {

        Realm realm = getRealmInstance(act);

        //clear user info in realm first.
        final RealmResults<ComicInfoD> result = realm.where(ComicInfoD.class).findAll();
        realm.beginTransaction();
        result.clear();
        realm.commitTransaction();

        //add new user info in realm
        realm.beginTransaction();
        ComicInfoD realmObject = realm.createObject(ComicInfoD.class);
        realmObject.setComicD(stringfyObj);
        realm.commitTransaction();
        realm.close();
    }


}
