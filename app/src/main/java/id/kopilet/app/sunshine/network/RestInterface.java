package id.kopilet.app.sunshine.network;

import id.kopilet.app.sunshine.model.ApiContent;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rieftux on 14/04/16.
 */
public interface RestInterface {

    @GET("daily?mode=json")
    Call<ApiContent> getWeather(
            @Query("q") String city,
            @Query("units") String units,
            @Query("cnt") int count,
            @Query("APPID") String appid);
}
