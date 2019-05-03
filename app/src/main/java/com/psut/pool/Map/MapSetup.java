package com.psut.pool.Map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.psut.pool.Activities.MainActivity;
import com.psut.pool.Models.Customer;
import com.psut.pool.Models.Driver;
import com.psut.pool.Models.User;
import com.psut.pool.Shared.Constants;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.psut.pool.Shared.Constants.DATABASE_USER_STATUS;
import static com.psut.pool.Shared.Constants.DESTINATION;
import static com.psut.pool.Shared.Constants.LOCATION_PERMISSION_REQUEST_CODE;
import static com.psut.pool.Shared.Constants.TRUN_LOCATION_ON;

@SuppressLint("ValidFragment")
public class MapSetup extends Fragment implements MapMethods {

    //Global Variables and Objects
    private boolean isPermissionGranted;
    private String[] permissions;
    private GoogleMap map;
    private Location currentLocation;
    private LatLng destination, currentLatLng;
    private boolean isDriver = Boolean.valueOf(MainActivity.getIsDriver());
    private DatabaseReference databaseReference;
    private Activity activity;

    public MapSetup(GoogleMap map, DatabaseReference databaseReference, Activity activity) {
        this.map = map;
        this.databaseReference = databaseReference;
        this.activity = activity;
    }

    public MapSetup(GoogleMap map, Activity activity) {
        this.map = map;
        this.activity = activity;
    }

    public MapSetup() {
    }

    public static Marker addMarkerToMap(@NotNull GoogleMap googleMap, LatLng latLng, BitmapDescriptor bitmapDescriptor, String title) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng).title(title).icon(bitmapDescriptor);
        Log.d("Marker", title);
        return googleMap.addMarker(markerOptions);
    }

    public void initObj() {
        ArrayList<LatLng> markerPoints = new ArrayList<>();
        boolean flag = getLocationPermission(activity);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(DATABASE_USER_STATUS);
    }

    public boolean isPermissionGranted() {
        return isPermissionGranted;
    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean isDriver() {
        return isDriver;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public User getUser() {
        if (isDriver) {
            return new Driver();
        } else {
            return new Customer();
        }
    }

    public void addPrimaryMarker(GoogleMap googleMap, @NotNull ArrayList<LatLng> list, LatLng lng, Button button, CardView cardView) {
        //Checking if there is no marker on the map:
        if (list.size() > 1) {
            list.clear();
            googleMap.clear();
            cardView.setVisibility(View.GONE);
        }

        //Adding new item to the list:
        list.add(lng);
        addMarkerToMap(googleMap, lng, BitmapDescriptorFactory.defaultMarker(HUE_GREEN), DESTINATION);
        button.setVisibility(View.VISIBLE);
    }

    @NotNull
    public BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, Objects.requireNonNull(vectorDrawable).getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private boolean getLocationPermission(Activity activity) {  //Getting Permissions from the user
        permissions = new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(activity).getApplicationContext()
                , Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(activity.getApplicationContext()
                    , Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true;
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
        return isPermissionGranted;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.LOCATION_AUTO_COMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                System.out.println(place.getName());
                geoLocation(place.getName(), map);
            } else {
                System.out.println(PlaceAutocomplete.getStatus(Objects.requireNonNull(getActivity()).getApplicationContext(), data));
            }
        }
    }

    public void geoLocation(String s, GoogleMap googleMap) {
        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = (geocoder.getFromLocationName(s, 1));
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!(addresses.isEmpty())) {
            Address address = addresses.get(0);
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), googleMap);
        }
    }

    public void moveCamera(LatLng lng, GoogleMap googleMap) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lng, Constants.DEFAULT_ZOOM));
    }

    @Override
    public void getLocation(Activity activity, GoogleMap googleMap, DatabaseReference reference) {
        try {
            if (isPermissionGranted) {  //Re-Checking permission
                FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(Objects.requireNonNull(activity));
                //Permission isn't granted
                if (!((LocationManager) (activity.getSystemService(Context.LOCATION_SERVICE)))
                        .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Toast.makeText(activity, TRUN_LOCATION_ON, Toast.LENGTH_LONG).show();
                    return;
                }

                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(permissions, LOCATION_PERMISSION_REQUEST_CODE);
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

                            writeUserData(reference);

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), googleMap);
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

    public void writeUserData(DatabaseReference reference) {
        User user;
        if (isDriver) {
            user = new Driver();
            user.setCurruntLatitude(String.valueOf(currentLocation.getLatitude()));
            user.setCurruntLongitude(String.valueOf(currentLocation.getLongitude()));
            reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(Constants.DATABASE_USER_CURRENT_LOCATION).updateChildren(user.toUserLocationMap());
        } else {
            user = new Customer();
            user.setCurruntLatitude(String.valueOf(currentLocation.getLatitude()));
            user.setCurruntLongitude(String.valueOf(currentLocation.getLongitude()));
            reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(Constants.DATABASE_USER_CURRENT_LOCATION).updateChildren(user.toUserLocationMap());
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void setupMapSettings(GoogleMap googleMap) {
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.setBuildingsEnabled(true);
        googleMap.setIndoorEnabled(false);
        googleMap.setTrafficEnabled(false);
        UiSettings uiSettings = googleMap.getUiSettings();
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setScrollGesturesEnabled(true);
        uiSettings.setTiltGesturesEnabled(true);
        uiSettings.setRotateGesturesEnabled(true);
    }

    @Override
    public void currentLocationButton(View view) {
        View locationButton = ((View) view.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.setMargins(0, 180, 180, 0);
    }

    @Override
    public void setupAutoCompleteSearch(CardView cardView, Activity activity) {
        cardView.setOnClickListener(v -> {
            // Set the fields to specify which types of place data to return.
            List<Place.Field> fields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID,
                    com.google.android.libraries.places.api.model.Place.Field.NAME);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields).setCountry(Constants.COUNTRY_ID)
                    .build(Objects.requireNonNull(activity).getApplicationContext());
            activity.startActivityForResult(intent, Constants.LOCATION_AUTO_COMPLETE_REQUEST_CODE);
        });
    }

    @Override
    public void setupMap(GoogleMap googleMap, Activity activity, View view, CardView cardView, DatabaseReference databaseReference) {
        //getLocation(activity , googleMap , databaseReference);
        setupMapSettings(googleMap);
        currentLocationButton(view);
        setupAutoCompleteSearch(cardView, activity);
    }

}
