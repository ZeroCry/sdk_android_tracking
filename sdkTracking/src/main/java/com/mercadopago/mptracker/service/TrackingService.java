package com.mercadopago.mptracker.service;

import com.mercadopago.mptracker.model.PaymentIntent;
import com.mercadopago.mptracker.model.TrackIntent;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mromar on 5/18/16.
 */
public interface TrackingService {

    @POST("/v1/checkout/tracking")
    Call<Void> trackToken(@Body TrackIntent body);

    @POST("/v1/checkout/tracking/off")
    Call<Void> trackPaymentId(@Body PaymentIntent body);
}
