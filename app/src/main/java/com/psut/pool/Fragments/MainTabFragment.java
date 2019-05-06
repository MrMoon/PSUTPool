
package com.psut.pool.Fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.Activities.MainActivity;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.Objects;

import static com.psut.pool.Shared.Constants.DATABASE_ACCOUNT_TYPE;
import static com.psut.pool.Shared.Constants.DATABASE_USERS;
import static com.psut.pool.Shared.Constants.SPECIAL;

public class MainTabFragment extends Fragment {

    //Global Variables and Objects
    private Fragment fragmentMainTab, fragmentUniMap, fragmentPublicMap, fragmentUniBus;
    private Button btnCaptainMap, btnUniMap, btnPublicMap, btnUniBus;
    private Boolean flag = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_main_tab, null);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(DATABASE_USERS);

        //Fragments
        fragmentMainTab = new PrimaryMapTabFragment();
        fragmentUniMap = new UniMapTabFragment();
        fragmentPublicMap = new PublicMapTabFragment();
        fragmentUniBus = new UniBusTabFragment();

        //Buttons
        btnCaptainMap = view.findViewById(R.id.btnCaptainMainFrag);
        btnUniMap = view.findViewById(R.id.btnUniMapMainFrag);
        btnPublicMap = view.findViewById(R.id.btnPublicMapMainFrag);
        btnUniBus = view.findViewById(R.id.btnUniBusMainFrag);

        //Default Fragment
        if (isServicesOk()) {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.linLayoutHomeMainFrag, fragmentMainTab).commit();
        }

        if (checkAccountType(databaseReference)) {
            btnUniBus.setVisibility(View.VISIBLE);
            btnUniBus.setOnClickListener(v -> {
                btnPublicMap.setBackgroundResource(R.drawable.button_shape_corners_white);
                btnPublicMap.setTextColor(getResources().getColor(R.color.background));

                btnUniMap.setBackgroundResource(R.drawable.button_shape_corners_white);
                btnUniMap.setTextColor(getResources().getColor(R.color.background));

                btnCaptainMap.setBackgroundResource(R.drawable.button_shape_corners_white);
                btnCaptainMap.setTextColor(getResources().getColor(R.color.background));

                btnUniBus.setBackgroundResource(R.drawable.button_shape_corners);
                btnUniBus.setTextColor(getResources().getColor(R.color.white));

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.linLayoutHomeMainFrag, fragmentUniBus).commit();
            });
        }

        //Buttons
        btnCaptainMap.setOnClickListener(v -> {
            btnCaptainMap.setBackgroundResource(R.drawable.button_shape_corners);
            btnCaptainMap.setTextColor(getResources().getColor(R.color.white));

            btnUniMap.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnUniMap.setTextColor(getResources().getColor(R.color.background));

            btnPublicMap.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnPublicMap.setTextColor(getResources().getColor(R.color.background));

            btnUniBus.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnUniBus.setTextColor(getResources().getColor(R.color.background));

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.linLayoutHomeMainFrag, fragmentMainTab).commit();
        });

        btnUniMap.setOnClickListener(v -> {
            btnUniMap.setBackgroundResource(R.drawable.button_shape_corners);
            btnUniMap.setTextColor(getResources().getColor(R.color.white));

            btnCaptainMap.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnCaptainMap.setTextColor(getResources().getColor(R.color.background));

            btnPublicMap.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnPublicMap.setTextColor(getResources().getColor(R.color.background));

            btnUniBus.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnUniBus.setTextColor(getResources().getColor(R.color.background));

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.linLayoutHomeMainFrag, fragmentUniMap).commit();
        });

        btnPublicMap.setOnClickListener(v -> {
            btnPublicMap.setBackgroundResource(R.drawable.button_shape_corners);
            btnPublicMap.setTextColor(getResources().getColor(R.color.white));

            btnUniMap.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnUniMap.setTextColor(getResources().getColor(R.color.background));

            btnCaptainMap.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnCaptainMap.setTextColor(getResources().getColor(R.color.background));

            btnUniBus.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnUniBus.setTextColor(getResources().getColor(R.color.background));

            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.linLayoutHomeMainFrag, fragmentPublicMap).commit();
        });

        return view;
    }

    private Boolean isServicesOk() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getActivity());
        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), available, Constants.ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(getActivity(), Constants.WENT_WRONG, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private Boolean checkAccountType(DatabaseReference reference) {
        if (MainActivity.getAccountType() != null) {
            if (MainActivity.getAccountType().equals(SPECIAL)) {
                return true;
            } else if (MainActivity.getAccountType().equals("0")) {
                return false;
            } else {
                return checkDatabase(reference);
            }
        } else {
            return checkDatabase(reference);
        }
    }

    private boolean checkDatabase(DatabaseReference reference) {
        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(DATABASE_ACCOUNT_TYPE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    flag = Objects.requireNonNull(dataSnapshot.getValue()).toString().equals(SPECIAL);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return flag;
    }
}
