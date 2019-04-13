package com.wolfsoft.kcab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.psut.pool.Adapters.ListAdapter;
import com.psut.pool.Models.ListModel;
import com.psut.pool.R;

import java.util.ArrayList;

public class Activity_list extends AppCompatActivity {

    String txt[] = {"1.Login_icab", "2.Signup_icab", "3.Home_icab", "4.Book_Cab_icab", "5.In_ride_icab"
            , "6.Ride_Complete_rating_icab", "7.Ride_History_icab", "8.Menu_icab"};
    private ListAdapter listAdapter;
    private RecyclerView recyclerview;
    private ArrayList<ListModel> listModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        recyclerview = findViewById(R.id.recyclerView1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(Activity_list.this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        listModelArrayList = new ArrayList<>();

        for (int i = 0; i < txt.length; i++) {

            ListModel listModel = new ListModel(txt[i]);

            listModelArrayList.add(listModel);

        }
        listAdapter = new ListAdapter(Activity_list.this, listModelArrayList);
        recyclerview.setAdapter(listAdapter);

    }
}
