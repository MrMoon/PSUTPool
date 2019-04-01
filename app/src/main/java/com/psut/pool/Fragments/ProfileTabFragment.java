package com.psut.pool.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;
import com.psut.pool.Shared.Layout;

import java.util.Objects;

public class ProfileTabFragment extends Fragment implements Layout {

    //Global Variables and Objects:
    private View view;
    private TextView txtPersonalInfo, txtMyRides, txtWallet, txtLogOut, txtStartDriving;
    private Switch switchIsDriving;
    private DatabaseReference databaseReference;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile_tab, null);
        layoutComponents();
        return view;
    }

    @Override
    public void layoutComponents() {
        //Check if driver:
        checkDriver();
        txtPersonalInfo = view.findViewById(R.id.txtPersonalInfoProfileFrag);
        txtStartDriving = view.findViewById(R.id.txtStartDrivingProfileFrag);
        txtMyRides = view.findViewById(R.id.txtMyRidesProfileFrag);
        txtWallet = view.findViewById(R.id.txtWalletProfileFrag);
        txtLogOut = view.findViewById(R.id.txtLogOutProfileFrag);
        switchIsDriving = view.findViewById(R.id.switchStartDrivingProfileFrag);
        switchIsDriving.setOnCheckedChangeListener((buttonView, isChecked) -> {

        });
    }

    private void checkDriver() {
        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child(Constants.DATABASE_USERS)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        databaseReference.orderByChild(Constants.DATABASE_IS_DRIVER).equalTo("true")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            txtStartDriving.setVisibility(View.VISIBLE);
                            switchIsDriving.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void getLayoutComponents() {

    }
}
