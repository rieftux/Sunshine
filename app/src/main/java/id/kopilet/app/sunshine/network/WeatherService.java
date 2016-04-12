package id.kopilet.app.sunshine.network;

import id.kopilet.app.sunshine.model.ApiResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by rieftux on 02/04/16.
 */
public final class WeatherService {

    public static final String API_URL = "http://api.openweathermap.org/data/2.5/forecast/";

//    api.openweathermap.org/data/2.5/forecast/daily?q=94043&mode=json&units=metric&cnt=7&appid=0259fb6b6603b0171e464769fa223449

    public interface OpenWeatherMap {

        @GET("daily?mode=json&units=metric")
        Call<ApiResponse> getWeather(
                @Query("q") String city,
                @Query("cnt") int count,
                @Query("APPID") String appid);
    }
}
