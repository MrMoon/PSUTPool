package com.psut.pool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {

    //Global Variables and Objects:
    private static String isDriver;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Objects.requireNonNull(getSupportActionBar()).hide();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_USERS);
        splash();
    }

    private void splash() {
        new Handler().postDelayed(this::checkUser, 2000);
    }

    private void checkUser() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            MainActivity.setIsDriver(isDriver());
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.INTENT_ID_STRING, isDriver());
            startActivity(intent);
            this.finish();
        } else {
            Intent intent = new Intent(this, StartActivity.class);
            startActivity(intent);
            this.finish();
        }
    }

    private String isDriver() {
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child(Constants.DATABASE_IS_DRIVER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            isDriver = Objects.requireNonNull(dataSnapshot.getValue()).toString();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        return isDriver;
    }
}
