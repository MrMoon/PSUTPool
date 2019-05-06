package com.psut.pool.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.Adapters.RidehistoryAdapter;
import com.psut.pool.Models.Rating;
import com.psut.pool.Models.RidehistoryModel;
import com.psut.pool.R;

import java.util.ArrayList;
import java.util.Objects;

import static com.psut.pool.Shared.Constants.DATABASE_AMOUNT;
import static com.psut.pool.Shared.Constants.DATABASE_DATE;
import static com.psut.pool.Shared.Constants.DATABASE_DROP_OFF_LOCATION;
import static com.psut.pool.Shared.Constants.DATABASE_NUMBER;
import static com.psut.pool.Shared.Constants.DATABASE_PICK_UP_LOCATION;
import static com.psut.pool.Shared.Constants.DATABASE_RATING;
import static com.psut.pool.Shared.Constants.DATABASE_TRIP;
import static com.psut.pool.Shared.Constants.DATABASE_TRIP_RANK;
import static com.psut.pool.Shared.Constants.DATABASE_USERS;
import static com.psut.pool.Shared.Constants.DRIVER_ID;
import static com.psut.pool.Shared.Constants.NO_HISTORY;
import static com.psut.pool.Shared.Constants.PLEASE_RATE_THE_TRIP;

public class HistoryActivity extends AppCompatActivity {

    //Global Variables and Objects
    private DatabaseReference databaseReference;
    private RelativeLayout relativeHistory;
    private RecyclerView recyclerViewHistory;
    private RatingBar ratingBarHistory;
    private LinearLayout linearLayout;
    private TextView txtNonHistory, txtDriverID;
    private ArrayList<String> pickUpLocationsNames = new ArrayList<>(), dropOffLocationsNames = new ArrayList<>(), dates = new ArrayList<>(), prices = new ArrayList<>(), driverIDs = new ArrayList<>();
    private String userID, driverID, tripRank, number;
    private boolean flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Objects:
        relativeHistory = findViewById(R.id.relativeHistory);
        recyclerViewHistory = findViewById(R.id.recyclerViewHistory);
        linearLayout = findViewById(R.id.linearLayoutHistory);
        txtNonHistory = findViewById(R.id.txtNonHistory);
        ratingBarHistory = findViewById(R.id.rtbHighScoreHistory);
        txtDriverID = findViewById(R.id.txtDriverIDHistory);
        databaseReference = FirebaseDatabase.getInstance().getReference().child(DATABASE_USERS);
        userID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        relativeHistory.setOnClickListener(v -> finish());

        readData(databaseReference);
    }

    private void setupRating(DatabaseReference reference) {
        ratingBarHistory.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> writeRatingToTrip(reference, txtDriverID.getText().toString(), String.valueOf(rating)));
    }

    private void readData(DatabaseReference reference) {
        reference.child(userID).child(DATABASE_TRIP).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                flag = dataSnapshot.exists();
                if (flag) {
                    linearLayout.setVisibility(View.VISIBLE);
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        //Data
                        String pickUpLocationName = Objects.requireNonNull(snapshot.child(DATABASE_PICK_UP_LOCATION).getValue()).toString();
                        pickUpLocationName = pickUpLocationName.substring(0, pickUpLocationName.indexOf("Jordan"));
                        String dropOffLocationName = Objects.requireNonNull(snapshot.child(DATABASE_DROP_OFF_LOCATION).getValue()).toString();
                        dropOffLocationName = dropOffLocationName.substring(0, dropOffLocationName.indexOf("Jordan"));
                        String date = Objects.requireNonNull(snapshot.child(DATABASE_DATE).getValue()).toString();
                        String price = Objects.requireNonNull(snapshot.child(DATABASE_AMOUNT).getValue()).toString();
                        String driverID = Objects.requireNonNull(snapshot.child(DRIVER_ID).getValue()).toString();

                        //Lists
                        pickUpLocationsNames.add(pickUpLocationName);
                        dropOffLocationsNames.add(dropOffLocationName);
                        dates.add(date);
                        prices.add(price);
                        driverIDs.add(driverID);

                        setupHistory();
                    }
                } else {
                    txtNonHistory.setVisibility(View.VISIBLE);
                    txtNonHistory.setText(NO_HISTORY);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        if (flag) {
            Toast.makeText(this, PLEASE_RATE_THE_TRIP, Toast.LENGTH_SHORT).show();
            setupRating(reference);
        }
    }

    private void setupHistory() {
        //Objects
        Integer i2 = R.drawable.rect_dotted, i1 = R.drawable.pin_black, i3 = R.drawable.navigatiob_blue;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HistoryActivity.this);
        ArrayList<RidehistoryModel> ridehistoryModelArrayList = new ArrayList<>();

        //Layout
        recyclerViewHistory.setLayoutManager(layoutManager);
        recyclerViewHistory.setItemAnimator(new DefaultItemAnimator());


        for (int i = 0; i < dropOffLocationsNames.size(); ++i) {
            RidehistoryModel ridehistoryModel = new RidehistoryModel(i1, i2, i3, dropOffLocationsNames.get(i), pickUpLocationsNames.get(i), dates.get(i), prices.get(i), driverIDs.get(i));
            ridehistoryModelArrayList.add(ridehistoryModel);
        }

        RidehistoryAdapter ridehistoryAdapter = new RidehistoryAdapter(HistoryActivity.this, ridehistoryModelArrayList);
        recyclerViewHistory.setAdapter(ridehistoryAdapter);
    }

    private void writeRatingToTrip(DatabaseReference reference, String driverID, String rating) {
        reference.child(driverID).child(DATABASE_TRIP_RANK).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    tripRank = Objects.requireNonNull(dataSnapshot.child(DATABASE_RATING).getValue()).toString();
                    number = Objects.requireNonNull(dataSnapshot.child(DATABASE_NUMBER).getValue()).toString();
                } else {
                    tripRank = String.valueOf(rating);
                    number = String.valueOf(0);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Rating newRating = new Rating(tripRank, number);
        reference.updateChildren(newRating.toRatingMap());
    }
}