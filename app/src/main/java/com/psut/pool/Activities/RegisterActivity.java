package com.psut.pool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.psut.pool.Models.Customer;
import com.psut.pool.Models.Driver;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    //Global Variables and Objects:
    private EditText txtName, txtEmail, txtID, txtPhoneNumber, txtAddress, txtCarID;
    private Button btnSignUp;
    private Spinner spinnerPreferred;
    private RadioGroup radioGroupGender;
    private RadioButton radioBtnMale, radioBtnFemale;
    private Switch isDriverSwitch;
    private DatabaseReference databaseReference;
    private String phoneNumber, preferred, name, uniID, address, email, gender;
    private boolean flag, isDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Layout Components:
        txtName = findViewById(R.id.txtNameRegister);
        txtEmail = findViewById(R.id.txtEmailRegister);
        txtID = findViewById(R.id.txtIDRegister);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumberRegister);
        txtAddress = findViewById(R.id.txtAddressRegister);
        txtCarID = findViewById(R.id.txtCarIDRegister);
        btnSignUp = findViewById(R.id.btnSignUpRegister);
        spinnerPreferred = findViewById(R.id.spinnerPreferredDriverRegister);
        radioGroupGender = findViewById(R.id.radioGroupGendersRegister);
        radioBtnMale = findViewById(R.id.radioBtnMaleRegister);
        radioBtnFemale = findViewById(R.id.radioBtnFemaleRegister);
        isDriverSwitch = findViewById(R.id.switchDriverRegister);

        //Objects:
        Intent intent = new Intent();
        phoneNumber = intent.getStringExtra(Constants.INTENT_PHONE_NUMBER_KEY);
        txtPhoneNumber.setText(phoneNumber); //Phone number is already given

        //Firebase Objects:
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_USERS);

        if (!isVerified(phoneNumber)) {
            btnSignUp.setVisibility(View.VISIBLE);
            btnSignUp.setOnClickListener(v -> registerUser());
        }

    }

    private boolean isVerified(String number) {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(RegisterActivity.this, Constants.WELCOME, Toast.LENGTH_SHORT).show();
            flag = false;
        } else {
            databaseReference.child(Constants.DATABASE_USERS)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                    .orderByChild(Constants.DATABASE_PHONE_NUMBER).equalTo(number)
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() != null) {
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                flag = true;
                                startActivity(intent);
                                finish();
                            } else {
                                flag = false;
                                Toast.makeText(RegisterActivity.this, Constants.WELCOME, Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        return flag;
    }

    private void registerUser() {
        if (isValid()) {
            //Getting Layout:
            name = txtName.getText().toString();
            email = txtEmail.getText().toString();
            uniID = txtID.getText().toString();
            address = txtName.getText().toString();
            preferred = spinnerPreferred.getSelectedItem().toString();
            radioBtnMale.setOnClickListener(v -> gender = "Male");
            radioBtnFemale.setOnClickListener(v -> gender = "Female");
            isDriverSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                isDriver = isChecked;
                if (isChecked) txtCarID.setVisibility(View.VISIBLE);
            });

            //User Object:
            if (isDriver) {
                Driver driver = new Driver(uniID, name, phoneNumber, gender, preferred, "true", txtCarID.getText().toString());
                databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).updateChildren(driver.toDriverMap());
            } else {
                Customer customer = new Customer(uniID, name, phoneNumber, gender, preferred, "false", "0");
                databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).updateChildren(customer.toCustomerMap());
            }

            //Update UI:
            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private boolean isValid() {
        boolean isValidFlag = true;
        if (TextUtils.isEmpty(txtName.getText().toString())) {
            txtName.setError(Constants.NOT_VALID_INPUT);
            isValidFlag = false;
        }

        if (TextUtils.isEmpty(txtAddress.getText().toString())) {
            txtAddress.setError(Constants.NOT_VALID_INPUT);
            isValidFlag = false;
        }

        if (TextUtils.isEmpty(txtEmail.getText().toString())) {
            txtEmail.setError(Constants.NOT_VALID_INPUT);
            isValidFlag = false;
        }

        if (isDriver) {
            if (TextUtils.isEmpty(txtCarID.getText().toString())) {
                txtCarID.setError(Constants.NOT_VALID_INPUT);
                isValidFlag = false;
            }
        }

        if (TextUtils.isEmpty(txtID.getText().toString())) {
            txtID.setError(Constants.NOT_VALID_INPUT);
            isValidFlag = false;
        }

        if (TextUtils.isEmpty(txtPhoneNumber.getText().toString())) {
            txtPhoneNumber.setError(Constants.NOT_VALID_INPUT);
            isValidFlag = false;
        }
        return isValidFlag;
    }
}
