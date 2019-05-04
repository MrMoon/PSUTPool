package com.psut.pool.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import android.net.Uri;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.Activities.MainActivity;
import com.psut.pool.Models.Customer;
import com.psut.pool.Models.Driver;
import com.psut.pool.Models.Rating;
import com.psut.pool.Models.Ride;
import com.psut.pool.Models.Trip;
import com.psut.pool.Models.TripRoute;
import com.psut.pool.Models.User;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.psut.pool.Shared.Constants.API_KEY;
import static com.psut.pool.Shared.Constants.CONFIRMED;
import static com.psut.pool.Shared.Constants.CONFIRM_RIDE;
import static com.psut.pool.Shared.Constants.CUSTOMER_CURRENT_LATITUDE;
import static com.psut.pool.Shared.Constants.CUSTOMER_CURRENT_LOCATION_DROP_NAME;
import static com.psut.pool.Shared.Constants.CUSTOMER_CURRENT_LOCATION_PICKUP_NAME;
import static com.psut.pool.Shared.Constants.CUSTOMER_CURRENT_LONGITIUDE;
import static com.psut.pool.Shared.Constants.CUSTOMER_ID;
import static com.psut.pool.Shared.Constants.CUSTOMER_NAME;
import static com.psut.pool.Shared.Constants.DATABASE_COST;
import static com.psut.pool.Shared.Constants.DATABASE_IS_DRIVER;
import static com.psut.pool.Shared.Constants.DATABASE_NAME;
import static com.psut.pool.Shared.Constants.DATABASE_PHONE_NUMBER;
import static com.psut.pool.Shared.Constants.DATABASE_REQUEST;
import static com.psut.pool.Shared.Constants.DATABASE_REQUESTS;
import static com.psut.pool.Shared.Constants.DATABASE_RESPONSE;
import static com.psut.pool.Shared.Constants.DATABASE_TRIP;
import static com.psut.pool.Shared.Constants.DATABASE_TRIP_STATUS;
import static com.psut.pool.Shared.Constants.DATABASE_USERS;
import static com.psut.pool.Shared.Constants.DATABASE_USER_CURRENT_LOCATION;
import static com.psut.pool.Shared.Constants.DATABASE_VALUE;
import static com.psut.pool.Shared.Constants.DELETED_REQUEST;
import static com.psut.pool.Shared.Constants.DELETE_REQUEST;
import static com.psut.pool.Shared.Constants.DENYED;
import static com.psut.pool.Shared.Constants.DESTINATION;
import static com.psut.pool.Shared.Constants.DESTINATION_LATITUDE;
import static com.psut.pool.Shared.Constants.DESTINATION_LONGITIUDE;
import static com.psut.pool.Shared.Constants.DRIVER_IS_ON_HIS_WAY;
import static com.psut.pool.Shared.Constants.DRIVER_PHONE_NUMBER;
import static com.psut.pool.Shared.Constants.FALSE;
import static com.psut.pool.Shared.Constants.REQUEST_CONFIRMED;
import static com.psut.pool.Shared.Constants.REQUEST_DENYED;
import static com.psut.pool.Shared.Constants.REQUEST_SENT;
import static com.psut.pool.Shared.Constants.RESPONSE_DENYED;
import static com.psut.pool.Shared.Constants.STATUS_DRIVING_MOVING;
import static com.psut.pool.Shared.Constants.STATUS_DRIVING_STARTING;
import static com.psut.pool.Shared.Constants.STATUS_DRIVING_WAITING;
import static com.psut.pool.Shared.Constants.TRUE;
import static com.psut.pool.Shared.Constants.WENT_WRONG;
import static com.psut.pool.Shared.Constants.YOU_ARE_HERE;

public class PrimaryMapTabFragment extends Fragment implements OnMapReadyCallback {

