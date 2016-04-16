package id.kopilet.app.sunshine.network;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.realm.RealmObject;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by rieftux on 02/04/16.
 */
public final class WeatherService {

    public static final String API_URL = "http://api.openweathermap.org/data/2.5/forecast/";

    private Retrofit mRetrofit;
    private RestInterface mInterface;

    private Gson mGson;

    public WeatherService() {
        mGson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create(mGson))
                .build();
        mInterface = mRetrofit.create(RestInterface.class);
    }

    public RestInterface getService() {
        return mInterface;
    }

}
