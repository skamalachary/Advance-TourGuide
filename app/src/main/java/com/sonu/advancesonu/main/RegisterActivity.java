package com.sonu.advancesonu.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sonu.advancesonu.R;
import com.sonu.advancesonu.signUpAndLogin.Login;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

        Button Register;
    int pos1;
    RadioGroup gen;
    RadioButton m,f;
        EditText visitor_name,age_r,gender_r,address_r,mobile_r,email_r,pwd;
        String gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        gen = (RadioGroup) findViewById(R.id.gend);
        m = (RadioButton) findViewById(R.id.male1);
        f = (RadioButton) findViewById(R.id.female1);
        Register = (Button) findViewById(R.id.register);
        visitor_name = (EditText) findViewById(R.id.name);
        age_r = (EditText) findViewById(R.id.age);
        address_r = (EditText) findViewById(R.id.add);
        mobile_r = (EditText) findViewById(R.id.mob_no);
        email_r = (EditText) findViewById(R.id.email_reg);
        pwd = (EditText) findViewById(R.id.password_reg);
        Register.setOnClickListener(this);
        gender = "";

        gen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

                pos1=gen.indexOfChild(findViewById(gen.getCheckedRadioButtonId()));

                switch (pos1) {
                    case 0:
                        gender = "Male";
                        Toast.makeText(getApplicationContext(), gender, Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        gender = "Female";
                        Toast.makeText(getApplicationContext(), gender, Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }



    private void register() {
        final String username = visitor_name.getText().toString().trim();
        final String password = pwd.getText().toString().trim();
        final String email = email_r.getText().toString().trim();
        final String age = age_r.getText().toString().trim();
        final String address = address_r.getText().toString().trim();
        final String mobile = mobile_r.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UserConfig.URL_REGISTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(RegisterActivity.this,response, Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegisterActivity.this,error.toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put(UserConfig.KEY_USERNAME,username);
                params.put(UserConfig.KEY_AGE,age);
                params.put(UserConfig.KEY_ADDRESS,address);
                params.put(UserConfig.KEY_MOBILE,mobile);
                params.put(UserConfig.KEY_GENDER,gender);
                params.put(UserConfig.KEY_PASSWORD,password);
                params.put(UserConfig.KEY_EMAIL, email);
                return params;
            }

        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
    @Override
    public void onClick(View view) {
        register();
        Intent intent = new Intent(RegisterActivity.this, Login.class);
        startActivity(intent);
    }
}
