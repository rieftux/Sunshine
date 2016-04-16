package id.kopilet.app.sunshine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.kopilet.app.sunshine.model.WeatherRealm;
import id.kopilet.app.sunshine.utils.Utility;

/**
 * Created by rieftux on 14/04/16.
 */
public class ForecastAdapter extends ArrayAdapter<WeatherRealm> {

    private LayoutInflater mInflater;
    private Context mContext;
    private List<WeatherRealm> mWeatherRealmList;
    private int mResLayout;

    public ForecastAdapter(Context context, int resource) {
        super(context, resource);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mResLayout = resource;
    }

    public ForecastAdapter(Context context, int resource, List<WeatherRealm> weatherRealms) {
        super(context, resource, weatherRealms);
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
        mResLayout = resource;
        mWeatherRealmList = weatherRealms;
    }


    public void setData(List<WeatherRealm> details) {
        this.mWeatherRealmList = details;
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ForecastHolder holder;

        if (convertView == null) {
            //inflate the layout
            convertView = mInflater.inflate(R.layout.list_item_forecast,
                    parent, false);

            /*convertView = mInflater.inflate(mResLayout,
                    parent, false);*/

            //set up viewHolder
            holder = new ForecastHolder();
            holder.txtHari = (TextView) convertView.findViewById(R.id.txtview_hari);
            holder.txtSuhu = (TextView) convertView.findViewById(R.id.txtview_suhu);
            //store the holder with the view
            convertView.setTag(holder);
        } else {
            // we've just avoided calling findViewById() on resource everytime
            // just use the viewHolder
            holder = (ForecastHolder) convertView.getTag();
        }

        WeatherRealm item = mWeatherRealmList.get(position);

        String hari = Utility.getReadableDateString(item.getDt());
        String suhu = Utility.formatHighLows(item.getTemp().getMax(), item.getTemp().getMin());

        holder.txtHari.setText(hari);
        holder.txtSuhu.setText(suhu);

        return convertView;
    }

    // ViewHolder
    private static class ForecastHolder {
        TextView txtHari;
        TextView txtSuhu;
    }
}
