package com.psut.pool.Shared;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@SuppressLint("ValidFragment")
public class MapManipulation extends Fragment {

    //Global Variables and Objects:
    private GoogleMap googleMap;
    private Context context;
    private Location currentLocation;
    private String[] permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private Double latitude, longitude;


    public MapManipulation(GoogleMap googleMap, Context context) {
        this.googleMap = googleMap;
        this.context = context;
    }

    public static void moveCamera(GoogleMap googleMap, LatLng latLng, Float zoom) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }

    public void resetFragment(GoogleMap googleMap, ArrayList<LatLng> list) {
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) fragmentTransaction.setReorderingAllowed(false);
        list.clear();
        googleMap.clear();
        fragmentTransaction.detach(this).attach(this).commit();
    }

    public ArrayList<Double> getLocation(GoogleMap googleMap, boolean flag) {
        ArrayList<Double> locations = new ArrayList<>();
        try {
            if (flag) {
                FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(Objects.requireNonNull(getActivity()));

                checkPermission();

                Task<Location> locationTask = fusedLocationProviderClient.getLastLocation(); //Getting Last Location
                locationTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentLocation = task.getResult();
                        //Getting current Location:
                        if (currentLocation != null) {
                            locations.add(currentLocation.getLatitude());
                            locations.add(currentLocation.getLongitude());
                            moveCamera(googleMap, new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), Constants.DEFAULT_ZOOM);
                        } else {
                            Toast.makeText(getActivity(), "Null", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), Constants.WENT_WRONG, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return locations;
    }

    private void checkPermission() {
        //Permission isn't granted:
        if (!((LocationManager) (Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE)))
                .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(getActivity(), Constants.TRUN_LOCATION_ON, Toast.LENGTH_LONG).show();
            return;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, Constants.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public void geoLocation(GoogleMap googleMap, String s) {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = (geocoder.getFromLocationName(s, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!(addresses.isEmpty())) {
            Address address = addresses.get(0);
            moveCamera(googleMap, new LatLng(address.getLatitude(), address.getLongitude()), Constants.DEFAULT_ZOOM);
        }
    }

    public void setupMap(GoogleMap googleMap) {
        checkPermission();
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(false);
        googleMap.setTrafficEnabled(true);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
    }

    @SuppressLint("ObsoleteSdkInt")
    public void setupSearch(GoogleMap googleMap, EditText editText) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
            editText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocation(googleMap, editText.getText().toString());
                }
                return false;
            });
        }
    }

}
