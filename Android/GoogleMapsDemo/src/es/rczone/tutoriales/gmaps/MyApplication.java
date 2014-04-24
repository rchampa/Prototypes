package es.rczone.tutoriales.gmaps;

import android.app.Application;
import es.rczone.tutoriales.gmaps.gps.LocationTracker;

/**
 * LocationTracker exists as long the application exists.
 * @author Ricardo
 *
 */
public class MyApplication extends Application {
    private LocationTracker mLocationManager;
    @Override
    public void onCreate() {
        super.onCreate();
        mLocationManager = LocationTracker.getInstance(this);
    }
}