package com.psut.pool.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.psut.pool.Adapters.OffersAdapter;
import com.psut.pool.Models.Offers;
import com.psut.pool.R;

import java.util.ArrayList;

public class OfferTabFragment extends Fragment {

    //Global Variables and Objects:
    private View view;
    private ListView listView;
    private ArrayList<Offers> offers;
    private OffersAdapter offersAdapter;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_offer_tab, null);

        //Objects:
        listView = view.findViewById(R.id.listViewFragOffer);
        offers = new ArrayList<>();
        setupListView(listView);
        return view;
    }

    private void setupListView(ListView view) {
        setupAdapter();
    }

    private void setupAdapter() {
        offers.add(new Offers("Enjoy 30% off of all T&B orders", R.drawable.psut));
    }
}
