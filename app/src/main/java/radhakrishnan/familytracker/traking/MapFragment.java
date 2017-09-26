package radhakrishnan.familytracker.traking;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import radhakrishnan.familytracker.R;
import radhakrishnan.familytracker.traking.model.Location;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private final Messenger mActivityMessenger = new Messenger(
            new ActivityHandler(this));
    MarkerOptions you = new MarkerOptions();
    private GoogleMap mMap;
    private Messenger mBoundServiceMessenger;
    private boolean mServiceConnected = false;
    private Location user;
    private HashMap<String, Location> otherUser = new HashMap<>();
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBoundServiceMessenger = null;
            mServiceConnected = false;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundServiceMessenger = new Messenger(service);
            mServiceConnected = true;
            try {
                Message msg = Message.obtain(null,
                        12, 0, 0);
                msg.replyTo = mActivityMessenger;
                mBoundServiceMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };

    public static MapFragment getInstance() {
        return new MapFragment();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tracking_fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        getActivity().bindService(LocationListener.getStartForgroundServiceIntent(getContext()), mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mServiceConnected) {
            getActivity().unbindService(mServiceConnection);
            mServiceConnected = false;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.zoomBy(12));

    }

    public void setOtherUser(Location location) {

        otherUser.put(location.getUserId(), location);
        UpdateMap();

    }

    public HashMap<String, Location> getOtherUser() {
        return otherUser;
    }

    public void setOtherUser(HashMap<String, Location> otherUser) {
        this.otherUser = otherUser;
    }

    private void UpdateMap() {
        if (mMap != null) {
            mMap.clear();
            for (String userid : otherUser.keySet()) {
                Location loc = otherUser.get(userid);
                MarkerOptions marker = new MarkerOptions();
                LatLng locationMap = new LatLng(loc.getLat(), loc.getLongt());
                marker.position(locationMap).title(loc.getUser().getName());
                mMap.addMarker(marker);

            }

            if (user != null) {
                you.position(new LatLng(user.getLat(), user.getLongt()));
                mMap.addMarker(you);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(user.getLat(), user.getLongt())));
            }
        }

    }

    private class ActivityHandler extends Handler {
        private final WeakReference<MapFragment> mActivity;

        ActivityHandler(MapFragment activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 12: {
                    if (msg.getData().getSerializable("Location") != null) {
                        user = (Location) msg.getData().getSerializable("Location");
                        UpdateMap();
                    }
                }
            }
        }

    }
}
