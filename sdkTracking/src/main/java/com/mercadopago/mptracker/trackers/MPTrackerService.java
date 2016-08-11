package com.mercadopago.mptracker.trackers;

import android.content.Context;
import android.util.Log;

import com.mercadopago.mptracker.model.PaymentIntent;
import com.mercadopago.mptracker.model.TrackIntent;
import com.mercadopago.mptracker.service.TrackingService;
import com.mercadopago.mptracker.util.HttpClientUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mromar on 5/18/16.
 */
public class MPTrackerService {

    private static MPTrackerService mMPTrackerServiceInstance;

    private static final String BASE_URL = "https://api.mercadopago.com/";

    protected MPTrackerService(){}

    synchronized public static MPTrackerService getInstance(){
        if(mMPTrackerServiceInstance == null) {
            mMPTrackerServiceInstance = new MPTrackerService();
        }
        return mMPTrackerServiceInstance;
    }

    public void trackToken(String cardToken, Integer flavor, String sdkPlatform, String sdkType, String publicKey, String sdkVersion, String site, Context context) {
        TrackIntent trackIntent = new TrackIntent();
        trackIntent.setPublicKey(publicKey);
        trackIntent.setCardToken(cardToken);
        trackIntent.setFlavor(flavor.toString());
        trackIntent.setPlatform(sdkPlatform);
        trackIntent.setType(sdkType);
        trackIntent.setSdkVersion(sdkVersion);
        trackIntent.setSite(site);

        Retrofit retrofitBuilder = new Retrofit.Builder()
                .client(HttpClientUtil.getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        TrackingService service = retrofitBuilder.create(TrackingService.class);

        Call<Void> call = service.trackToken(trackIntent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 400) {
                    Log.e("Failure","Error 400, parameter invalid");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Failure","Service failure");
            }
        });
    }

    public void trackPaymentId(Long paymentId, Integer flavor, String sdkPlatform, String sdkType, String publicKey, String sdkVersion, String site, Context context) {
        PaymentIntent trackIntent = new PaymentIntent();
        trackIntent.setPublicKey(publicKey);
        trackIntent.setPaymentId(paymentId.toString());
        trackIntent.setFlavor(flavor.toString());
        trackIntent.setPlatform(sdkPlatform);
        trackIntent.setType(sdkType);
        trackIntent.setSdkVersion(sdkVersion);
        trackIntent.setSite(site);

        Retrofit retrofitBuilder = new Retrofit.Builder()
                .client(HttpClientUtil.getClient(context))
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();

        TrackingService service = retrofitBuilder.create(TrackingService.class);

        Call<Void> call = service.trackPaymentId(trackIntent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == 400) {
                    Log.e("Failure","Error 400, parameter invalid");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("Failure","Service failure");
            }
        });
    }

}
