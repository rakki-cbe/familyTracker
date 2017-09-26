package radhakrishnan.familytracker.traking;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.lang.ref.WeakReference;

import radhakrishnan.familytracker.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class LocationListener extends Service {
    public static final String STARTFOREGROUND_ACTION = "radhakrishnan.familytracker.traking.action.startTrack";
    public static final String STOPFOREGROUND_ACTION = "radhakrishnan.familytracker.traking.action.stop";
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_TRACK = "radhakrishnan.familytracker.traking.action.track";
    private static final int FOREGROUND_SERVICE = 5454;
    final Messenger mMessenger = new Messenger(new BoundServiceHandler(this));

    public static Intent getStartForgroundServiceIntent(Context context) {
        Intent startIntent = new Intent(context, LocationListener.class);
        startIntent.setAction(LocationListener.STARTFOREGROUND_ACTION);
        return startIntent;
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        if (intent != null && intent.getAction().equals(STARTFOREGROUND_ACTION)) {
            Intent notificationIntent = new Intent(this, MapFragment.class);
            notificationIntent.setAction(ACTION_TRACK);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
            Intent Intent = new Intent(this, LocationListener.class);
            Intent.setAction(STOPFOREGROUND_ACTION);
            PendingIntent stopIntent = PendingIntent.getService(this, 0,
                    Intent, 0);
            Notification notification = new NotificationCompat.Builder(this)
                    .setContentTitle("Family traker")
                    .setTicker("Traking location")
                    .setContentText("Traking location")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    //.setLargeIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop",
                            stopIntent).build();

            startForeground(FOREGROUND_SERVICE,
                    notification);
            LocationHelper.getInstance().startTracking(this);
        } else if (intent.getAction().equals(
                STOPFOREGROUND_ACTION)) {
            LocationHelper.getInstance().stopTraking();
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocationHelper.getInstance().stopTraking();
    }

    private static class BoundServiceHandler extends Handler {
        private final WeakReference<LocationListener> mService;

        BoundServiceHandler(LocationListener service) {
            mService = new WeakReference<>(service);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 12:

                    Messenger activityMessenger = msg.replyTo;
                    LocationHelper.getInstance().setCallBack(activityMessenger);

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    }


}
