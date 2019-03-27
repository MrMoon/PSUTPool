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
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PrimaryMapTabFragment extends Fragment implements OnMapReadyCallback {

    //Global Variables and Objects:
    private EditText txtSearch;
    private View view;
    private GoogleMap map;
    private Location currentLocation;
    private LatLng destination, currentLatLng;
    private ArrayList<LatLng> markerPoints;
    private String[] permissions;
    private Double latitude, longitude;
    private boolean isPermissionGranted;


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_captain_tab, null);

        //Objects:
        txtSearch = view.findViewById(R.id.txtSearchCaptainFrag);
        markerPoints = new ArrayList<>();

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
        setupMapSettings(googleMap);
        if (isPermissionGranted) {
            getLocation();
            if (ActivityCompat
                    .checkSelfPermission(Objects.requireNonNull(getActivity())
                            , Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            setupSearch();

            map = googleMap;

            currentLocationButton();

            //Markers:
            map.setOnMapClickListener(latLng -> {
                //Checking if there is no marker on the map:
                if (markerPoints.size() > 0) {
                    markerPoints.clear();
                    map.clear();
                }

                //Adding new item to the list:
                markerPoints.add(latLng);

                //Setting the position of the marker:
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                //Adding Marker:
                map.addMarker(markerOptions);

                //Getting latitude and longitude:
                try {
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //Setting up LatLng s :
                destination = markerPoints.get(0);
                currentLatLng = new LatLng(latitude, longitude);

                //Markers Set:
                MarkerOptions startLocation = new MarkerOptions().position(currentLatLng)
                        .title("You're Here")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                MarkerOptions endLocation = new MarkerOptions().position(destination)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

                map.addMarker(startLocation);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, Constants.DEFAULT_ZOOM - 0.5f));

                DirectionsResult directionsResult = getDirectionsResult(currentLatLng.toString(), destination.toString(), TravelMode.DRIVING);
                if (directionsResult != null) {
                    addPolyLine(directionsResult, map);
                    postionCamera(directionsResult.routes[0], map);
                    addMarkersToMap(directionsResult, map);
                } else {
                    Toast.makeText(getActivity(), " Null :) ", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void currentLocationButton() {
        View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();

        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 180, 180, 0);
    }

    @SuppressLint("MissingPermission")
    private void setupMapSettings(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setBuildingsEnabled(true);
        map.setTrafficEnabled(true);
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
    }

    //Setting up the search txt:
    @SuppressLint("ObsoleteSdkInt")
    private void setupSearch() {
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

    private GeoApiContext getGEOAPIContex() {
        GeoApiContext geoApiContext = new GeoApiContext();
        return geoApiContext
                .setQueryRateLimit(3)
                .setApiKey(getString(R.string.google_api_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
    }

    private DirectionsResult getDirectionsResult(String origin, String dest, TravelMode mode) {
        DateTime dateTime = new DateTime();
        try {
            return DirectionsApi.newRequest(getGEOAPIContex())
                    .mode(mode)
                    .origin(origin)
                    .destination(dest)
                    .departureTime(dateTime)
                    .await();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void addPolyLine(DirectionsResult result, GoogleMap googleMap) {
        List<LatLng> decodedPath = PolyUtil.decode(result.routes[0].overviewPolyline.getEncodedPath());
        googleMap.addPolyline(new PolylineOptions().addAll(decodedPath));
    }

    private void postionCamera(DirectionsRoute directionsRoute, GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(directionsRoute.legs[0].startLocation.lat, directionsRoute.legs[0].startLocation.lng), 12));
    }

    private void addMarkersToMap(DirectionsResult results, GoogleMap mMap) {
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].startLocation.lat, results.routes[0].legs[0].startLocation.lng)).title(results.routes[0].legs[0].startAddress));
        mMap.addMarker(new MarkerOptions().position(new LatLng(results.routes[0].legs[0].endLocation.lat, results.routes[0].legs[0].endLocation.lng)).title(results.routes[0].legs[0].startAddress).snippet(getEndLocationTitle(results)));
    }

    private String getEndLocationTitle(DirectionsResult results) {
        return "Time :" + results.routes[0].legs[0].duration.humanReadable + " Distance :" + results.routes[0].legs[0].distance.humanReadable;
    }
}