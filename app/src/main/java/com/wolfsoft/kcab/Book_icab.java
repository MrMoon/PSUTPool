package com.wolfsoft.kcab;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.psut.pool.R;

public class Book_icab extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    ImageView car1, car2, car3, car4, car5;
    private double radius = 2000;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_icab);

        car1 = findViewById(R.id.car1);
        car2 = findViewById(R.id.car2);
        car3 = findViewById(R.id.car3);
        car4 = findViewById(R.id.car4);
        car1.setOnClickListener(this);
        car2.setOnClickListener(this);
        car3.setOnClickListener(this);
        car4.setOnClickListener(this);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.googleMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng origin = new LatLng(-7.788969, 110.338382);
        LatLng destination = new LatLng(-7.781200, 110.349709);

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(origin)
                .include(destination).build();
        Point displaySize = new Point();
        getWindowManager().getDefaultDisplay().getSize(displaySize);
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));


        LatLng latLng = new LatLng(-7.788969, 110.338382);

        double lat = -7.788969;
        double lng = 110.338382;


        // create marker
        MarkerOptions marker = new MarkerOptions().position(latLng).title("Set Pickup Point");
        // adding marker
        googleMap.addMarker(marker);
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));
        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                latLng).zoom(14).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.car1:
                car1.setImageResource(R.drawable.car);
                car2.setImageResource(R.drawable.car3);
                car3.setImageResource(R.drawable.car5);
                car4.setImageResource(R.drawable.car3);
                break;

            case R.id.car2:
                car1.setImageResource(R.drawable.car3);
                car2.setImageResource(R.drawable.car);
                car3.setImageResource(R.drawable.car5);
                car4.setImageResource(R.drawable.car3);
                break;
            case R.id.car3:
                car1.setImageResource(R.drawable.car3);
                car2.setImageResource(R.drawable.car5);
                car3.setImageResource(R.drawable.car);
                car4.setImageResource(R.drawable.car3);
                break;
            case R.id.car4:
                car1.setImageResource(R.drawable.car3);
                car2.setImageResource(R.drawable.car5);
                car3.setImageResource(R.drawable.car3);
                car4.setImageResource(R.drawable.car);
                break;
        }
    }
}
