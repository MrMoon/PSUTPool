package com.psut.pool.Shared;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.Activities.MainActivity;

import java.util.Objects;

abstract public class Authentication extends AppCompatActivity {

    public static void isVerified(String id, Context context) {
        if (id == null)
            id = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        //Checking if user exists:
        databaseReference.child(Constants.DATABASE_USERS)
                .child(id)
                .orderByChild(Constants.DATABASE_PHONE_NUMBER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            toMain(context);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    public static void toMain(Context context) { //Update UI:
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