    //Global Variables and Objects:
    private View view;
    private Button btnRoute, btnConfirmRide, btnDenyRide;
    private TextView txtDropOffLocationName, txtPickUpLocationName, txtName, txtCost;
    private CardView cardViewSearch, cardViewCustomerTrip, cardViewConfirmTrip;
    private RelativeLayout relativeConfirm;
    private GoogleMap map;
    private Context context;
    private Location currentLocation;
    private DatabaseReference databaseReference;
    private LatLng destination, currentLatLng;
    private ArrayList<LatLng> markerPoints;
    private HashMap<Marker, String> markers = new HashMap<>();  //Marker obj , ID
    private HashMap<Float, String> driversInfo = new HashMap<>();  //Distance Between Locations , ID
    private HashMap<String, String> driversIDAndNames = new HashMap<>();    //ID , Name
    private HashMap<String, String> driversNameAndID = new HashMap<>();    //Name , ID
    private HashMap<String, Object> customerInfo = new HashMap<>();
    private String distance, duration, driverName, driverID, currentLocationName, destinationLocationName, uid, userName, phoneNumber;
    private User user;
    private float cost, min;
    private int rideNumber;
    private String[] permissions;
    private boolean isPermissionGranted, isDriver = Boolean.parseBoolean(MainActivity.getIsDriver());


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_captain_tab, container, false);
        layoutComponents();
        initObj();
        startMapSetUp();
        return view;
    }

    private void initObj() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        markerPoints = new ArrayList<>();
        context = Objects.requireNonNull(getActivity()).getApplicationContext();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_USERS);
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        isDriver = Boolean.parseBoolean(MainActivity.getIsDriver());
        if (!Places.isInitialized())
            Places.initialize(Objects.requireNonNull(getActivity()).getApplicationContext(), API_KEY);
    }

    private void startMapSetUp() {
        if (getLocationPermission()) {
            intitMap();
        } else {
            getLocationPermission();
            Toast.makeText(context, "Please Give the app the Permissions", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean getLocationPermission() {  //Getting Permissions from the user
        permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()).getApplicationContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext()
                    , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true;
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
        return isPermissionGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {   //Permission Results
        isPermissionGranted = false;
        if (requestCode == 9002) {
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

    @Override
    public void onStart() {
        super.onStart();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (isDriver) {
            checkRequest(databaseReference);
        } else {
            checkConfirm(databaseReference);
        }
    }

    private void checkConfirm(DatabaseReference reference) {
        reference.child(uid).child(DATABASE_RESPONSE).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child(DATABASE_VALUE).exists()) {
                        if (Objects.requireNonNull(dataSnapshot.child(DATABASE_VALUE).getValue()).toString().equalsIgnoreCase(TRUE)) {
                            Toast.makeText(context, REQUEST_CONFIRMED + " " + DRIVER_IS_ON_HIS_WAY, Toast.LENGTH_SHORT).show();
                            btnDenyRide.setVisibility(View.GONE);
                            btnConfirmRide.setVisibility(View.GONE);
                            relativeConfirm.setVisibility(View.VISIBLE);
                            cardViewConfirmTrip.setVisibility(View.VISIBLE);
                            txtName.setVisibility(View.VISIBLE);
                            txtName.setText("Driver Number: " + Objects.requireNonNull(dataSnapshot.child(DRIVER_PHONE_NUMBER).getValue()).toString());
                            reference.getRoot().child(DATABASE_USERS).child(uid).child(DATABASE_RESPONSE).setValue(CONFIRMED);
                        } else {
                            Toast.makeText(context, REQUEST_DENYED, Toast.LENGTH_SHORT).show();
                            setupDenyLayout(RESPONSE_DENYED);
                        }
                    } else {
                        if (Objects.requireNonNull(dataSnapshot.getValue()).toString().equalsIgnoreCase(DENYED)) {
                            Toast.makeText(context, REQUEST_DENYED, Toast.LENGTH_SHORT).show();
                            setupDenyLayout(RESPONSE_DENYED);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Setting up the after it's ready
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("Map", "Map is Ready");

        //Re-Checking the permissions
        if (isPermissionGranted) {
            if (ActivityCompat
                    .checkSelfPermission(Objects.requireNonNull(getActivity())
                            , Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                return;
            }

            //Map setup
            map = googleMap;
            setupMap(map);

            //Markers
            map.setOnMapClickListener(latLng -> setupMarkers(map, latLng, databaseReference));

            if (isDriver) {
                checkRequest(databaseReference);
            } else {
                checkConfirm(databaseReference);
            }

            getDrivers(databaseReference);

            //Trip Setup
            btnRoute.setOnClickListener(v -> startFullTripSetup(map, currentLatLng, destination, databaseReference));

        } else {
            getLocationPermission();
        }
    }

    private void checkRequest(DatabaseReference reference) {
        reference.child(uid).child(DATABASE_REQUESTS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        if (snapshot.exists()) {
                            Toast.makeText(context, "You have a request ", Toast.LENGTH_SHORT).show();
                            btnConfirmRide.setVisibility(View.VISIBLE);
                            btnDenyRide.setVisibility(View.VISIBLE);
                            cardViewCustomerTrip.setVisibility(View.VISIBLE);
                            cardViewConfirmTrip.setVisibility(View.VISIBLE);
                            relativeConfirm.setVisibility(View.VISIBLE);
                            btnConfirmRide.setText(CONFIRM_RIDE);
                            getTripInfo(reference, snapshot.getKey());
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getTripInfo(DatabaseReference reference, String key) {
        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(DATABASE_REQUESTS).child(key).addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    //Data
                    String customerID = Objects.requireNonNull(dataSnapshot.child(CUSTOMER_ID).getValue()).toString();
                    String customerName = Objects.requireNonNull(dataSnapshot.child(CUSTOMER_NAME).getValue()).toString();
                    String customerPickUpLocationName = Objects.requireNonNull(dataSnapshot.child(CUSTOMER_CURRENT_LOCATION_PICKUP_NAME).getValue()).toString();
                    String customerLocationLat = Objects.requireNonNull(dataSnapshot.child(CUSTOMER_CURRENT_LATITUDE).getValue()).toString();
                    String customerLocationLon = Objects.requireNonNull(dataSnapshot.child(CUSTOMER_CURRENT_LONGITIUDE).getValue()).toString();
                    String customerDropOffLocationName = Objects.requireNonNull(dataSnapshot.child(CUSTOMER_CURRENT_LOCATION_DROP_NAME).getValue()).toString();
                    String customerDestLat = Objects.requireNonNull(dataSnapshot.child(DESTINATION_LATITUDE).getValue()).toString();
                    String customerDestLon = Objects.requireNonNull(dataSnapshot.child(DESTINATION_LONGITIUDE).getValue()).toString();
                    String cost = Objects.requireNonNull(dataSnapshot.child(DATABASE_COST).getValue()).toString();
                    String phoneNumber = Objects.requireNonNull(Objects.requireNonNull(dataSnapshot.child(DATABASE_PHONE_NUMBER).getValue()).toString());
                    LatLng customerLatLng = new LatLng(Double.valueOf(customerLocationLat), Double.valueOf(customerLocationLon));
                    LatLng destLatLng = new LatLng(Double.valueOf(customerDestLat), Double.valueOf(customerDestLon));

                    //Layout
                    setupRequestLayout(View.VISIBLE);
                    txtPickUpLocationName.setText(customerPickUpLocationName);
                    txtDropOffLocationName.setText(customerDropOffLocationName);
                    txtName.setText(customerName);
                    txtCost.setText(cost + " JD");

                    btnConfirmRide.setOnClickListener(v -> {
                        sendResponse(reference, customerID, TRUE, phoneNumber);
                        startTrip(reference, destLatLng);
                        cardViewCustomerTrip.setVisibility(View.GONE);
                        map.clear();
                    });

                    btnDenyRide.setOnClickListener(v -> {
                        sendResponse(reference, customerID, DENYED);
                        setupTripLayout(map, reference);
                        reference.child(uid).child(DATABASE_REQUESTS).setValue(FALSE);
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupRequestLayout(int value) {
        cardViewCustomerTrip.setVisibility(value);
        relativeConfirm.setVisibility(value);
        cardViewConfirmTrip.setVisibility(value);
        btnDenyRide.setVisibility(value);
    }

    private void sendResponse(DatabaseReference reference, String customerID, String value, String phoneNumber) {
        reference.child(customerID).child(DATABASE_RESPONSE).updateChildren(toResponesMap(value, phoneNumber));
        reference.getRoot().child(DATABASE_USERS).child(uid).child(DATABASE_REQUESTS).setValue(CONFIRMED);
    }

    private void sendResponse(DatabaseReference reference, String customerID, String value) {
        reference.child(customerID).child(DATABASE_RESPONSE).setValue(DENYED);
    }

    private Map<String, Object> toResponesMap(String value, String phoneNumber) {
        HashMap<String, Object> respones = new HashMap<>();
        respones.put(DATABASE_VALUE, value);
        respones.put(DRIVER_PHONE_NUMBER, phoneNumber);
        return respones;
    }

    private void startTrip(DatabaseReference reference, LatLng dest) {
        cardViewConfirmTrip.setVisibility(View.GONE);
        cardViewConfirmTrip.setVisibility(View.GONE);
        relativeConfirm.setVisibility(View.GONE);
        reference.child(uid).child(DATABASE_TRIP).child(String.valueOf(rideNumber)).child(DATABASE_TRIP_STATUS).setValue(STATUS_DRIVING_MOVING);
        launchGoogleMaps(dest);
    }

    private void launchGoogleMaps(LatLng dest) {
        String URL = "https://www.google.com/maps/dir/?api=1&travelmode=driving&dir_action=navigate&destination=" + dest.latitude + "," + dest.longitude;
        Uri location = Uri.parse(URL);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        startActivity(mapIntent);
    }


    private void setupMap(GoogleMap googleMap) {
        getLocation();
        cardViewSearch.setVisibility(View.VISIBLE);
        setupMapSettings(googleMap);
        setupAutoCompleteSearch();
    }

    private void setupAutoCompleteSearch() {
        cardViewSearch.setOnClickListener(v -> {
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

    //Search Results
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
        currentLocationButton();
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
                            currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());

                            writeUserData(databaseReference, currentLatLng);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void startFullTripSetup(GoogleMap googleMap, LatLng origin, LatLng destinationLatLng, DatabaseReference reference) {
        getDestinationInfo(googleMap, reference, destinationLatLng, origin);
        setDriverName(reference);
        setupConfirm(reference);
        setupTripLayout(googleMap, reference);
        btnConfirmRide.setOnClickListener(v -> sendRequestToDriver(reference));
    }

    @SuppressLint("SetTextI18n")
    private void setupConfirm(DatabaseReference reference) {
        if (TextUtils.isEmpty(txtName.getText().toString())) {
            setDriverName(reference);
        }
    }

    private void sendRequestToDriver(DatabaseReference reference) {
        responesSet(reference);
        //Data
        String driverIDToSend = driversNameAndID.get(txtName.getText().toString());
        driverID = driversNameAndID.get(txtName.getText().toString());
        driverName = txtName.getText().toString();

        if (TextUtils.isEmpty(driverIDToSend)) {
            driverIDToSend = getNearestDriverID();
            txtName.setText(getNearestDriverName());
        }

        writeTripData(reference, txtName.getText().toString(), uid);
        writeRequestData(reference, Objects.requireNonNull(driverIDToSend));

        Snackbar snackbar = Snackbar.make(view, REQUEST_SENT + txtName.getText().toString(), Snackbar.LENGTH_LONG);
        snackbar.show();

        checkConfirm(reference);

        btnConfirmRide.setText(DELETE_REQUEST);
        btnConfirmRide.setOnClickListener(v -> denyRequest(reference, txtName.getText().toString()));
    }

    private void responesSet(DatabaseReference reference) {
        reference.getRoot().child(DATABASE_USERS).child(uid).child(DATABASE_RESPONSE).setValue(STATUS_DRIVING_WAITING);
    }

    private void writeRequestData(DatabaseReference reference, @NonNull String id) {
        getUserName(reference, uid, id);
    }

    private HashMap<String, Object> getCustomerInfo() {
        HashMap<String, Object> customerInfo = new HashMap<>();
        customerInfo.put(CUSTOMER_ID, uid);
        customerInfo.put(CUSTOMER_NAME, userName);
        customerInfo.put(CUSTOMER_CURRENT_LOCATION_PICKUP_NAME, currentLocationName);
        customerInfo.put(CUSTOMER_CURRENT_LOCATION_DROP_NAME, destinationLocationName);
        customerInfo.put(CUSTOMER_CURRENT_LATITUDE, currentLatLng.latitude);
        customerInfo.put(CUSTOMER_CURRENT_LONGITIUDE, currentLatLng.longitude);
        customerInfo.put(DESTINATION_LATITUDE, destination.latitude);
        customerInfo.put(DESTINATION_LONGITIUDE, destination.longitude);
        customerInfo.put(DATABASE_COST, cost);
        customerInfo.put(DATABASE_REQUEST, TRUE);
        customerInfo.put(DATABASE_PHONE_NUMBER, phoneNumber);
        return customerInfo;
    }

    private void denyRequest(DatabaseReference reference, String name) {
        Log.d("Request to id = " + Objects.requireNonNull(driversNameAndID.get(name)), DELETE_REQUEST);
        deleteDriverTripRequest(reference, name);
        deleteUserTripRequest(reference, uid);
        --rideNumber;
    }

    private void deleteUserTripRequest(DatabaseReference reference, String uid) {
        reference.child(Objects.requireNonNull(uid)).child(DATABASE_TRIP).removeValue().addOnCompleteListener(task -> {
            if (task.isSuccessful()) setupDenyLayout(DELETED_REQUEST);
        });
    }

    private void deleteDriverTripRequest(DatabaseReference reference, String name) {
        reference.child(Objects.requireNonNull(driversNameAndID.get(name))).child(DATABASE_REQUESTS).setValue(FALSE).addOnCompleteListener(task -> {
            if (task.isSuccessful()) setupDenyLayout(DELETED_REQUEST);
        });
    }

    private String getNearestDriverName() {
        return driversIDAndNames.get(driversInfo.get(Collections.min(driversInfo.keySet())));
    }

    private String getNearestDriverID() {
        return driversInfo.get(Collections.min(driversInfo.keySet()));
    }

    private String getDriverIDFromMarker(Marker marker) {
        try {
            if (marker.getPosition() != currentLatLng && marker.getPosition() != markerPoints.get(0) && marker.getPosition() != markerPoints.get(1) && marker.getPosition() != destination) {
                if (markers.containsKey(marker)) {
                    driverID = markers.get(marker);
                    driverName = marker.getTitle();
                    Log.d("getDriverID", driverID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, WENT_WRONG, Toast.LENGTH_SHORT).show();
        }

        return driverName;
    }

    private void writeUserData(DatabaseReference reference, LatLng latLng) {
        if (isDriver) {
            user = new Driver();
            user.setCurruntLatitude(String.valueOf(latLng.latitude));
            user.setCurruntLongitude(String.valueOf(latLng.longitude));
            reference.child(uid).child(Constants.DATABASE_USER_CURRENT_LOCATION).updateChildren(user.toUserLocationMap());
        } else {
            user = new Customer();
            user.setCurruntLatitude(String.valueOf(latLng.latitude));
            user.setCurruntLongitude(String.valueOf(latLng.longitude));
            reference.child(uid).child(Constants.DATABASE_USER_CURRENT_LOCATION).updateChildren(user.toUserLocationMap());
        }
    }

    private void resetFragment() {   //Fragment refresh
        FragmentTransaction fragmentTransaction = Objects.requireNonNull(getFragmentManager()).beginTransaction();
        if (Build.VERSION.SDK_INT >= 26) fragmentTransaction.setReorderingAllowed(false);
        markerPoints.clear();
        map.clear();
        btnConfirmRide.setText(CONFIRM_RIDE);
        fragmentTransaction.detach(this).attach(this).commit();
        getLocation();
    }

    private void moveCamera(LatLng lng) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, Constants.DEFAULT_ZOOM));
    }

    private void setupMarkers(GoogleMap googleMap, LatLng latLng, DatabaseReference reference) {
        addPrimaryMarker(googleMap, markerPoints, latLng, reference);
        layoutComponents();
        setLayout();
        //Markers Set:
        try {
            getLocation();
            //Getting latitude and longitude:
            double latitude = currentLocation.getLatitude();
            double longitude = currentLocation.getLongitude();

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

    private void setLayout() {
        relativeConfirm.setVisibility(View.GONE);
        cardViewConfirmTrip.setVisibility(View.GONE);
        btnConfirmRide.setVisibility(View.VISIBLE);
        txtName.setVisibility(View.VISIBLE);
        txtCost.setVisibility(View.VISIBLE);
    }

    private void addPrimaryMarker(GoogleMap googleMap, @NotNull ArrayList<LatLng> list, LatLng lng, DatabaseReference reference) {
        //Checking if there is no marker on the map
        if (list.size() > 1) {
            list.clear();
            googleMap.clear();
            getDrivers(reference);
            relativeConfirm.setVisibility(View.GONE);
            btnRoute.setVisibility(View.GONE);
            btnConfirmRide.setText(CONFIRM_RIDE);
        }

        list.add(lng);

        addMarkerToMap(googleMap, lng, BitmapDescriptorFactory.defaultMarker(HUE_GREEN), DESTINATION);
        btnRoute.setVisibility(View.VISIBLE);
    }

    private Marker addMarkerToMap(@NotNull GoogleMap googleMap, LatLng latLng, BitmapDescriptor bitmapDescriptor, String title) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(title).icon(bitmapDescriptor);
        Log.d("Marker NearestDriver", "Marker Added : " + title);
        return googleMap.addMarker(markerOptions);
    }

    private void getDestinationInfo(@NotNull GoogleMap googleMap, DatabaseReference reference, LatLng destination, LatLng origin) {
        try {
            GoogleDirection.withServerKey(API_KEY)
                    .from(origin)
                    .to(destination)
                    .transportMode(TransportMode.DRIVING)
                    .execute(new DirectionCallback() {
                        @SuppressLint({"DefaultLocale", "SetTextI18n"})
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onDirectionSuccess(Direction direction, String rawBody) {
                            String status = direction.getStatus();
                            switch (status) {
                                case RequestResult.OK:
                                    //Getting data from json
                                    Route route = direction.getRouteList().get(0);
                                    Leg leg = route.getLegList().get(0);
                                    Info distanceInfo = leg.getDistance();
                                    Info durationInfo = leg.getDuration();

                                    //Setting up data
                                    distance = distanceInfo.getText();
                                    duration = durationInfo.getText();
                                    currentLocationName = leg.getStartAddress();
                                    destinationLocationName = leg.getEndAddress();

                                    txtPickUpLocationName.setText(currentLocationName);
                                    txtDropOffLocationName.setText(destinationLocationName);

                                    //Drawing route
                                    ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                                    PolylineOptions polylineOptions = DirectionConverter.createPolyline(Objects.requireNonNull(getActivity()),
                                            directionPositionList, 5, Color.RED);
                                    googleMap.addPolyline(polylineOptions);

                                    //Bounds
                                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                    builder.include(origin);
                                    builder.include(destination);
                                    LatLngBounds bounds = builder.build();

                                    int width = getResources().getDisplayMetrics().widthPixels;
                                    int height = getResources().getDisplayMetrics().heightPixels;
                                    int padding = (int) (width * 0.20); // offset from edges of the map 10% of screen

                                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);

                                    map.animateCamera(cu);

                                    //Cost
                                    cost = getCost(duration, distance);
                                    if (cost != 0.0) {
                                        txtCost.setText(cost + " JD");
                                    } else {
                                        getCost(duration, distance);
                                        txtCost.setText(cost + " JD");
                                    }

                                    break;
                                case RequestResult.NOT_FOUND:
                                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), Constants.NO_ROUTE_EXIST, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "No", Toast.LENGTH_SHORT).show();
                                    Log.d("Status", status);
                                    break;
                            }
                        }

                        @Override
                        public void onDirectionFailure(Throwable t) {
                            t.printStackTrace();
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
        setDriverName(reference);
    }

    private void writeTripData(@NotNull DatabaseReference reference, String driverName, String id) {
        //Data
        float[] result = new float[1];
        Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, destination.latitude, destination.longitude, result);
        @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        rideNumber = getLastRideNumber(reference, id);

        //Setting up the Objects
        TripRoute tripRoute = new TripRoute(currentLocationName, destinationLocationName, distance, duration, STATUS_DRIVING_STARTING);
        Ride ride = new Ride(Constants.description(currentLocationName, destinationLocationName), cost + " JD", date, tripRoute);
        Rating rating = new Rating("", String.valueOf(rideNumber));
        Trip trip = new Trip(driversNameAndID.get(txtName.getText().toString()), user, ride, driverName, rating);
        //writing to the database
        reference.child(id).child(DATABASE_TRIP).child(String.valueOf(rideNumber)).updateChildren(trip.toFullTripMap()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) rideNumber++;
        });
    }

    private int getLastRideNumber(DatabaseReference reference, String id) {
        Query lastQuery = reference.child(id).child(DATABASE_TRIP).orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                System.out.println(dataSnapshot.toString());
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        rideNumber = Integer.valueOf(Objects.requireNonNull(snapshot.getKey()));
                    }
                } else {
                    rideNumber = 0;
                }
                System.out.println(rideNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return rideNumber;
    }

    private void setupDenyLayout(String s) {
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
        cardViewConfirmTrip.setVisibility(View.GONE);
        btnRoute.setVisibility(View.GONE);
        map.clear();
        btnConfirmRide.setText(CONFIRM_RIDE);
    }

    //Return the nearest driver id and name
    private void getDrivers(@NonNull DatabaseReference reference) {
        //Data
        getLocation();
        HashMap<HashMap<Float, String>, LinkedHashMap<LatLng, Float>> driversData = new HashMap<>();
        LinkedHashMap<LatLng, Float> driversLocation = new LinkedHashMap<>();  //Location , Distance
        float[] distanceBetweenLocations = new float[1];
        double[] latlng = new double[2];

        //Getting Data from the Database
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        System.out.println(userSnapshot.toString());
                        getLocation();
                        if (userSnapshot.exists()) {
                            //Objects
                            int i = 0;
                            String driverID = userSnapshot.getKey();

                            //Validating
                            if (Objects.requireNonNull(driverID).equals(uid) || driverID.equals(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()))
                                continue;
                            if (Objects.requireNonNull(userSnapshot.child(DATABASE_IS_DRIVER).getValue()).toString().equalsIgnoreCase(FALSE))
                                continue;

                            //Getting Ride Full Info
                            if (!(Objects.requireNonNull(driverID).equals(uid)
                                    && Objects.requireNonNull(userSnapshot.child(DATABASE_IS_DRIVER).getValue()).toString().equalsIgnoreCase(TRUE))) {
                                for (DataSnapshot locationSnapshot : userSnapshot.child(DATABASE_USER_CURRENT_LOCATION).getChildren()) {
                                    latlng[i++] = Double.valueOf(Objects.requireNonNull(locationSnapshot.getValue()).toString());
                                }

                                //Setting up the data
                                String name = Objects.requireNonNull(userSnapshot.child(DATABASE_NAME).getValue()).toString();
                                String phoneNumber = Objects.requireNonNull(userSnapshot.child(DATABASE_PHONE_NUMBER).getValue()).toString();
                                LatLng lng = new LatLng(latlng[0], latlng[1]);
                                Marker marker = addMarkerToMap(map, lng, bitmapDescriptorFromVector(Objects.requireNonNull(getActivity()).getApplicationContext()), name);
                                Location.distanceBetween(currentLatLng.latitude, currentLatLng.longitude, lng.latitude, lng.longitude, distanceBetweenLocations);

                                //Putting data into data structures
                                driversLocation.put(lng, distanceBetweenLocations[0]);
                                driversInfo.put(distanceBetweenLocations[0], driverID);
                                driversData.put(driversInfo, driversLocation);
                                markers.put(marker, driverID);
                                driversIDAndNames.put(driverID, name);
                                driversNameAndID.put(name, driverID);
                            }
                        } else {
                            Toast.makeText(context, WENT_WRONG, Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
                Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @NotNull
    private BitmapDescriptor bitmapDescriptorFromVector(Context context) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_directions_car_black_24dp);
        vectorDrawable.setBounds(0, 0, Objects.requireNonNull(vectorDrawable).getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private Float getCost(@NotNull String d0, @NotNull String d1) {
        try {
            float dur = Float.valueOf(d0.substring(0, d0.indexOf(" ")));
            float dis = Float.valueOf(d1.substring(0, d1.indexOf(" ")));

            System.out.println("Duration = " + dur + "\n" + "Distance = " + dis);

            if (dis > 0 && dis <= 5) {
                cost = 0.5f;
            } else if (dis >= 6 && dis <= 11) {
                cost = 1f;
            } else {
                cost = 2f;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cost;
    }

    public void layoutComponents() {
        txtCost = view.findViewById(R.id.txtCost);
        txtName = view.findViewById(R.id.txtNamePrimaryFrag);
        txtDropOffLocationName = view.findViewById(R.id.txtDropOffLocationNameFragPrimary);
        txtPickUpLocationName = view.findViewById(R.id.txtPickUpLocationNameFragPrimary);
        relativeConfirm = view.findViewById(R.id.relativeConfirmTrip);
        cardViewSearch = view.findViewById(R.id.cardViewSearchMainFrag);
        cardViewConfirmTrip = view.findViewById(R.id.cardviewConfirmTrip);
        cardViewCustomerTrip = view.findViewById(R.id.cardviewCustomeTrip);
        btnRoute = view.findViewById(R.id.btnRouteFragMain);
        btnConfirmRide = view.findViewById(R.id.btnConfirmTripFragPrimary);
        btnDenyRide = view.findViewById(R.id.btnDenyTripFragPrimary);
    }

    private void setupTripLayout(GoogleMap googleMap, DatabaseReference reference) {
        btnRoute.setVisibility(View.GONE);
        btnDenyRide.setVisibility(View.GONE);
        cardViewSearch.setVisibility(View.VISIBLE);
        cardViewConfirmTrip.setVisibility(View.VISIBLE);
        relativeConfirm.setVisibility(View.VISIBLE);
        setDriverName(reference);
        googleMap.setOnMarkerClickListener(marker -> {
            txtName.setText(getDriverIDFromMarker(marker));
            System.out.println("Driver ID " + driverID);
            System.out.println("Drive Name " + driverName);
            return false;
        });
    }

    private void setDriverName(DatabaseReference reference) {
        if (!driversInfo.isEmpty()) txtName.setText(getNearestDriverName());
        else getDrivers(reference);
    }

    private void getUserName(DatabaseReference reference, String userID, String driverID) {
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    userName = Objects.requireNonNull(dataSnapshot.child(DATABASE_NAME).getValue()).toString();
                    phoneNumber = Objects.requireNonNull(dataSnapshot.child(DATABASE_PHONE_NUMBER).getValue()).toString();
                    customerInfo = getCustomerInfo();
                    writeCustomerInfo(reference, driverID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void writeCustomerInfo(DatabaseReference reference, String id) {
        reference.child(Objects.requireNonNull(id)).child(DATABASE_REQUESTS).child(String.valueOf(rideNumber)).updateChildren(customerInfo);
    }
}