package id.kopilet.app.sunshine.utils;

import id.kopilet.app.sunshine.model.ApiContent;
import id.kopilet.app.sunshine.model.WeatherRealm;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by rieftux on 14/04/16.
 */
public class DataManager {

    private Realm mRealm;
    private RealmResults<ApiContent> mResults;

    public DataManager() {
        mRealm = Realm.getDefaultInstance();
    }

    public boolean checkRecord() {
        mResults = mRealm.where(ApiContent.class).findAll();
        return mResults.size() != 0;
    }

    public RealmResults<ApiContent> dataAll() {
        mResults = mRealm.where(ApiContent.class).findAll();
        return mResults;
    }

    public static boolean existsRecord() {
//        Realm realm = Realm.getInstance(context);
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ApiContent> contents = realm.allObjects(ApiContent.class);
        return contents.size() != 0;
    }

    public ApiContent findData() {
//        Realm realm = Realm.getInstance(context);
//        Realm realm = Realm.getDefaultInstance();
//        RealmResults<ApiContent> apiResponses = realm.allObjects(ApiContent.class);

        // Build the query looking at all object:
        RealmQuery<ApiContent> query = mRealm.where(ApiContent.class);
        //Execute the query
        RealmResults<ApiContent> results = query.findAll();

        return results.first();
    }

    public static WeatherRealm findWeatherAt(int idx) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<ApiContent> results = realm.where(ApiContent.class).findAll();
        WeatherRealm singleWeather = results.first().getList().get(idx);

        return  singleWeather;
    }



}
