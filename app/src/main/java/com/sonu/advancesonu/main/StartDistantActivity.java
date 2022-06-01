package com.sonu.advancesonu.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.sonu.advancesonu.R;

public class StartDistantActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_restaurant,btn_mall,btn_hospital,btn_school,btn_park,btn_cinema,btn_top;
    String typeName;
    EditText gplace;
    Spinner searchType;
    Button searchBtn;
    String[] searchTypeName= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_distant);
        btn_restaurant=(Button)findViewById(R.id.restaurant);
        btn_top=(Button)findViewById(R.id.top);
        btn_mall=(Button)findViewById(R.id.shopping_mall);
        btn_hospital=(Button)findViewById(R.id.hospital);
        btn_school=(Button)findViewById(R.id.school);
        btn_park=(Button)findViewById(R.id.park);
        btn_cinema=(Button)findViewById(R.id.movie_theater);

        btn_restaurant.setOnClickListener(this);
        btn_top.setOnClickListener(this);
        btn_mall.setOnClickListener(this);
        btn_hospital.setOnClickListener(this);
        btn_school.setOnClickListener(this);
        btn_park.setOnClickListener(this);
        btn_cinema.setOnClickListener(this);
        gplace= (EditText) findViewById(R.id.txtplace);

    }

    @Override
    public void onClick(View view) {

        if(view.getId()==R.id.restaurant) {
            typeName="Restuarant";
            Intent i = new Intent(StartDistantActivity.this, VenueExploreActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("userplace",gplace.getText().toString());
            bundle.putString("type",typeName);
            i.putExtras(bundle);
            startActivity(i);
        }
        else if(view.getId()==R.id.park) {
            typeName="Park";
            Intent i = new Intent(StartDistantActivity.this, VenueExploreActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("userplace",gplace.getText().toString());
            bundle.putString("type",typeName);
            i.putExtras(bundle);
            startActivity(i);
        }
        else if(view.getId()==R.id.shopping_mall) {
            typeName="Shopping Mall";
            Intent i = new Intent(StartDistantActivity.this, VenueExploreActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("userplace",gplace.getText().toString());
            bundle.putString("type",typeName);
            i.putExtras(bundle);
            startActivity(i);
        }
        else if(view.getId()==R.id.hospital) {
            typeName="Hospital";
            Intent i = new Intent(StartDistantActivity.this, VenueExploreActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("userplace",gplace.getText().toString());
            bundle.putString("type",typeName);
            i.putExtras(bundle);
            startActivity(i);
        }
        else if(view.getId()==R.id.school) {
            typeName="School";
            Intent i = new Intent(StartDistantActivity.this, VenueExploreActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("userplace",gplace.getText().toString());
            bundle.putString("type",typeName);
            i.putExtras(bundle);
            startActivity(i);
        }
        else if(view.getId()==R.id.top) {
            Intent i=new Intent(StartDistantActivity.this,XtestActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("userplace",gplace.getText().toString());
            i.putExtras(bundle);
            startActivity(i);
        }
        else if(view.getId()==R.id.movie_theater) {
            typeName="Movie Theater";
            Intent i = new Intent(StartDistantActivity.this, VenueExploreActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("userplace",gplace.getText().toString());
            bundle.putString("type",typeName);
            i.putExtras(bundle);
            startActivity(i);
        }

    }


}
