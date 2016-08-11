package com.mercadopago.mptracker.trackers;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.HashMap;

import static android.text.TextUtils.isEmpty;


/**
 * Created by mromar on 4/27/16.
 */
public class GATracker {

    private static GATracker mGATrackerInstance;

    private String mGAKey;
    private HashMap<String, String> mGAKeys = new HashMap<>();
    private Tracker mTracker;
    private Boolean mGATrackerInitialize = false;

    //GA Keys
    private static final String MlaGAKey = "UA-46085787-6";
    private static final String MlbGAKey = "UA-46090222-6";
    private static final String MlmGAKey = "UA-46090517-10";
    private static final String MlcGAKey = "UA-46085697-7";
    private static final String McoGAKey = "UA-46087162-10";
    private static final String MlvGAKey = "UA-46090035-10";

    //Sites
    private static final String SITE_MLA = "MLA";
    private static final String SITE_MLB = "MLB";
    private static final String SITE_MLM = "MLM";
    private static final String SITE_MLC = "MLC";
    private static final String SITE_MCO = "MCO";
    private static final String SITE_MLV = "MLV";

    //Payment custom dimensions GA keys
    private static final Integer PAYMENT_STATUS_GA_INDEX = 8;
    private static final Integer PAYMENT_STATUS_DETAIL_GA_INDEX = 9;
    private static final Integer PAYMENT_TYPE_ID_GA_INDEX = 10;
    private static final Integer PAYMENT_INSTALLMENTS_GA_INDEX = 11;
    private static final Integer PAYMENT_ISSUER_ID_GA_INDEX = 12;
    private static final Integer PAYMENT_METHOD_ID_GA_INDEX = 13;

    //Session custom dimensions GA keys
    private static final Integer FLAVOR_GA_INDEX = 1;
    private static final Integer PUBLIC_KEY_GA_INDEX = 5;
    private static final Integer SDK_VERSION_GA_INDEX = 4;
    private static final Integer SDK_TYPE_GA_INDEX = 2;

    private static final Long NO_VALUE = 0L;

    protected GATracker(){}

    synchronized public static GATracker getInstance(){
        if(mGATrackerInstance == null) {
            mGATrackerInstance = new GATracker();
        }
        return mGATrackerInstance;
    }

    public void gaTrackerInit(String siteId, Context context){
        if (!mGATrackerInitialize){
            setGAKeys();
            mGATrackerInitialize = true;

            if(isSiteValid(siteId)) {
                setGAKey(siteId);
                trackerBuilder(context);
            }
            else{
                Log.e("Invalid parameter", "Site is not in GA Keys");
            }
        }
    }

    private void trackerBuilder(Context context){
        Analytics analytics = new Analytics();
        mTracker = analytics.getDefaultTracker(mGAKey, context);
    }

    private void setGAKeys() {
        mGAKeys.put(SITE_MLA, MlaGAKey);
        mGAKeys.put(SITE_MLB, MlbGAKey);
        mGAKeys.put(SITE_MLM, MlmGAKey);
        mGAKeys.put(SITE_MLC, MlcGAKey);
        mGAKeys.put(SITE_MCO, McoGAKey);
        mGAKeys.put(SITE_MLV, MlvGAKey);
    }

    public void trackEvent(String category, String action, String label){
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .setValue(NO_VALUE)
                .build());
    }

    public void trackEvent(String category, String action){
        mTracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setValue(NO_VALUE)
                .build());
    }

    public void trackEvent(String category, String action, String paymentMethodId, String status, String statusDetail, String typeId, Integer installments, Integer issuerId){
        if (areEventParametersValid( category, action, paymentMethodId, status, statusDetail, typeId, installments, issuerId)){
            HashMap<Integer, String> customDimensions = new HashMap<>();

            customDimensions.put(PAYMENT_STATUS_GA_INDEX, status);
            customDimensions.put(PAYMENT_STATUS_DETAIL_GA_INDEX, statusDetail);
            customDimensions.put(PAYMENT_TYPE_ID_GA_INDEX, typeId);
            customDimensions.put(PAYMENT_INSTALLMENTS_GA_INDEX, installments.toString());
            customDimensions.put(PAYMENT_ISSUER_ID_GA_INDEX, issuerId.toString());
            customDimensions.put(PAYMENT_METHOD_ID_GA_INDEX, paymentMethodId);

            HitBuilders.EventBuilder builder = new HitBuilders.EventBuilder();
            builder.setCategory(category).setAction(action).setValue(NO_VALUE);

            for (HashMap.Entry<Integer, String> entry : customDimensions.entrySet()) {
                builder.setCustomDimension(entry.getKey(), entry.getValue());
            }

            mTracker.send(builder.build());
        }
    }

    public void trackScreen(String name){
        mTracker.setScreenName(name);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    public void trackNewSession( Integer flavor, String publicKey, String sdkVersion, String sdkType, String siteId, Context context){
        HashMap<Integer, String> customDimensions = new HashMap<>();

        gaTrackerInit(siteId, context);

        HitBuilders.ScreenViewBuilder screenBuilder = new HitBuilders.ScreenViewBuilder();
        screenBuilder.setNewSession();

        customDimensions.put(FLAVOR_GA_INDEX, flavor.toString());
        customDimensions.put(PUBLIC_KEY_GA_INDEX, publicKey);
        customDimensions.put(SDK_VERSION_GA_INDEX, sdkVersion);
        customDimensions.put(SDK_TYPE_GA_INDEX, sdkType);

        for (HashMap.Entry<Integer, String> entry : customDimensions.entrySet()) {
            screenBuilder.setCustomDimension(entry.getKey(), entry.getValue());
        }

        mTracker.send(screenBuilder.build());
    }

    private boolean isSiteValid(String site){
        return mGAKeys.containsKey(site);
    }

    private void setGAKey(String site){
        mGAKey = mGAKeys.get(site);
    }

    private boolean areEventParametersValid(String category, String action, String paymentMethodId, String status, String statusDetail, String typeId, Integer installments, Integer issuerId){
        if (isEmpty(category)) {
            Log.e("Invalid parameter", "category can not be null or empty");
            return false;
        }
        else if(isEmpty(action)) {
            Log.e("Invalid parameter", "action can not be null or empty");
            return false;
        }
        else if(isEmpty(paymentMethodId)) {
            Log.e("Invalid parameter", "paymentMethodId can not be null or empty");
            return false;
        }
        else if(isEmpty(status)){
            Log.e("Invalid parameter","paymentStatus can not be null or empty");
            return false;
        }
        else if(isEmpty(statusDetail)){
            Log.e("Invalid parameter","paymentStatusDetail can not be null or empty");
            return false;
        }
        else if(isEmpty(typeId)){
            Log.e("Invalid parameter","paymentTypeId can not be null or empty");
            return false;
        }
        else if(installments == null || installments < 0){
            Log.e("Invalid parameter","paymentInstallments can not be null or less than zero");
            return false;
        }
        else if(issuerId == null || issuerId < 0) {
            Log.e("Invalid parameter", "paymentIssuerId can not be null or less than zero");
            return false;
        }
        else{
            return true;
        }
    }
}
