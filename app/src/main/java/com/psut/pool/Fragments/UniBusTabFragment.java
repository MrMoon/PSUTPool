package com.psut.pool.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.psut.pool.R;

import static com.psut.pool.R.drawable.uni_bus_0;
import static com.psut.pool.R.drawable.uni_bus_1;
import static com.psut.pool.R.drawable.uni_bus_2;

public class UniBusTabFragment extends Fragment {
    int i = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.fragment_unibus_tab, null);
        ImageView imageView = view.findViewById(R.id.imgUniBusFrag);
        imageView.setImageDrawable(getResources().getDrawable(uni_bus_0));
        imageView.setOnClickListener(v -> {
            if (i == 0) {
                imageView.setImageDrawable(getResources().getDrawable(uni_bus_0));
                ++i;
            } else if (i == 1) {
                imageView.setImageDrawable(getResources().getDrawable(uni_bus_1));
                ++i;
            } else if (i == 2) {
                imageView.setImageDrawable(getResources().getDrawable(uni_bus_2));
                i = 0;
            }
        });

        return view;
    }
}
