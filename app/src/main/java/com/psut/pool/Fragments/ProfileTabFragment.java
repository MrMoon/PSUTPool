package com.psut.pool.Fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.Activities.PersonalInfoActivity;
import com.psut.pool.Activities.StartActivity;
import com.psut.pool.Models.Customer;
import com.psut.pool.Models.Driver;
import com.psut.pool.Models.User;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;
import com.psut.pool.Shared.Layout;

import java.util.Objects;

public class ProfileTabFragment extends Fragment implements Layout {

    //Global Variables and Objects:
    private View view;
    private TextView txtPersonalInfo, txtMyRides, txtWallet, txtLogOut, txtStartDriving;
    private Switch switchIsDriving;
    private User user;
    private String uid;
    private boolean isDriver, isDriving;


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects:
        view = inflater.inflate(R.layout.fragment_profile_tab, null);
        uid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        layoutComponents();
        onClickLayout();

        return view;
    }

    private void isDriver() {
        if (uid != null) {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child(Constants.DATABASE_USERS)
                        .child(uid)
                        .child(Constants.DATABASE_IS_DRIVER)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    String driver = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                                    if (driver.equalsIgnoreCase("false")) {
                                        isDriver = false;
                                        switchIsDriving.setVisibility(View.GONE);
                                        txtStartDriving.setVisibility(View.GONE);
                                    } else {
                                        isDriver = true;
                                        Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Welcome Drive :)", Toast.LENGTH_SHORT).show();
                                        switchIsDriving.setVisibility(View.VISIBLE);
                                        txtStartDriving.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    Toast.makeText(Objects.requireNonNull(getActivity()).getApplicationContext(), "Empty", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), Constants.WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
        } else {
            FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    uid = firebaseUser.getUid();
                }
            };
        }
    }

    @Override
    public void layoutComponents() {
        //Check if driver:
        isDriver();
        txtPersonalInfo = view.findViewById(R.id.txtPersonalInfoProfileFrag);
        txtStartDriving = view.findViewById(R.id.txtStartDrivingProfileFrag);
        txtMyRides = view.findViewById(R.id.txtMyRidesProfileFrag);
        txtWallet = view.findViewById(R.id.txtWalletProfileFrag);
        txtLogOut = view.findViewById(R.id.txtLogOutProfileFrag);
        switchIsDriving = view.findViewById(R.id.switchStartDrivingProfileFrag);
        switchIsDriving.setOnCheckedChangeListener((buttonView, isChecked) -> isDriving = isChecked);
    }

    @Override
    public void getLayoutComponents() {

    }

    @Override
    public void onClickLayout() {
        txtLogOut.setOnClickListener(this);
        txtPersonalInfo.setOnClickListener(this);
        txtWallet.setOnClickListener(this);
        txtMyRides.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtLogOutProfileFrag:
                //Check User type:
                if (isDriver) {
                    user = new Driver("Offline");
                } else {
                    user = new Customer("Offline");
                }
                //Writing status to database:
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child(Constants.DATABASE_USERS)
                        .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                        .updateChildren(user.toOfflineMap());
                //Signing out:
                FirebaseAuth.getInstance().signOut();
                //Exiting app:
                Intent startActivityIntent = new Intent(getActivity(), StartActivity.class);
                startActivity(startActivityIntent);
                Objects.requireNonNull(getActivity()).finish();
                break;

            case R.id.txtPersonalInfoProfileFrag:
                Intent personalInfoIntent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivity(personalInfoIntent);
                break;

        }

    }
}
