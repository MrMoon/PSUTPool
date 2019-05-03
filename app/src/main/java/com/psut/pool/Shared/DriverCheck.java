package com.psut.pool.Shared;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.psut.pool.Shared.Constants.DATABASE_IS_DRIVER;

abstract public class DriverCheck {

    private static String isDriver;

    public static String isDriver(DatabaseReference databaseReference) {
        databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                .child(DATABASE_IS_DRIVER)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.d("DataSnapshot", dataSnapshot.toString());
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
