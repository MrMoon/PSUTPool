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
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

        return view;
    }

    private void isDriver() {
        if (uid != null) {
            try {
                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child(Constants.DATABASE_USERS)
                        .child(uid)
                        .orderByChild(Constants.DATABASE_PHONE_NUMBER)
                        .equalTo(Boolean.toString(true))
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() != null) {
                                    switchIsDriving.setVisibility(View.VISIBLE);
                                    txtStartDriving.setVisibility(View.VISIBLE);
                                } else {
                                    switchIsDriving.setVisibility(View.GONE);
                                    txtStartDriving.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                System.out.println(databaseError.toString());
                            }
                        });
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), Constants.WENT_WRONG, Toast.LENGTH_SHORT).show();
            }
        } else {
            FirebaseAuth auth = FirebaseAuth.getInstance();
            FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    uid = firebaseUser.getUid();
                }
            };
            return;
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
        switchIsDriving.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDriving = isChecked;
        });
    }

    @Override
    public void getLayoutComponents() {

    }
}
