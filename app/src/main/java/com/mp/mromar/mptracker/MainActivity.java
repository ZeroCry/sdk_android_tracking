package com.mp.mromar.mptracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mercadopago.mptracker.MPTracker;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Track
        MPTracker.getInstance().trackScreen("BANK_DEALS", 3, "1234", "MLA", "1.0", this);
        MPTracker.getInstance().trackPayment("TEST_SCREEN", "TEST_ACTION", 1234567L, "visa", "approved", "acredited", "credit_card", 6, 2);

        MPTracker.getInstance().trackScreen("BANK_DEALS", 3, "publicKey", "MLA", "1.0", this);

        MPTracker.getInstance().trackEvent("BANK_DEALS", "BANK_DEALS", 3, "1234", "MLA", "1.0", this);
        MPTracker.getInstance().trackEvent("GUESSING_CARD", "GUESSING_CARD", "SUCCESS", 3, "publicKey", "MLA", "1.0", this);

        MPTracker.getInstance().trackScreen("GUESSING_CARD", 3, "publicKey", "MLA", "1.0", this);
    }
}















