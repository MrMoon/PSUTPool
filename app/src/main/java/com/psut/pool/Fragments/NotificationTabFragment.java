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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.psut.pool.Adapters.NotificationsAdapter;
import com.psut.pool.Models.Notifications;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationTabFragment extends Fragment {

    //Global Variables and Objects:
    private View view;
    private ListView listView;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<Notifications> notifications;
    private DatabaseReference databaseReference;

    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification_tab, null);

        //Objects:
        listView = view.findViewById(R.id.listViewFragNofi);
        notifications = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_USERS);
        setupListView(listView);
        return view;
    }

    private void setupListView(ListView view) {
        setupAdapter();
        notificationsAdapter = new NotificationsAdapter(Objects.requireNonNull(getActivity()).getApplicationContext(), notifications);
        view.setAdapter(notificationsAdapter);
    }

    private void setupAdapter() {
        notifications.add(new Notifications(Constants.WELCOME));
    }
}
