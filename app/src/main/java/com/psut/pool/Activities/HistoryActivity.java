package com.psut.pool.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.psut.pool.Adapters.RidehistoryAdapter;
import com.psut.pool.Models.RidehistoryModel;
import com.psut.pool.R;

import java.util.ArrayList;
import java.util.Objects;

public class HistoryActivity extends AppCompatActivity {

    Integer i1[] = {R.drawable.pin_black, R.drawable.pin_black, R.drawable.pin_black, R.drawable.pin_black, R.drawable.pin_black};
    Integer i2[] = {R.drawable.rect_dotted, R.drawable.rect_dotted, R.drawable.rect_dotted, R.drawable.rect_dotted, R.drawable.rect_dotted};
    Integer i3[] = {R.drawable.navigatiob_blue, R.drawable.navigatiob_blue, R.drawable.navigatiob_blue, R.drawable.navigatiob_blue, R.drawable.navigatiob_blue};
    String txtmall[] = {"Amman, PSUT ", "Amman , Sport City", "Amman , Jordan University", "Amman , Irbid", "Amman , Rainbow Street"};
    String txthome[] = {"Home", "PSUT", "PSUT", "Home", "Home"};
    String txtdate[] = {"01 June 2018", "02 May 2018", "17 Sep 2018", "25 July 2018", "21 Jan 2018"};
    String txtprice[] = {"$0.5", "$1.50", "$2.50", "$3.94", "$2.04"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Objects.requireNonNull(getSupportActionBar()).hide();

        RelativeLayout relativeLayout = findViewById(R.id.relativeHistory);
        relativeLayout.setOnClickListener(v -> finish());
        RecyclerView recyclerview = findViewById(R.id.recycler1);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HistoryActivity.this);
        recyclerview.setLayoutManager(layoutManager);
        recyclerview.setItemAnimator(new DefaultItemAnimator());

        ArrayList<RidehistoryModel> ridehistoryModelArrayList = new ArrayList<>();

        for (int i = 0; i < i1.length; i++) {

            RidehistoryModel listModel = new RidehistoryModel(i1[i], i2[i], i3[i], txtmall[i], txthome[i], txtdate[i], txtprice[i]);

            ridehistoryModelArrayList.add(listModel);

        }
        RidehistoryAdapter ridehistoryAdapter = new RidehistoryAdapter(HistoryActivity.this, ridehistoryModelArrayList);
        recyclerview.setAdapter(ridehistoryAdapter);

    }
}