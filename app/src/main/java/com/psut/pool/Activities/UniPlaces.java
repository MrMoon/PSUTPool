package com.psut.pool.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Objects;

import javax.annotation.Nullable;

import static com.psut.pool.Shared.Constants.MARKER_ID;
import static com.psut.pool.Shared.Constants.MARKER_LAYOUT;
import static com.psut.pool.Shared.Constants.WENT_WRONG;

public class UniPlaces extends AppCompatActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Objects.requireNonNull(getSupportActionBar()).hide();
        Intent intent = getIntent();
        int id = intent.getIntExtra(MARKER_ID, 0);
        int layout = intent.getIntExtra(MARKER_LAYOUT, 0);
        System.out.println(layout);
        if (id != 0 && layout != 0) {
            setContentView(layout);
            LinearLayout linearLayout = findViewById(id);
            linearLayout.setOnClickListener(v -> finish());
        } else {
            Toast.makeText(this, WENT_WRONG, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
