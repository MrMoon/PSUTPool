package com.psut.pool.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
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
import android.support.v7.widget.CardView;
import android.util.Log;
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
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.Activities.MainActivity;
import com.psut.pool.Models.Customer;
import com.psut.pool.Models.Driver;
import com.psut.pool.Models.RequestRide;
import com.psut.pool.Models.Ride;
import com.psut.pool.Models.Trip;
import com.psut.pool.Models.TripRoute;
import com.psut.pool.Models.User;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;
import com.psut.pool.Shared.Online;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.psut.pool.R.drawable.ic_directions_car_black_24dp;
import static com.psut.pool.Shared.Constants.DATABASE_IS_DRIVER;
import static com.psut.pool.Shared.Constants.DATABASE_NAME;
import static com.psut.pool.Shared.Constants.DATABASE_PHONE_NUMBER;
import static com.psut.pool.Shared.Constants.DATABASE_REQUEST;
import static com.psut.pool.Shared.Constants.DATABASE_TRIP;
import static com.psut.pool.Shared.Constants.DATABASE_USER_CURRENT_LOCATION;
import static com.psut.pool.Shared.Constants.DATABASE_USER_STATUS;
import static com.psut.pool.Shared.Constants.DESTINATION;
import static com.psut.pool.Shared.Constants.STATUS_DRIVING_MOVING;
import static com.psut.pool.Shared.Constants.STATUS_USER_OFFLINE;
import static com.psut.pool.Shared.Constants.STATUS_USER_ONILINE;
import static com.psut.pool.Shared.Constants.TRUE;
import static com.psut.pool.Shared.Constants.WENT_WRONG;
import static com.psut.pool.Shared.Constants.YOU_ARE_HERE;

public class PrimaryMapTabFragment extends Fragment implements OnMapReadyCallback {

