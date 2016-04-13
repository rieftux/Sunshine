package id.kopilet.app.sunshine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import id.kopilet.app.sunshine.model.ApiResponse;
import id.kopilet.app.sunshine.model.Weather;
import id.kopilet.app.sunshine.network.WeatherService;
import id.kopilet.app.sunshine.utils.Utility;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListForecastFragment extends Fragment {

    private final String LOG_TAG = ListForecastFragment.class.getSimpleName();

    private Context mContext;
    private ArrayAdapter<String> mWeekForecastAdapter;
    private List<String> mWeekForecastList = new ArrayList<>();

    public ListForecastFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecast_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            loadData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final ListView listViewForecast = (ListView) rootView.findViewById(R.id.listview_forecast);

        mWeekForecastAdapter = new ArrayAdapter<>(mContext,
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                mWeekForecastList);

        listViewForecast.setAdapter(mWeekForecastAdapter);
        listViewForecast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedWeather = mWeekForecastAdapter.getItem(position);
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, selectedWeather);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }

    private void loadData() {

        // First clear adapter from list (if any)
        mWeekForecastAdapter.clear();

        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(mContext);

        // get location from setting
        String location = Utility.getPreferredLocation(getActivity());

        // get units from setting
        String units = sharedPreferences.getString(getString(R.string.pref_units_key),
                getString(R.string.pref_units_metric));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WeatherService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        WeatherService.OpenWeatherMap service = retrofit
                .create(WeatherService.OpenWeatherMap.class);

        Call<ApiResponse> listCall = service.getWeather(location, units,
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
                } else {
                    Toast.makeText(mContext, "Gagal Ambil GSON", Toast.LENGTH_LONG).show();
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
