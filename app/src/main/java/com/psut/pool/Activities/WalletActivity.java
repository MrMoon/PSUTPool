package com.psut.pool.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.psut.pool.R;

import java.util.Objects;

import static com.psut.pool.Shared.Constants.DATABASE_ACCOUNT_TYPE;
import static com.psut.pool.Shared.Constants.DATABASE_USERS;
import static com.psut.pool.Shared.Constants.NOT_VALID_INPUT;
import static com.psut.pool.Shared.Constants.SPECIAL;

public class WalletActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        Objects.requireNonNull(getSupportActionBar()).hide();

        //Objects
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(DATABASE_USERS);

        //Layout
        RelativeLayout relativeLayout = findViewById(R.id.relativeWallet);
        LinearLayout linearLayout = findViewById(R.id.lin);
        TextView txtAccountType = findViewById(R.id.txtAccountTypeWallet);
        EditText txtCode = findViewById(R.id.txtCodeWallet);
        Button btnSubmit = findViewById(R.id.btnSubmitCode);

        relativeLayout.setOnClickListener(v -> finish());
        txtAccountType.setOnClickListener(v -> {
            linearLayout.setVisibility(View.VISIBLE);
            btnSubmit.setOnClickListener(v0 -> {
                if (TextUtils.isEmpty(txtCode.getText().toString())) {
                    txtCode.setError(NOT_VALID_INPUT);
                } else if (txtCode.getText().toString().equals("20190505")) {
                    Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show();
                    setupSpecialAccount(databaseReference);
                } else {
                    txtCode.setVisibility(View.GONE);
                    btnSubmit.setVisibility(View.GONE);
                    Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
                }

            });
        });
    }

    private void setupSpecialAccount(DatabaseReference reference) {
        reference.child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).child(DATABASE_ACCOUNT_TYPE).setValue(SPECIAL);
    }
}
