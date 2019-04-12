package com.psut.pool.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.Fragments.MainTabFragment;
import com.psut.pool.Fragments.NotificationTabFragment;
import com.psut.pool.Fragments.OfferTabFragment;
import com.psut.pool.Fragments.ProfileTabFragment;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;
import com.psut.pool.Shared.Layout;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements Layout {

    //Global Variables and Objects:
    private ImageView imgCar, imgOffer, imgNotification, imgAccount;
    private Fragment fragmentMainTab, fragmentOfferTab, fragmentNofitication, fragmentProfile;
    private static String isDriver;
    private DatabaseReference databaseReference;

    public static String getIsDriver() {
        return isDriver;
    }

    public static void setIsDriver(String isDriver) {
        MainActivity.isDriver = isDriver;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Objects:
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_USERS);

        layoutComponents();
        setupFragments();
        isDriver = isDriver();
    }

    private String isDriver() {
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child(Constants.DATABASE_IS_DRIVER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            isDriver = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return isDriver;
    }

    @Override
    public void layoutComponents() {
        //Layout Components:
        imgCar = findViewById(R.id.imgCarMain);
        imgOffer = findViewById(R.id.imgOfferMain);
        imgNotification = findViewById(R.id.imgNotificationMain);
        imgAccount = findViewById(R.id.imgAccountMain);

        //Fragments:
        fragmentMainTab = new MainTabFragment();
        fragmentOfferTab = new OfferTabFragment();
        fragmentNofitication = new NotificationTabFragment();
        fragmentProfile = new ProfileTabFragment();
    }

    private void setupFragments() {

        //Default Fragment:
        getSupportFragmentManager().beginTransaction().replace(R.id.linLayoutHomeMain, fragmentMainTab).commit();

        //Other Fragments:
        imgOffer.setOnClickListener(v -> {
            imgOffer.setImageResource(R.drawable.ic_local_offer_black_24dp);
            imgCar.setImageResource(R.drawable.ic_directions_car_gray_24dp);
            imgAccount.setImageResource(R.drawable.ic_account_circle_gray_24dp);
            imgNotification.setImageResource(R.drawable.ic_notifications_gray_24dp);
            getSupportFragmentManager().beginTransaction().replace(R.id.linLayoutHomeMain, fragmentOfferTab).commit();
        });

        imgNotification.setOnClickListener(v -> {
            imgNotification.setImageResource(R.drawable.ic_notifications_black_24dp);
            imgOffer.setImageResource(R.drawable.ic_local_offer_gray_24dp);
            imgCar.setImageResource(R.drawable.ic_directions_car_gray_24dp);
            imgAccount.setImageResource(R.drawable.ic_account_circle_gray_24dp);
            getSupportFragmentManager().beginTransaction().replace(R.id.linLayoutHomeMain, fragmentNofitication).commit();
        });

        imgAccount.setOnClickListener(v -> {
            imgAccount.setImageResource(R.drawable.ic_account_circle_black_24dp);
            imgOffer.setImageResource(R.drawable.ic_local_offer_gray_24dp);
            imgCar.setImageResource(R.drawable.ic_directions_car_gray_24dp);
            imgNotification.setImageResource(R.drawable.ic_notifications_gray_24dp);
            getSupportFragmentManager().beginTransaction().replace(R.id.linLayoutHomeMain, fragmentProfile).commit();
        });

        imgCar.setOnClickListener(v -> {
            imgCar.setImageResource(R.drawable.ic_directions_car_black_24dp);
            imgAccount.setImageResource(R.drawable.ic_account_circle_gray_24dp);
            imgOffer.setImageResource(R.drawable.ic_local_offer_gray_24dp);
            imgNotification.setImageResource(R.drawable.ic_notifications_gray_24dp);
            getSupportFragmentManager().beginTransaction().replace(R.id.linLayoutHomeMain, fragmentMainTab).commit();
        });
    }

    @Override
    public void getLayoutComponents() {
    }

    @Override
    public void onClickLayout() {
    }

    @Override
    public void onClick(View v) {
    }
}
