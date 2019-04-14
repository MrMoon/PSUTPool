package com.psut.pool.Activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;
import com.psut.pool.Shared.Layout;

import java.util.Objects;

public class PersonalInfoActivity extends AppCompatActivity implements Layout {

    //Global Variables and Objects:
    private ImageView imgBackArrow;
    private TextView txtName, txtId, txtNumber, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        Objects.requireNonNull(getSupportActionBar()).hide();

        layoutComponents();

        imgBackArrow.setOnClickListener(this);
    }

    @Override
    public void layoutComponents() {
        imgBackArrow = findViewById(R.id.imgBackArrowPersonalInfo);
        txtName = findViewById(R.id.txtNamePersonalInfo);
        txtId = findViewById(R.id.txtIDPersonalInfo);
        txtNumber = findViewById(R.id.txtNumberPersonalInfo);
        txtEmail = findViewById(R.id.txtEmailPersonalInfo);
        getLayoutComponents();
    }

    @Override
    public void getLayoutComponents() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            readPersonalInfo();
        } else {
            Toast.makeText(this, Constants.WENT_WRONG, Toast.LENGTH_SHORT).show();
        }
    }

    private void readPersonalInfo() {
        try {
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
            databaseReference
                    .child(Constants.DATABASE_USERS)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                txtName.setText(Constants.DATABASE_NAME + ": " + Objects.requireNonNull(dataSnapshot.child(Constants.DATABASE_NAME).getValue()).toString());
                                txtId.setText(Constants.DATABASE_UNI_ID + ": " + Objects.requireNonNull(dataSnapshot.child(Constants.DATABASE_UNI_ID).getValue()).toString());
                                txtEmail.setText(Constants.DATABASE_EMAIL + ": " + Objects.requireNonNull(dataSnapshot.child(Constants.DATABASE_EMAIL).getValue()).toString());
                                txtNumber.setText(Constants.DATABASE_PHONE_NUMBER + ": " + Objects.requireNonNull(dataSnapshot.child(Constants.DATABASE_PHONE_NUMBER).getValue()).toString());
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClickLayout() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgBackArrowPersonalInfo:
                finish();
                break;
        }
    }
}
