package id.kopilet.app.sunshine.model;

import io.realm.RealmObject;

/**
 * Created by rieftux on 14/04/16.
 */
public class CoordRealm extends RealmObject {

    private double lon;
    private double lat;

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
