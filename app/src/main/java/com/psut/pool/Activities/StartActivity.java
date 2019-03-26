package com.psut.pool.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.Objects;

public class StartActivity extends AppCompatActivity implements View.OnClickListener {

    //Global Variables and Objects:
    private Dialog dialog;
    private Button btnConfirm;
    private EditText txtPhoneNumber;
    private FirebaseAuth firebaseAuth;
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

        btnConfirm.setOnClickListener(this);


        //Firebase:
        firebaseAuth = FirebaseAuth.getInstance();
    }

    //Passing the phone number to the VerifyPhone Activity
    private void passPhoneNumber() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            phoneNumber = txtPhoneNumber.getText().toString();
            if (phoneNumber.isEmpty() || phoneNumber.length() < 9) {
                Toast.makeText(this, Constants.VALID_PHONE_NUMBER, Toast.LENGTH_SHORT).show();
            } else {
                setupDialog();
            }
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

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) passPhoneNumber();
    }
}
