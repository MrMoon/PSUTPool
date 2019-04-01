package com.psut.pool.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.Objects;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    //Global Variables and Objects:
    private Dialog dialog;
    private Button btnConfirm;
    private EditText txtPhoneNumber;
    private boolean flag = true;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Layout Components:
        dialog = new Dialog(this);
        btnConfirm = findViewById(R.id.btnConfirmStart);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumberStart);

        //Buttons:
        btnConfirm.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    //Passing the phone number to the VerifyPhone Activity or continue to Main:
    private void passPhoneNumber() {
        phoneNumber = txtPhoneNumber.getText().toString();
        if (phoneNumber.isEmpty() || phoneNumber.length() < 9 || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            Toast.makeText(this, Constants.VALID_PHONE_NUMBER, Toast.LENGTH_SHORT).show();
        } else {
            setupDialog();
        }
    }

    private void setupDialog() {
        dialog.setContentView(R.layout.layout_pop_up);
        TextView textView = dialog.findViewById(R.id.txtNumberLayoutPopUp);
        textView.setText(phoneNumber);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.btnEditLayoutPopUp).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnYesLayoutPopUp).setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, VerifyPhoneActivity.class);
            intent.putExtra(Constants.INTENT_PHONE_NUMBER_KEY, phoneNumber);
            Toast.makeText(this, "Your Number is " + phoneNumber, Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
        dialog.show();
    }

    private boolean isVerified(String number) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_USERS);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(this, Constants.WELCOME, Toast.LENGTH_SHORT).show();
            flag = false;
        } else {
            databaseReference.child(Constants.DATABASE_USERS)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .orderByChild(Constants.DATABASE_PHONE_NUMBER).equalTo(number)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                flag = true;
                            } else {
                                flag = false;
                                Toast.makeText(StartActivity.this, Constants.WELCOME, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        return flag;
    }

    private void toMain() {
        //Update UI:
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            if (isVerified(phoneNumber)) {
                toMain();
            } else {
                passPhoneNumber();
            }
        }
    }
}
