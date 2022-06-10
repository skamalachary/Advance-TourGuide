package com.sonu.advancesonu.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.sonu.advancesonu.R;

public class StartNearActivity extends AppCompatActivity implements View.OnClickListener{
    private LinearLayout btn_restaurant,btn_atm,btn_mall,btn_hospital,btn_school,btn_park,btn_cinema,btn_cafe;
    String typeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_near);
        btn_restaurant=findViewById(R.id.restaurant);
        btn_atm=findViewById(R.id.atm);
        btn_mall=findViewById(R.id.shopping_mall);
        btn_hospital=findViewById(R.id.hospital);
        btn_school=findViewById(R.id.school);
        btn_park=findViewById(R.id.park);
        btn_cinema=findViewById(R.id.movie_theater);
        btn_cafe=findViewById(R.id.cafe);

        btn_restaurant.setOnClickListener(this);
        btn_atm.setOnClickListener(this);
        btn_mall.setOnClickListener(this);
        btn_hospital.setOnClickListener(this);
        btn_school.setOnClickListener(this);
        btn_park.setOnClickListener(this);
        btn_cinema.setOnClickListener(this);
        btn_cafe.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.restaurant) {
            typeName="restuarant";
        }
        else if(view.getId()==R.id.atm) {
            typeName="atm";
        }
        else if(view.getId()==R.id.shopping_mall) {
            typeName="shopping_mall";
        }
        else if(view.getId()==R.id.hospital) {
            typeName="hospital";
        }
        else if(view.getId()==R.id.school) {
            typeName="school";
        }
        else if(view.getId()==R.id.park) {
            typeName="park";
        }
        else if(view.getId()==R.id.movie_theater) {
            typeName="movie_theater";
        }
        else if(view.getId()==R.id.cafe) {
            typeName="cafe";
        }
        Intent i = new Intent(StartNearActivity.this, NearActivity.class);
        i.putExtra("type", typeName);
        startActivity(i);
    }
}
