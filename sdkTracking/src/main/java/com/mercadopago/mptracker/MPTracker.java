package com.mercadopago.mptracker;

import android.content.Context;
import android.util.Log;

import com.mercadopago.mptracker.trackers.GATracker;
import com.mercadopago.mptracker.trackers.MPTrackerService;

import static android.text.TextUtils.isEmpty;


/**
 * Created by mromar on 3/31/16.
 */
public class MPTracker {

    private static MPTracker mMPTrackerInstance;

    private Integer mFlavor;
    private String mPublicKey;
    private String mSdkVersion;
    private String mSiteId;
    private Context mContext;

    private static final String SDK_PLATFORM = "Android";
    private static final String SDK_TYPE = "native";

    private static final String NO_SCREEN = "NO_SCREEN";
    private static final String DEFAULT_SITE = "MLA";

    private Boolean trackerInitialized = false;

    protected MPTracker(){}

    synchronized public static MPTracker getInstance(){
        if(mMPTrackerInstance == null) {
            mMPTrackerInstance = new MPTracker();
        }
        return mMPTrackerInstance;
    }

    public void trackPayment(String screenName, String action, Long paymentId, String paymentMethodId, String status, String statusDetail, String typeId, Integer installments, Integer issuerId){
        if (trackerInitialized && areParametersValid(action, paymentId, paymentMethodId, status, statusDetail, typeId, installments, issuerId)){

            if (!isCardPaymentType(typeId)){
                MPTrackerService.getInstance().trackPaymentId(paymentId, mFlavor, SDK_PLATFORM, SDK_TYPE, mPublicKey, mSdkVersion, mSiteId, mContext);
            }
            GATracker.getInstance().trackEvent(getPath(screenName), action, paymentMethodId, status, statusDetail, typeId, installments, issuerId);
        }
    }

    public void trackToken(String token){
        if (trackerInitialized && isTokenValid(token)){
            MPTrackerService.getInstance().trackToken(token, mFlavor, SDK_PLATFORM, SDK_TYPE, mPublicKey, mSdkVersion, mSiteId, mContext);
        }
    }

    public void trackEvent(String screenName, String action, Integer flavor, String publicKey, String siteId, String sdkVersion, Context context){
        initTracker(flavor, publicKey, siteId, sdkVersion, context);

        if (trackerInitialized && !isEmpty(action)) {
            GATracker.getInstance().trackEvent(getPath(screenName), action);
        }
    }

    public void trackEvent(String screenName, String action, Integer flavor, String publicKey, String sdkVersion, Context context){
        initTracker(flavor, publicKey, getSiteId(), sdkVersion, context);

        if (trackerInitialized && !isEmpty(action)) {
            GATracker.getInstance().trackEvent(getPath(screenName), action);
        }
    }

    public void trackEvent(String screenName, String action, String result, Integer flavor, String publicKey, String siteId, String sdkVersion, Context context){
        initTracker(flavor, publicKey, siteId, sdkVersion, context);

        if (trackerInitialized && !isEmpty(action) && !isEmpty(result)) {
            GATracker.getInstance().trackEvent(getPath(screenName), action, result);
        }
    }

    public void trackEvent(String screenName, String action, String result, Integer flavor){
        initTracker(flavor, mPublicKey, mSiteId, mSdkVersion, mContext);

        if (trackerInitialized && !isEmpty(action) && !isEmpty(result)) {
            GATracker.getInstance().trackEvent(getPath(screenName), action, result);
        }
    }

    public void trackScreen(String name, Integer flavor, String publicKey, String siteId, String sdkVersion, Context context){
        initTracker(flavor, publicKey, siteId, sdkVersion, context);

        if(trackerInitialized && areScreenParametersValid(name, context)) {
            GATracker.getInstance().trackScreen(getPath(name));
        }
    }

    public void trackScreen(String name, Integer flavor, String publicKey, String sdkVersion, Context context){
        initTracker(flavor, publicKey, getSiteId(), sdkVersion, context);

        if(trackerInitialized && areScreenParametersValid(name, context)) {
            GATracker.getInstance().trackScreen(getPath(name));
        }
    }

    private void initTracker(Integer flavor, String publicKey, String siteId, String sdkVersion, Context context){
        if (!isTrackerInitialized()) {
            if (areInitParametersValid(flavor, publicKey, siteId, sdkVersion, context)) {
                trackerInitialized = true;

                this.mFlavor = flavor;
                this.mPublicKey = publicKey;
                this.mSiteId = siteId;
                this.mSdkVersion = sdkVersion;
                this.mContext = context;

                GATracker.getInstance().trackNewSession(flavor, publicKey, sdkVersion, SDK_TYPE, siteId, context);
            }
        }
    }

    private boolean areInitParametersValid(Integer flavor, String publicKey, String siteId, String sdkVersion, Context context) {
        if (flavor == null || flavor <=0 || flavor > 3){
            Log.e("Invalid parameter","flavor can not be null or different to 1, 2 or 3");
            return false;
        }
        else if(isEmpty(publicKey)) {
            Log.e("Invalid parameter", "publicKey can not be null or empty");
            return false;
        }
        else if(isEmpty(sdkVersion)){
            Log.e("Invalid parameter", "sdkVersion can not be null or empty");
            return false;
        }
        else if(isEmpty(siteId)){
            Log.e("Invalid parameter", "siteId can not be null or empty");
            return false;
        }
        else if(context == null){
            Log.e("Invalid parameter","context can not be null");
            return false;
        }
        else{
            return true;
        }
    }

    private boolean areParametersValid( String action, Long paymentId, String paymentMethodId, String status, String statusDetail, String typeId, Integer installments, Integer issuerId) {
        if (!isEmpty(action)){
            return true;
        }
        else {
            return false;
        }
    }


    private boolean isTokenValid(String token){
        return !isEmpty(token);
    }

    private boolean areScreenParametersValid(String name, Context context) {
        if(isEmpty(name)){
            Log.e("Invalid parameter","name can not be null or empty");
            return false;
        }
        else if(context == null){
        Log.e("Invalid parameter","context can not be null");
        return false;
        }
        else{
            return true;
        }
    }

    private boolean isTrackerInitialized(){
        return this.mFlavor != null && this.mPublicKey != null && this.mSdkVersion != null && this.mSiteId != null;
    }

    private String getPath(String name){
        StringBuilder path = new StringBuilder();

        if (isEmpty(name)){
            path.append("F" + mFlavor + "/" + NO_SCREEN);
            return path.toString();
        }
        else{
            path.append("F" + mFlavor + "/" + name);
            return path.toString();
        }
    }

    private String getSiteId(){
        return mSiteId == null ? DEFAULT_SITE : mSiteId;
    }

    private Boolean isCardPaymentType(String paymentTypeId){
        return paymentTypeId.equals("credit_card") || paymentTypeId.equals("debit_card") || paymentTypeId.equals("prepaid_card");
    }
}




