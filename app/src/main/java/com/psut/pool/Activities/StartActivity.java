package com.psut.pool.Activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;
import com.psut.pool.Shared.Layout;

import java.util.Objects;

public class StartActivity extends AppCompatActivity implements View.OnClickListener, Layout {

    //Global Variables and Objects:
    private DatabaseReference databaseReference;
    private Dialog dialog;
    private Button btnConfirm;
    private EditText txtPhoneNumber;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Objects.requireNonNull(getSupportActionBar()).hide();

        layoutComponents();

        //Buttons:
        btnConfirm.setOnClickListener(this);
    }

    private void passPhoneNumber() {    //Passing the phone number to the VerifyPhone Activity
        getLayoutComponents();
        if (phoneNumber.isEmpty() || phoneNumber.length() < 9 || !Patterns.PHONE.matcher(phoneNumber).matches()) {
            Toast.makeText(this, Constants.VALID_PHONE_NUMBER, Toast.LENGTH_SHORT).show();
        } else {
            setupDialog();
        }
    }

    private void setupDialog() {    //Setting up the dialog
        dialog.setContentView(R.layout.layout_pop_up);
        TextView textView = dialog.findViewById(R.id.txtNumberLayoutPopUp);
        textView.setText(phoneNumber);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.btnEditLayoutPopUp).setOnClickListener(v -> dialog.dismiss());
        dialog.findViewById(R.id.btnYesLayoutPopUp).setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, VerifyPhoneActivity.class);
            intent.putExtra(Constants.INTENT_PHONE_NUMBER_KEY, phoneNumber);
            startActivity(intent);
        });
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            passPhoneNumber();
        }
    }

    @Override
    public void layoutComponents() {
        //Layout Components:
        dialog = new Dialog(this);
        btnConfirm = findViewById(R.id.btnConfirmStart);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumberStart);
    }

    @Override
    public void getLayoutComponents() {
        phoneNumber = txtPhoneNumber.getText().toString();
    }
}
