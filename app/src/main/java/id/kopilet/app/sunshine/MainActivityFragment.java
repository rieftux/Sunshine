package id.kopilet.app.sunshine;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.kopilet.app.sunshine.model.ApiResponse;
import id.kopilet.app.sunshine.model.Weather;
import id.kopilet.app.sunshine.network.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    private Context mContext;
    private ArrayAdapter<String> mWeekForecastAdapter;
    private List<String> mWeekForecastList = new ArrayList<>();

    public MainActivityFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listViewForecast = (ListView) rootView.findViewById(R.id.listview_forecast);

        mWeekForecastAdapter = new ArrayAdapter<>(mContext,
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                mWeekForecastList);

        listViewForecast.setAdapter(mWeekForecastAdapter);

        loadData();

        return rootView;
    }


    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService.OpenWeatherMap service = retrofit
                .create(WeatherService.OpenWeatherMap.class);

        Call<ApiResponse> listCall = service.getWeather("Bandung",
                7, BuildConfig.OPEN_WEATHER_MAP_API_KEY);

        listCall.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call,
                                   Response<ApiResponse> response) {

                if (response.isSuccessful()) {
                    ApiResponse apiResponse = response.body();

                    for (int i = 0; i < apiResponse.getList().size(); i++) {
                        Weather weather = apiResponse.getList().get(i);
                        String strWeather = weather.getWeather().get(0).getMain() + " - " + weather.getTemp().getMax();

                        mWeekForecastList.add(strWeather);
                        Log.v(LOG_TAG, strWeather);
                    }

                    mWeekForecastAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call,
                                  Throwable t) {

                Log.v(LOG_TAG, t.getMessage());
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
