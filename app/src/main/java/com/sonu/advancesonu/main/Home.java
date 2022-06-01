package com.sonu.advancesonu.main;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.sonu.advancesonu.R;
import com.sonu.advancesonu.signUpAndLogin.Login;
import com.sonu.advancesonu.user.EditProfile;
import com.sonu.advancesonu.user.UserInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.messaging.FirebaseMessaging;

import com.squareup.picasso.Picasso;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;



public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    int PRIVATE_MODE = 0;

    private static final String PREF_NAME = "LoginPreference";

    private NavigationView navigationView;
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    //  private ImageView profileImage;
    public static UserInfo userInfo;
    private TextView navName;
    private LinearLayout editProfileImage;
    private CircleImageView profileImage;

    private String generatedOrderId;

    private SharedPreferences pref;

    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String feedback;
    private Button btnMap,btnNext;

    @Override
    protected void onPostResume() {
        super.onPostResume();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_home_main);
        pref =  getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        String pPrice = pref.getString("p_price","499");
        String descrip = pref.getString("p_des","Channels : entertainment, sports , news , music");
        String day = pref.getString("l_day","10");
        init();



        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.nav_app_bar_open_drawer_description, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,HomeActivity.class));
            }
        });


    }

    private void init() {


        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_main);
        btnMap = findViewById(R.id.mapButton);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Home");
        // loadFragment(new Main(),"Home");
        View headerView = navigationView.getHeaderView(0);
        profileImage = headerView.findViewById(R.id.nav_profile_image);
        editProfileImage = headerView.findViewById(R.id.nav_edit_profile);
        navName = headerView.findViewById(R.id.nav_profile_name);

        editProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userInfo.getUserName() != null) {
                    Intent intent = new Intent(Home.this, EditProfile.class);
                    startActivity(intent);
                    mDrawer.closeDrawer(navigationView);
                }
            }
        });

        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this,consoleapi.class));
            }
        });



        userInfo = new UserInfo();
        getUserInfo();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {

            case R.id.nav_settings:
             //   showDialog();
                mDrawer.closeDrawer(GravityCompat.START);
              /*  loadFragment(new Settings(),"Settings");
                mDrawer.closeDrawer(navigationView);
                menuItem.setChecked(true);*/
                return true;
            case R.id.nav_about_us:

               // startActivity(new Intent(Home.this, ContactUs.class));
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_logout:
                FirebaseMessaging.getInstance().unsubscribeFromTopic("all");
                FirebaseAuth.getInstance().signOut();
                clearSharedPreference();
                startActivity(new Intent(Home.this, Login.class));
                finish();

        }
        return false;
    }


    public void getUserInfo() {
        FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userInfo.setUserName(dataSnapshot.child("userName").getValue().toString());
                        userInfo.setUserImage(dataSnapshot.child("image").getValue().toString());
                        userInfo.setUserEmail(dataSnapshot.child("email").getValue().toString());
                        userInfo.setUserMob(dataSnapshot.child("mob").getValue().toString());
                        if (userInfo.getUserImage().equals("na")) {
                        } else {
                            Picasso.get().load(userInfo.getUserImage()).into(profileImage);
                            //Toast.makeText(getApplicationContext(),userInfo.getUserImage(),Toast.LENGTH_SHORT).show();
                        }

                        navName.setText(userInfo.getUserName());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }


    public void clearSharedPreference() {
        SharedPreferences preferences = getSharedPreferences("mob", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();


    }











    private void sendFeedback(String feedback) {


        if(feedback.equals("")) {
            Toast.makeText(Home.this,"Please enter the feedback",Toast.LENGTH_LONG).show();

            return;
        }


        final HashMap<String, String> userData = new HashMap<>();
        userData.put("email", Home.userInfo.getUserEmail());
        userData.put("userName", Home.userInfo.getUserName());
        userData.put("date",getCurrentDate());
        userData.put("mobile",Home.userInfo.getUserMob());
        userData.put("query",feedback);



        database.getReference().child("userFeedback").child(mAuth.getCurrentUser().getUid()).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(Home.this,"feedback submitted",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Home.this,task.getException()+"", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    public String getCurrentDate() {

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();

        return df.format(date);
    }

}
