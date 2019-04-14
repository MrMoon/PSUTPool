package com.psut.pool.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;

import com.psut.pool.R;

import java.util.Objects;

public class ContactUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        Objects.requireNonNull(getSupportActionBar()).hide();

        RelativeLayout relativeLayout = findViewById(R.id.relativeContactUs);
        relativeLayout.setOnClickListener(v -> finish());
    }
}
