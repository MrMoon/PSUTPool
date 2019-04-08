
package com.psut.pool.Fragments;

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
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.Objects;

public class MainTabFragment extends Fragment {

    private Fragment fragmentMainTab, fragmentUniMap, fragmentPublicMap;
    private Button btnCaptainMap, btnUniMap, btnPublicMap;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Global Variables and Objects:
        View view = inflater.inflate(R.layout.fragment_main_tab, null);

        //Fragments:
        fragmentMainTab = new PrimaryMapTabFragment();
        fragmentUniMap = new UniMapTabFragment();
        fragmentPublicMap = new PublicMapTabFragment();

        //Buttons:
        btnCaptainMap = view.findViewById(R.id.btnCaptainMainFrag);
        btnUniMap = view.findViewById(R.id.btnUniMapMainFrag);
        btnPublicMap = view.findViewById(R.id.btnPublicMapMainFrag);

        //Default Fragment:
        if (isServicesOk()) {
            Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.linLayoutHomeMainFrag, fragmentMainTab).commit();
        }

        //Buttons:
        btnCaptainMap.setOnClickListener(v -> {
            btnCaptainMap.setBackgroundResource(R.drawable.button_shape_corners);
            btnCaptainMap.setTextColor(getResources().getColor(R.color.white));

            btnUniMap.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnUniMap.setTextColor(getResources().getColor(R.color.background));

            btnPublicMap.setBackgroundResource(R.drawable.button_shape_corners_white);
            btnPublicMap.setTextColor(getResources().getColor(R.color.background));

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
}
