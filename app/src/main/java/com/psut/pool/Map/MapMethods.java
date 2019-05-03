package com.psut.pool.Map;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;

import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.database.DatabaseReference;

public interface MapMethods {
    void getLocation(Activity activity, GoogleMap googleMap, DatabaseReference databaseReference);

    void setupMapSettings(GoogleMap googleMap);

    void currentLocationButton(View view);

    void setupAutoCompleteSearch(CardView cardView, Activity activity);

    void setupMap(GoogleMap googleMap, Activity activity, View view, CardView cardView, DatabaseReference databaseReference);
}
