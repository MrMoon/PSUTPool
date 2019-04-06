package com.psut.pool.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.chrisbanes.photoview.PhotoView;
import com.psut.pool.R;

public class PublicMapTabFragment extends Fragment {

    //Global Variables and Objects:
    private View view;
    private PhotoView photoView;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Objects:
        view = inflater.inflate(R.layout.fragment_public_map_tab, null);
        photoView = view.findViewById(R.id.imgPublicMapFragPublic);
        photoView.setImageResource(R.drawable.public_map);
        return view;
    }
}
