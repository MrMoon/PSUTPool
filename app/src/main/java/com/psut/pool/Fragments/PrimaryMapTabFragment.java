package com.psut.pool.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PrimaryMapTabFragment extends Fragment implements OnMapReadyCallback {

    //Global Variables and Objects:
    private EditText txtSearch;
    private View view;
    private GoogleMap map;
    private Location currentLocation;
    private String[] permissions;
    private boolean isPermissionGranted;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_captain_tab, null);
        txtSearch = view.findViewById(R.id.txtSearchCaptainFrag);
        getLocationPermission();
        return view;
    }

    //Getting Permissions from the user:
    private void getLocationPermission() {
        permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext()
                    , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true;
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

    //Assigning map to the fragment:
    private void intitMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragmentMap);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    //Setting up the after it's ready:
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        if (isPermissionGranted) {
            getLocation();
            if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity())
                    , Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);

            init();

            map = googleMap;

            View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0, 180, 180, 0);
        }
    }

    //Setting up the search txt:
    @SuppressLint("ObsoleteSdkInt")
    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            txtSearch.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocatione();
                }
                return false;
            });
        }
    }

    //Search Results:
    private void geoLocatione() {
        String search = txtSearch.getText().toString();
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = (geocoder.getFromLocationName(search, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!(addresses.isEmpty())) {
            Address address = addresses.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), Constants.DEFAULT_ZOOM);
        }
    }

    //Getting user current location:
    private void getLocation() {
        try {
            if (isPermissionGranted) {
                FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(Objects.requireNonNull(getActivity()));
                if (!((LocationManager) (getActivity().getSystemService(Context.LOCATION_SERVICE)))
                        .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getActivity(), Constants.TRUN_LOCATION_ON, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, Constants.LOCATION_PERMISSION_REQUEST_CODE);
                    return;
                }
                Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();

                locationTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentLocation = task.getResult();
                        if (currentLocation != null) {
                            moveCamera(new LatLng(currentLocation.getLatitude()
                                    , currentLocation.getLongitude()), 15f);
                        }
                    } else {
                        Toast.makeText(getActivity(), Constants.WENT_WRONG, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Permission Results:
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        isPermissionGranted = false;
        switch (requestCode) {
            case 9002:
                if (!(Arrays.toString(grantResults).isEmpty())) {
                    if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                        isPermissionGranted = false;
                        return;
                    }
                    isPermissionGranted = true;
                    intitMap();
                }
        }
    }

    //Setting up the view on the map
    private void moveCamera(LatLng lng, Float zoom) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, zoom));
    }
}
