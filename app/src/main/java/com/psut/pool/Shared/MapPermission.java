package com.psut.pool.Shared;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.psut.pool.R;

import java.util.Objects;

@SuppressLint("ValidFragment")
public class MapPermission extends Fragment implements MapsPermissions {

    //Global Variables and Objects:
    private Context context;
    private boolean flag = false;

    @SuppressLint("ValidFragment")
    public MapPermission(Context context) {
        this.context = context;
    }

    @Override
    public Boolean getLocationPermission(Activity activity, Context context) {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(activity).getApplicationContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext()
                    , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                flag = true;
                intitMap(context);
            } else {
                ActivityCompat.requestPermissions(activity
                        , permissions
                        , Constants.LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(activity
                    , permissions
                    , Constants.LOCATION_PERMISSION_REQUEST_CODE);
        }
        return flag;
    }

    @Override
    public void intitMap(Context context) {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragmentMap);
        Objects.requireNonNull(mapFragment).getMapAsync((OnMapReadyCallback) context);
    }
}
