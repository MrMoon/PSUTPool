package com.psut.pool.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.RequestResult;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Info;
import com.akexorcist.googledirection.model.Leg;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.psut.pool.Activities.MainActivity;
import com.psut.pool.Models.Customer;
import com.psut.pool.Models.Driver;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class PrimaryMapTabFragment extends Fragment implements OnMapReadyCallback {

    //Global Variables and Objects:
    private View view;
    private Button btnRoute;
    private GoogleMap map;
    private Location currentLocation;
    private DatabaseReference databaseReference;
    private LatLng destination, currentLatLng;
    private ArrayList<LatLng> markerPoints;
    private String[] permissions;
    private String distance, duration, apiKey = "AIzaSyAviOlzcFhIac8VYwlXJ8g__oLjoVlfE2w";
    private Double latitude, longitude;
    private int i = 0;
    private boolean isPermissionGranted;
    private PlaceAutocompleteFragment placeAutoComplete;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //view = inflater.inflate(R.layout.fragment_captain_tab, null);
        view = inflater.inflate(R.layout.fragment_captain_tab, container, false);

        //Objects:
        btnRoute = view.findViewById(R.id.btnRouteFragMain);
        markerPoints = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.DATABASE_USERS)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child(Constants.DATABASE_USER_CURRENT_LOCATION);

        //Starting the map setup:
        getLocationPermission();
        return view;
    }

    private void getLocationPermission() {  //Getting Permissions from the user
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {   //Permission Results
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.LOCATION_AUTO_COMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(Objects.requireNonNull(getActivity()).getApplicationContext(), data);
                moveCamera(place.getLatLng());
            } else {
                System.out.println(PlaceAutocomplete.getStatus(Objects.requireNonNull(getActivity()).getApplicationContext(), data));
            }
        }
    }

    private void intitMap() {   //Assigning map to the fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.fragmentMap);
        Objects.requireNonNull(mapFragment).getMapAsync(this);
    }

    //Setting up the after it's ready
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Re-Checking the permissions
        if (isPermissionGranted) {
            if (ActivityCompat
                    .checkSelfPermission(Objects.requireNonNull(getActivity())
                            , Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            map = googleMap;
            getLocation();

            currentLocationButton();
            setupMapSettings(map);

            //Markers:
            map.setOnMapClickListener(this::setupMarkers);

            btnRoute.setOnClickListener(v -> getDestinationInfo(map, destination, currentLatLng, apiKey));
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
        //map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.setMyLocationEnabled(true);

        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setBuildingsEnabled(true);
        map.setIndoorEnabled(false);
        map.setTrafficEnabled(true);
        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
    }

    private void getLocation() {    //Getting user current location
        try {
            if (isPermissionGranted) {  //Re-Checking permission
                FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(Objects.requireNonNull(getActivity()));
                //Permission isn't granted:
                if (!((LocationManager) (getActivity().getSystemService(Context.LOCATION_SERVICE)))
                        .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(getActivity(), Constants.TRUN_LOCATION_ON, Toast.LENGTH_LONG).show();
                    return;
                }

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, Constants.LOCATION_PERMISSION_REQUEST_CODE);
                    return;
                }

                //Permission Granted:
                Task<Location> locationTask = fusedLocationProviderClient.getLastLocation(); //Getting Last Location

                locationTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentLocation = task.getResult();
                        //Getting current Location:
                        if (currentLocation != null) {
                            latitude = currentLocation.getLatitude();
                            longitude = currentLocation.getLongitude();
                            currentLatLng = new LatLng(latitude, longitude);

                            writeData();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
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
    }

    private void writeData() {
        String isDriver = MainActivity.getIsDriver();
        if (isDriver.equalsIgnoreCase("true")) {
            Driver driver = new Driver();
            driver.setCurruntLatitude(latitude.toString());
            driver.setCurruntLongitude(longitude.toString());
            databaseReference.updateChildren(driver.toUserLocationMap());
        } else {
            Customer customer = new Customer();
            customer.setCurruntLatitude(latitude.toString());
            customer.setCurruntLongitude(longitude.toString());
            databaseReference.updateChildren(customer.toUserLocationMap());
        }
    }

    private void resetFragment() {   //Fragment refresh
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) fragmentTransaction.setReorderingAllowed(false);
        markerPoints.clear();
        map.clear();
        fragmentTransaction.detach(this).attach(this).commit();
        getLocation();
    }

    private void moveCamera(LatLng lng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, Constants.DEFAULT_ZOOM));
    }

    private void setupMarkers(LatLng latLng) {
        //Checking if there is no marker on the map:
        if (markerPoints.size() > 1) {
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

        //Markers Set:
        try {
            //Getting latitude and longitude:
            latitude = currentLocation.getLatitude();
            longitude = currentLocation.getLongitude();

            //Setting up LatLng s :
            destination = markerPoints.get(0);
            currentLatLng = new LatLng(latitude, longitude);
            markerPoints.add(currentLatLng);

            MarkerOptions endLocation = new MarkerOptions().position(destination)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            MarkerOptions startLocation = new MarkerOptions().position(currentLatLng)
                    .title("You're Here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            map.addMarker(startLocation);
            map.addMarker(endLocation);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, Constants.DEFAULT_ZOOM - 0.5f));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Just a min", Toast.LENGTH_SHORT).show();
            resetFragment();
        }
    }

    private void getDestinationInfo(GoogleMap googleMap, LatLng destination, LatLng origin, String serverKey) {
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .transportMode(TransportMode.DRIVING)
                .execute(new DirectionCallback() {
                    @SuppressLint("DefaultLocale")
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onDirectionSuccess(Direction direction, String rawBody) {
                        String status = direction.getStatus();
                        switch (status) {
                            case RequestResult.OK:
                                //Getting data from json:
                                Route route = direction.getRouteList().get(0);
                                Leg leg = route.getLegList().get(0);
                                Info distanceInfo = leg.getDistance();
                                Info durationInfo = leg.getDuration();
                                distance = distanceInfo.getText();
                                duration = durationInfo.getText();

                                System.out.println(distance);
                                System.out.println(duration);

                                //Cost:
                                System.out.println("Cost = " + String.format("%.2f", getCost(duration, distance)));
                                Snackbar snackbar = Snackbar.make(view, "Cost = " + String.format("%.2f", getCost(duration, distance)) + " JD", Snackbar.LENGTH_LONG);
                                snackbar.show();

                                //Drawing route:
                                ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                PolylineOptions polylineOptions = DirectionConverter.createPolyline(Objects.requireNonNull(getActivity()),
                                        directionPositionList, 5, Color.RED);
                                googleMap.addPolyline(polylineOptions);

                                //Bounds:
                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                builder.include(origin);
                                builder.include(destination);
                                LatLngBounds bounds = builder.build();

                                int width = getResources().getDisplayMetrics().widthPixels;
                                int height = getResources().getDisplayMetrics().heightPixels;
                                int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

                                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
                                map.animateCamera(cu);
                                break;
                            case RequestResult.NOT_FOUND:
                                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "No routes exist", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onDirectionFailure(Throwable t) {
                    }
                });
    }

    private Float getCost(String d0, String d1) {
        //distance(d1) , duration (d0):
        d0 = d0.substring(0, d0.indexOf(" "));
        d1 = d1.substring(0, d1.indexOf(" "));

        float dur = Float.valueOf(d0);
        float dis = Float.valueOf(d1);
        System.out.println(dis);
        System.out.println(dur);
        float cost;

        if (dis > 0 && dis <= 5) {
            cost = 0.5f;
        } else if (dis >= 6 && dis <= 11) {
            cost = 1f;
        } else {
            cost = 2f;
        }

        return cost;
    }
}