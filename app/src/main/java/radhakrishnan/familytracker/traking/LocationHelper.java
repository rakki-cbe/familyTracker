package radhakrishnan.familytracker.traking;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import radhakrishnan.familytracker.traking.model.Locationdao;

/**
 * Created by radhakrishnan on 30/8/17.
 */

class LocationHelper {
    private static final int REQUEST_CHECK_SETTINGS = 655;
    private static LocationHelper instance;
    private LocationRequest mLocationRequest = new LocationRequest();
    private Messenger activityMessenger;
    private double lat, longt;
    //private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private LocationHelper() {

    }

    public static LocationHelper getInstance() {
        if (instance == null)
            instance = new LocationHelper();
        return instance;
    }

    boolean startTracking(Context activity) {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setSmallestDisplacement(100);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                    if (lat != location.getLatitude() || longt != location.getLongitude()) {
                        Bundle b = new Bundle();
                        b.putSerializable("Location", new radhakrishnan.familytracker.traking.model.Location(location.getLongitude(), location.getLatitude()));
                        Message replyMsg = Message.obtain(null, 12);
                        replyMsg.setData(b);
                        try {
                            activityMessenger.send(replyMsg);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }

                        Locationdao.getInstance().setUserInfo(null, location.getLatitude(), location.getLongitude());
                        lat = location.getLatitude();
                        longt = location.getLongitude();
                    }
                    Log.d("My location", location.getLatitude() + ":" + location.getLongitude());
                }
            }

        };
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
        return true;
    }

    boolean stopTraking() {
        if (mFusedLocationClient != null && mLocationCallback != null)
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        return true;
    }

    void setCallBack(Messenger callBack) {
        this.activityMessenger = callBack;
    }
}
