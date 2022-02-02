package com.sonu.advancesonu;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class success extends AppCompatActivity {

    private TextView ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);

        ref = findViewById(R.id.ref);
        ref.setText("refrence id : "+System.currentTimeMillis()+"");
    }
}