    //Global Variables and Objects:
    private View view;
    private Button btnRoute;
    private Dialog dialog;
    private User user;
    private CardView cardView;
    private GoogleMap map;
    private Location currentLocation;
    private DatabaseReference databaseReference;
    private LatLng destination, currentLatLng;
    private ArrayList<LatLng> markerPoints;
    private ArrayList<String> names;
    private String[] permissions;
    private String distance, duration, apiKey = Constants.API_KEY, id, keyTrip = "0", phoneNumber, driverID;
    private Double latitude, longitude;
    private float tripCost;
    private int i = 0;
    private boolean isPermissionGranted;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_captain_tab, container, false);

        //Objects:
        dialog = new Dialog(Objects.requireNonNull(getActivity()).getApplicationContext());
        cardView = view.findViewById(R.id.cardViewSearchMainFrag);
        btnRoute = view.findViewById(R.id.btnRouteFragMain);
        markerPoints = new ArrayList<>();
        names = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_USERS);
        if (!Places.isInitialized()) {
            Places.initialize(Objects.requireNonNull(getActivity()).getApplicationContext(), apiKey);
        }
        isOnline(FirebaseAuth.getInstance().getCurrentUser());
        //Starting the map setup:
        getLocationPermission();
        return view;
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Attached", Toast.LENGTH_SHORT).show();
        if (map != null) {
            map.clear();
        }
        if (FirebaseAuth.getInstance().getCurrentUser() != null && databaseReference != null) {
            checkRequest(databaseReference);
        }
    }

    private void checkRequest(DatabaseReference reference) {
        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (Objects.requireNonNull(dataSnapshot.child(DATABASE_REQUEST).getValue()).toString().equalsIgnoreCase(TRUE)) {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Customer is waiting for you!!", Toast.LENGTH_SHORT).show();
                    startTrip(databaseReference);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startTrip(DatabaseReference reference) {

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

            setupMapSettings(map);
            currentLocationButton();
            setupAutoCompleteSearch();

            //Markers:
            map.setOnMapClickListener(latLng -> setupMarkers(map, latLng));
            checkRequest(databaseReference);
            btnRoute.setOnClickListener(v -> getDestinationInfo(map, destination, currentLatLng, apiKey));
        }
    }


    private void isOnline(FirebaseUser currentUser) {
        if (Online.isOnline(Objects.requireNonNull(getActivity()).getApplicationContext(), getActivity().getApplicationContext().getPackageName())) {
            databaseReference.child(currentUser.getUid()).child(DATABASE_USER_STATUS).setValue(STATUS_USER_ONILINE);
        } else {
            databaseReference.child(currentUser.getUid()).child(DATABASE_USER_STATUS).setValue(STATUS_USER_OFFLINE);
        }
    }


    private void setupAutoCompleteSearch() {
        cardView.setOnClickListener(v -> {
            // Set the fields to specify which types of place data to return.
            List<com.google.android.libraries.places.api.model.Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
                    com.google.android.libraries.places.api.model.Place.Field.NAME);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields).setCountry(Constants.COUNTRY_ID)
                    .build(Objects.requireNonNull(getActivity()).getApplicationContext());
            startActivityForResult(intent, Constants.LOCATION_AUTO_COMPLETE_REQUEST_CODE);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LOCATION_AUTO_COMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                System.out.println(place.getName());
                geoLocation(place.getName());
            } else {
                System.out.println(PlaceAutocomplete.getStatus(Objects.requireNonNull(getActivity()).getApplicationContext(), data));
            }
        }
    }


    //Search Results:
    private void geoLocation(String s) {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = (geocoder.getFromLocationName(s, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!(addresses.isEmpty())) {
            Address address = addresses.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()));
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
        map.setMyLocationEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setBuildingsEnabled(true);
        map.setIndoorEnabled(false);
        map.setTrafficEnabled(false);
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

                            writeUserData(databaseReference);

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

    private void writeUserData(DatabaseReference reference) {
        String isDriver = MainActivity.getIsDriver();
        if (isDriver.equalsIgnoreCase(Constants.TRUE)) {
            user = new Driver();
            user.setCurruntLatitude(latitude.toString());
            user.setCurruntLongitude(longitude.toString());
            reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(Constants.DATABASE_USER_CURRENT_LOCATION).updateChildren(user.toUserLocationMap());
        } else {
            user = new Customer();
            user.setCurruntLatitude(latitude.toString());
            user.setCurruntLongitude(longitude.toString());
            reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(Constants.DATABASE_USER_CURRENT_LOCATION).updateChildren(user.toUserLocationMap());
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

    private void setupMarkers(GoogleMap googleMap, LatLng latLng) {
        addPrimaryMarker(googleMap, markerPoints, latLng);

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
                    .icon(BitmapDescriptorFactory.defaultMarker(HUE_RED));

            MarkerOptions startLocation = new MarkerOptions().position(currentLatLng)
                    .title(YOU_ARE_HERE)
                    .icon(BitmapDescriptorFactory.defaultMarker(HUE_GREEN));
            map.addMarker(startLocation);
            map.addMarker(endLocation);

            map.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, Constants.DEFAULT_ZOOM - 0.5f));
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), Constants.JUST_A_MIN, Toast.LENGTH_SHORT).show();
            resetFragment();
        }
    }

    private void addPrimaryMarker(GoogleMap googleMap, @NotNull ArrayList<LatLng> list, LatLng lng) {
        //Checking if there is no marker on the map:
        if (list.size() > 1) {
            list.clear();
            googleMap.clear();
        }

        //Adding new item to the list:
        list.add(lng);

        addMarkertoMap(googleMap, lng, BitmapDescriptorFactory.defaultMarker(HUE_GREEN), DESTINATION);
        btnRoute.setVisibility(View.VISIBLE);
    }

    private void addMarkertoMap(@NotNull GoogleMap googleMap, LatLng latLng, BitmapDescriptor bitmapDescriptor, String title) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(title).icon(bitmapDescriptor);
        googleMap.addMarker(markerOptions);
    }

    private void getDestinationInfo(@NotNull GoogleMap googleMap, LatLng destination, LatLng origin, String serverKey) {
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

                                //Cost:
                                System.out.println("Cost Will be = " + String.format("%.2f", getCost(duration, distance)));
                                Snackbar snackbar = Snackbar.make(view, Constants.COST_WILL_BE + String.format("%.2f", getCost(duration, distance)) + Constants.JD, Snackbar.LENGTH_LONG);
                                tripCost = getCost(duration, distance);
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

                                //Nearest Drivers:
                                //HashMap<HashMap<String, Object>, LinkedHashMap<LatLng, Float>> nearestDrivers = new HashMap<>(getNearestDrivers(databaseReference));
                                HashMap<String, Object> nearestDrivers = new HashMap<>(getNearestDrivers(databaseReference));
                                //setupRequest(databaseReference , nearestDrivers);
                                sendRequest(databaseReference);
                                map.animateCamera(cu);

                                writeTripData(databaseReference);
                                break;
                            case RequestResult.NOT_FOUND:
                                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), Constants.NO_ROUTE_EXIST, Toast.LENGTH_SHORT).show();
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

    private void sendRequest(DatabaseReference reference) {
        reference.child("5XpFrLvFHL3W4TXxNJh5Je8rkCTy").child(DATABASE_REQUEST).setValue(TRUE);
    }

    private void setupRequest(@NotNull DatabaseReference reference, HashMap<String, Object> map) {
        //Request Object:
        RequestRide requestRide = new RequestRide(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), id, TRUE, user);
        //Getting Driver Number:

    }

    private void writeTripData(@NotNull DatabaseReference reference) {
        //Getting the requirement:
        float[] result = new float[1];
        Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, destination.latitude, destination.longitude, result);

        //Setting up the Objects:
        TripRoute tripRoute = new TripRoute(currentLatLng.toString(), destination.toString(), String.valueOf(result[0]), STATUS_DRIVING_MOVING);
        Ride ride = new Ride(Constants.description(currentLatLng.toString(), destination.toString()), String.valueOf(tripCost), tripRoute);
        Trip trip = new Trip(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), "", user, ride);
        //writing to the database:
        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid())).child(DATABASE_TRIP).updateChildren(trip.toFullTripMap());

    }

    private Map<String, Object> getNearestDrivers(@NonNull DatabaseReference reference) {
        //Data:
        HashMap<HashMap<String, Object>, LinkedHashMap<LatLng, Float>> driversData = new HashMap<>();
        HashMap<String, Object> driversInfo = new HashMap<>();  //ID , Phone Number
        LinkedHashMap<LatLng, Float> driversLocation = new LinkedHashMap<>();  //Location , Distance
        float[] distanceBetweenLocations = new float[1];
        double[] latlng = new double[2];

        //Getting Data from the Database:
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        //Objects:
                        int i = 0;
                        String driverID = userSnapshot.getKey();
                        names.add(driverID);
                        //Getting Ride Full Info:
                        if (!(Objects.requireNonNull(driverID).equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()))
                                && Objects.requireNonNull(userSnapshot.child(DATABASE_IS_DRIVER).getValue()).toString().equalsIgnoreCase(String.valueOf(Boolean.valueOf(true)))) {
                            for (DataSnapshot locationSnapshot : userSnapshot.child(DATABASE_USER_CURRENT_LOCATION).getChildren()) {
                                latlng[i++] = Double.valueOf(Objects.requireNonNull(locationSnapshot.getValue()).toString());
                            }
                            //Setting up the data:
                            LatLng lng = new LatLng(latlng[0], latlng[1]);
                            addMarkertoMap(map, lng, bitmapDescriptorFromVector(Objects.requireNonNull(getActivity()).getApplicationContext(), ic_directions_car_black_24dp), userSnapshot.child(DATABASE_NAME).getValue().toString());
                            Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, lng.latitude, lng.longitude, distanceBetweenLocations);

                            if (!driversLocation.containsKey(lng))
                                driversLocation.put(lng, distanceBetweenLocations[0]);
                            Log.d("Drivers Location", driversLocation.toString());

                            if (!driversInfo.containsKey(driverID))
                                driversInfo.put(driverID, Objects.requireNonNull(userSnapshot.child(DATABASE_PHONE_NUMBER).getValue()).toString());
                            Log.d("Drivers Info", driversInfo.toString());

                            driversData.put(driversInfo, driversLocation);
                            Log.d("Drivers Data", driversData.toString());
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), WENT_WRONG, Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
        });

        return driversInfo;
    }

    @NotNull
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, Objects.requireNonNull(vectorDrawable).getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private Float getCost(@NotNull String d0, @NotNull String d1) {
        float dur = Float.valueOf(d0.substring(0, d0.indexOf(" ")));
        float dis = Float.valueOf(d1.substring(0, d1.indexOf(" ")));

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