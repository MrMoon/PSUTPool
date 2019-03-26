package com.psut.pool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class VerifyPhoneActivity extends AppCompatActivity implements View.OnClickListener {

    //Global Variables and Objects:
    private EditText txtCode;
    private Button btnConfirm;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private String verificationID, phoneNumber, verificationId;
    private boolean isVerificating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Layout Components:
        txtCode = findViewById(R.id.txtCodeVerfiy);
        btnConfirm = findViewById(R.id.btnConfirmVerfiy);

        //Getting Phone Number:
        phoneNumber = getIntent().getStringExtra(Constants.INTENT_PHONE_NUMBER_KEY);

        //Firebase Objects:
        firebaseAuth = FirebaseAuth.getInstance();

        //Sign In:
        startPhoneNumberVerification(phoneNumber);
        btnConfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            if (txtCode.getText().toString().isEmpty()) {
                txtCode.setError(Constants.INVALID_CODE);
            } else {
                verifyPhoneNumberWithCode(verificationID, txtCode.getText().toString());
            }
        }
    }

    private void verifyPhoneNumberWithCode(String s, String code) {
        PhoneAuthCredential authCredential = PhoneAuthProvider.getCredential(s, code);
        signInWithPhoneAuthCredential(authCredential);
    }

    private void sendverificationcode() {
        startPhoneNumberVerification(phoneNumber);
        verificationCallbacks();
    }

    private void verificationCallbacks() {
        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                if (phoneAuthCredential.getSmsCode() != null) {
                    txtCode.setText(phoneAuthCredential.getSmsCode());
                    signInWithPhoneAuthCredential(phoneAuthCredential);
                }
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    txtCode.setError(Constants.VALID_PHONE_NUMBER);
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Toast.makeText(VerifyPhoneActivity.this, Constants.LIMIT_EXCEEDED, Toast.LENGTH_LONG).show();
                } else {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationID = verificationId;
                resendingToken = forceResendingToken;
            }
        };
    }

    private void startPhoneNumberVerification(String s) {
        verificationCallbacks();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+962" + s,
                60,
                TimeUnit.SECONDS,
                this,
                callbacks
        );
        isVerificating = true;
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                    txtCode.setError(Constants.INVALID_CODE);
                } else {
                    Objects.requireNonNull(task.getException()).printStackTrace();
                }
            }
        });
    }
}
