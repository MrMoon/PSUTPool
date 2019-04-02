package com.psut.pool.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.psut.pool.Models.Customer;
import com.psut.pool.Models.Driver;
import com.psut.pool.R;
import com.psut.pool.Shared.Constants;
import com.psut.pool.Shared.Layout;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity implements Layout {

    //Global Variables and Objects:
    private EditText txtName, txtEmail, txtID, txtPhoneNumber, txtAddress, txtCarID;
    private Button btnSignUp;
    private Spinner spinnerPreferred;
    private RadioGroup radioGroupGender;
    private RadioButton radioBtnMale, radioBtnFemale;
    private Switch isDriverSwitch;
    private DatabaseReference databaseReference;
    private String phoneNumber, preferred, name, uniID, address, email, gender;
    private boolean isVerified, isDriver;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        layoutComponents();

        //Getting phone number from previous intent:
        phoneNumber = getIntent().getStringExtra(Constants.INTENT_PHONE_NUMBER_KEY);
        txtPhoneNumber.setText(phoneNumber);

        //Firebase Objects:
        databaseReference = FirebaseDatabase.getInstance().getReference().child(Constants.DATABASE_USERS);

        btnSignUp.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        getLayoutComponents();
        if (isValid()) {
            //User Object:
            if (isDriver) {
                Driver driver = new Driver(name, email, uniID, phoneNumber, address, preferred, gender, Boolean.toString(isDriver), "Online", txtCarID.getText().toString());
                databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).updateChildren(driver.toDriverMap()); //Database writing
                toMain();   //Update UI
            } else {
                Customer customer = new Customer(name, email, uniID, phoneNumber, address, preferred, gender, Boolean.toString(isDriver), "Online", "0");
                databaseReference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).updateChildren(customer.toCustomerMap()); //Database writing
                toMain();   //Update UI
            }
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

        if (TextUtils.isEmpty(txtEmail.getText().toString()) &&
                Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).matches()) {
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

        if (TextUtils.isEmpty(txtPhoneNumber.getText().toString()) &&
                Patterns.PHONE.matcher(txtPhoneNumber.getText().toString()).matches()) {
            txtPhoneNumber.setError(Constants.NOT_VALID_INPUT);
            isValidFlag = false;
        }
        return isValidFlag;
    }

    private void toMain() {
        //Update UI:
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void layoutComponents() {
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
        isDriverSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDriver = isChecked;
            if (isChecked) txtCarID.setVisibility(View.VISIBLE);
            else txtCarID.setVisibility(View.GONE);
        });
    }

    @Override
    public void getLayoutComponents() {
        name = txtName.getText().toString();
        email = txtEmail.getText().toString();
        uniID = txtID.getText().toString();
        address = txtName.getText().toString();
        preferred = spinnerPreferred.getSelectedItem().toString();
        radioBtnMale.setOnClickListener(v -> gender = "Male");
        radioBtnFemale.setOnClickListener(v -> gender = "Female");
    }
}