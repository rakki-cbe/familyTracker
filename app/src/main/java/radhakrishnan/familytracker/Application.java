package radhakrishnan.familytracker;

import android.app.Activity;
import android.content.Context;

import radhakrishnan.familytracker.utill.PrefrenceHandler;

/**
 * Created by radhakrishnan on 29/8/17.
 */

public class Application extends android.app.Application {
    Context context;
    Activity simple;

    public Context getContext() {
        return this.getBaseContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        PrefrenceHandler.init(this);

        /***
         * Used for leakcanary checck
         */
      /*
      if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.

        }
        else {
            LeakCanary.install(this);
        }*/


    }

    public void setSimple(Activity simple) {
        this.simple = simple;
    }
}
