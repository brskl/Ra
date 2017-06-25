package com.benjaminsklar.ra;

import android.app.Application;
import android.util.Log;

/**
 * Created by Ben on 5/29/2017.
 */

public class RaApplication extends Application {
    private static RaApplication mRaApplication;

    public static RaApplication getRaApplication() { return mRaApplication; }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(RaApplication.class.toString(), "onCreate");

        mRaApplication = this;
    }
}