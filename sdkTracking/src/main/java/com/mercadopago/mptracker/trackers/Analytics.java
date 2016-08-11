package com.mercadopago.mptracker.trackers;

import android.app.Application;
import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by mromar on 6/6/16.
 */
public class Analytics extends Application {
    private Tracker mTracker;

    synchronized public Tracker getDefaultTracker(String gaKey, Context context) {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
            mTracker = analytics.newTracker(gaKey);
        }
        return mTracker;
    }
}

