package id.kopilet.app.sunshine;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Context mContext;
    private ArrayAdapter<String> weekForecastAdapter;

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

        List<String> weekForecastString = new ArrayList<>();
        weekForecastString.add("Raining - 25 - 2-4-16");
        weekForecastString.add("Raining - 20 - 3-4-16");
        weekForecastString.add("Cold - 21 - 4-4-16");
        weekForecastString.add("Raining - 23 - 5-4-16");
        weekForecastString.add("Hot - 27 - 6-4-16");

        weekForecastAdapter = new ArrayAdapter<String>(mContext,
                R.layout.list_item_forecast,
                R.id.list_item_forecast_textview,
                weekForecastString);

        listViewForecast.setAdapter(weekForecastAdapter);

        return rootView;
    }
}
