package com.psut.pool.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.psut.pool.Fragments.MainTabFragment;
import com.psut.pool.Fragments.NotificationTabFragment;
import com.psut.pool.Fragments.OfferTabFragment;
import com.psut.pool.Fragments.ProfileTabFragment;
import com.psut.pool.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //Global Variables and Objects:
    private ImageView imgCar, imgOffer, imgNotification, imgAccount;
    private Fragment fragmentMainTab, fragmentOfferTab, fragmentNofitication, fragmentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

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

        //Default Fragment:
        getSupportFragmentManager().beginTransaction().replace(R.id.linLayoutHomeMain, fragmentMainTab).commit();

        //All Fragments:
        imgOffer.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgOfferMain:
                imgOffer.setImageResource(R.drawable.ic_local_offer_black_24dp);
                imgCar.setImageResource(R.drawable.ic_directions_car_gray_24dp);
                imgAccount.setImageResource(R.drawable.ic_account_circle_gray_24dp);
                imgNotification.setImageResource(R.drawable.ic_notifications_gray_24dp);
                getSupportFragmentManager().beginTransaction().replace(R.id.linLayoutHomeMain, fragmentOfferTab).commit();
                break;

            case R.id.imgNotificationMain:
                imgNotification.setImageResource(R.drawable.ic_notifications_black_24dp);
                imgOffer.setImageResource(R.drawable.ic_local_offer_gray_24dp);
                imgCar.setImageResource(R.drawable.ic_directions_car_gray_24dp);
                imgAccount.setImageResource(R.drawable.ic_account_circle_gray_24dp);
                getSupportFragmentManager().beginTransaction().replace(R.id.linLayoutHomeMain, fragmentNofitication).commit();
                break;

            case R.id.imgProfileFrag:
                imgAccount.setImageResource(R.drawable.ic_account_circle_black_24dp);
                imgOffer.setImageResource(R.drawable.ic_local_offer_gray_24dp);
                imgCar.setImageResource(R.drawable.ic_directions_car_gray_24dp);
                imgNotification.setImageResource(R.drawable.ic_notifications_gray_24dp);
                getSupportFragmentManager().beginTransaction().replace(R.id.linLayoutHomeMain, fragmentProfile).commit();
                break;
        }
    }
}
