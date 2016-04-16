package id.kopilet.app.sunshine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import id.kopilet.app.sunshine.model.ApiContent;
import id.kopilet.app.sunshine.model.WeatherRealm;
import id.kopilet.app.sunshine.utils.DataManager;
import id.kopilet.app.sunshine.utils.Utility;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class ListForecastFragment extends Fragment {

    protected MyApplication app;

    private final String LOG_TAG = ListForecastFragment.class.getSimpleName();

    private Context mContext;

    private ArrayAdapter<String> mWeekForecastAdapter;
    private List<String> mWeekForecastList = new ArrayList<>();

    private Realm mRealm;

    private ListView listViewForecast;

    private ForecastAdapter mForecastAdapter;
    private List<WeatherRealm> mWeatherRealmsList;

    public ListForecastFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        app = (MyApplication) mContext.getApplicationContext();
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
            updateData();
            return true;
        }

        if (id == R.id.action_load_db) {
            loadDatabase();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        final ListView listViewForecast = (ListView) rootView.findViewById(R.id.listview_forecast);

        listViewForecast = (ListView) rootView.findViewById(R.id.listview_forecast);

        List<String> weekForecastString = new ArrayList<>();
        weekForecastString.add("Raining - 25 - 2-4-16");
        weekForecastString.add("Raining - 20 - 3-4-16");
        weekForecastString.add("Raining - 22 - 4-4-16");
        weekForecastString.add("Hot - 27 - 5-4-16");
        weekForecastString.add("Very Hot - 30 - 6-4-16");
        weekForecastString.add("Very Hot - 33 - 7-4-16");
        weekForecastString.add("Very Hot - 33 - 8-4-16");

        mWeekForecastAdapter = new ArrayAdapter<>(mContext,
                R.layout.list_item_forecast,
                R.id.txtview_forecast,
                weekForecastString);


//        mForecastAdapter = new ForecastAdapter(mContext, mWeatherRealmResults, true);

        mForecastAdapter = new ForecastAdapter(mContext, R.layout.list_item_forecast);

//        listViewForecast.setAdapter(mWeekForecastAdapter);

        listViewForecast.setAdapter(mForecastAdapter);

        listViewForecast.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selectedWeather = mForecastAdapter.getItem(position).toString();
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(Intent.EXTRA_TEXT, position);
                startActivity(intent);
            }
        });

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateData();
    }

    @Override
    public void onStop() {
        super.onStop();
        mRealm.close();
    }

    private void updateData() {
        // get location from setting
        String location = Utility.getPreferredLocation(getActivity());
        // get unit from setting
        String unit;
        if (Utility.isMetric(getActivity()))
            unit = getString(R.string.pref_units_metric);
        else
            unit = getString(R.string.pref_units_imperial);

        FetchData fetchData = new FetchData();
        fetchData.getWeatherData(location, unit);
    }

    private void loadDatabase() {

        Log.v(LOG_TAG, "LoadFromDatabase");
        mForecastAdapter.clear();

        DataManager dataManager = new DataManager();

        if (dataManager.checkRecord()) {
            RealmResults<ApiContent> contents = dataManager.dataAll();

            mWeatherRealmsList = contents.first().getList();

//            mWeatherRealmsList = new ArrayList<>(contents.first().getList());

            for (WeatherRealm w : mWeatherRealmsList) {
                String weatherString = Utility.getReadableDateString(w.getDt()) + " - " + w.getWeather().get(0).getMain()
                        + " - " + Utility.formatHighLows(w.getTemp().getMax(), w.getTemp().getMin());
                Log.v(LOG_TAG, "Data: " + weatherString);
            }

            mForecastAdapter.setData(mWeatherRealmsList);
            mForecastAdapter.notifyDataSetChanged();

            Log.v(LOG_TAG, "Success LoadFromDB");
        } else {
            Log.v(LOG_TAG, "Fail LoadFromDB");
        }

    }

}
