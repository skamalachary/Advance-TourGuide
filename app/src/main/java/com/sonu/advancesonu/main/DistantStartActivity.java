package com.sonu.advancesonu.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.sonu.advancesonu.R;

public class DistantStartActivity extends AppCompatActivity {

    EditText gplace;
    Spinner searchType;
    Button searchBtn;
    String[] searchTypeName= null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_distant_start);
        gplace= (EditText) findViewById(R.id.txtplace);
        searchType=(Spinner) findViewById(R.id.spr_search_type);
        searchBtn= (Button) findViewById(R.id.btn_search);
        searchTypeName=getResources().getStringArray(R.array.search_type_name);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, searchTypeName);
        searchType.setAdapter(adapter);
        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                int selectedPosition = searchType.getSelectedItemPosition();
                String type = searchTypeName[selectedPosition];

                if(type.equals("See Top Recomendations"))
                {
                    Intent i=new Intent(DistantStartActivity.this,XtestActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("userplace",gplace.getText().toString());
                    i.putExtras(bundle);
                    startActivity(i);
                }
                else
                {
                    Intent i=new Intent(DistantStartActivity.this, VenueExploreActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("userplace",gplace.getText().toString());
                    i.putExtras(bundle);
                    startActivity(i);                }
            }
        });
    }
}