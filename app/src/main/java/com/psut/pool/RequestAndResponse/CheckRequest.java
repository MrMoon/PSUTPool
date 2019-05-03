package com.psut.pool.RequestAndResponse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import static com.psut.pool.Shared.Constants.DATABASE_REQUESTS;
import static com.psut.pool.Shared.Constants.TRUE;

public class CheckRequest {
    private DatabaseReference databaseReference;
    private Context context;
    private Runnable runnable;
    private Thread thread = new Thread(runnable);
    private boolean flag = false;

    public CheckRequest(DatabaseReference databaseReference, Context context) {
        this.databaseReference = databaseReference;
        this.context = context;
    }

    public boolean keepChecking() {
        runnable = () -> checkRequest(databaseReference);
        thread.start();
        Log.d("Thread Name", thread.getName());
        Log.d("Thread Life", String.valueOf(thread.isAlive()));
        return thread.isAlive();
    }

    public boolean checkRequest(DatabaseReference reference) {
        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (flag = Objects.requireNonNull(dataSnapshot.child(DATABASE_REQUESTS).getValue()).toString().equalsIgnoreCase(TRUE)) {
                    Log.d("Driver Request Flag", String.valueOf(flag));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return flag;
    }

}
