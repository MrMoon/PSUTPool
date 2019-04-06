package com.psut.pool.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class UniMapTabFragment extends Fragment implements OnMapReadyCallback {

    //Global Variables and Objects:
    private GoogleMap map;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Global Variables and Objects:
        View view = inflater.inflate(R.layout.fragment_uni_map_tab, null);
        getLocationPermission();
        return view;
    }

    //Getting Permissions from the user:
    private void getLocationPermission() {
        String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext()
                    , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                intitMap();
            } else {
                ActivityCompat.requestPermissions(getActivity()
                        , permissions
                        , Constants.LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(getActivity()
                    , permissions
                    , Constants.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    //Permission Results:
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 9002:
                if (!(Arrays.toString(grantResults).isEmpty())) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    intitMap();
                }
        }
    }

    //Assigning map to the fragment:
    private void intitMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragmentUniMap);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        //Bounds:
        setUpBonds();

        //Markers:
        setUpMarkers(Constants.toLocationMap());

    }

    private void setUpBonds() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            Constants.restrictionList().forEach(builder::include);
        LatLngBounds latLngBounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.2);

        map.setLatLngBoundsForCameraTarget(latLngBounds);
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, width, height, padding));
        map.setMinZoomPreference(map.getCameraPosition().zoom);
        moveCamera(Constants.UNI_MAP_MAIN, Constants.UNI_MAP_DEFAULT_ZOOM);
    }

    private void setUpMarkers(Map<String, LatLng> lngMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            lngMap.forEach((s, latLng) -> {
                markerOptions.position(latLng).title(s);
                map.addMarker(markerOptions);
            });
    }

    private void moveCamera(LatLng latLng, float zoom) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }
}
