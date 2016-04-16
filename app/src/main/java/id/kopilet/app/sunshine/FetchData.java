package id.kopilet.app.sunshine;

import android.content.Context;
import android.util.Log;

import id.kopilet.app.sunshine.model.ApiContent;
import id.kopilet.app.sunshine.network.WeatherService;
import io.realm.Realm;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rieftux on 15/04/16.
 */
public class FetchData {

    private final String LOG_TAG = FetchData.class.getSimpleName();
    private WeatherService mService;
    private Call<ApiContent> mCall;
    private Context mContext;
    private Realm mRealm;

    public FetchData() {
        mRealm = Realm.getDefaultInstance();
        mService = new WeatherService();
    }

    public FetchData(Realm realm) {
        mRealm = realm;
    }

    public FetchData(Context context, Realm realm) {
        mContext = context;
        mRealm = realm;
    }

    public void getWeatherData(String location, String unit) {
        mCall = mService.getService().getWeather(location, unit, 7, BuildConfig.OPEN_WEATHER_MAP_API_KEY);
        mCall.enqueue(new Callback<ApiContent>() {
            @Override
            public void onResponse(Call<ApiContent> call, final Response<ApiContent> response) {
                Log.v(LOG_TAG, "Success response: " + response.message());

                if (response.isSuccessful()) {

                    mRealm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm bgRealm) {
                            ApiContent contentResponse = response.body();
                            bgRealm.where(ApiContent.class).findAll().clear();
                            bgRealm.copyToRealmOrUpdate(contentResponse);

                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            // Transaction was a success.
                            Log.v(LOG_TAG, "SUCCESS REALM TRX");

                    /*        DataManager manager = new DataManager();
                            RealmResults<ApiContent> resultRealm = manager.dataAll();
                            List<WeatherRealm> list = resultRealm.first().getList();
                            for (WeatherRealm w : list) {
                                String weatherString = Utility.getReadableDateString(w.getDt()) + " - " + w.getWeather().get(0).getMain()
                                        + " - " + Utility.formatHighLows(w.getTemp().getMax(), w.getTemp().getMin());
                                Log.v(LOG_TAG, "Data realm: " + weatherString);
                            }*/

//                            mRealm.close();
                        }
                    }, new Realm.Transaction.OnError() {
                        @Override
                        public void onError(Throwable error) {
                            // Transaction failed and was automatically canceled.
                            Log.v(LOG_TAG, "ERROR TRX: " + error.getMessage());
//                            mRealm.close();
                        }
                    });

                }
            }

            @Override
            public void onFailure(Call<ApiContent> call, Throwable t) {
                Log.v(LOG_TAG, "Error response: " + t.getMessage());
            }
        });
    }

}
