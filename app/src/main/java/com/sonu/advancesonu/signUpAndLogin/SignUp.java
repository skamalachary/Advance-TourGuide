package com.sonu.advancesonu.signUpAndLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sonu.advancesonu.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.messaging.FirebaseMessaging;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class SignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText edtUsername;
    private EditText edtPass;
    private EditText edtEmail;
    private Button btCreateAccount;
    private TextView backToLogin;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initUi();
    }

    private void initUi()
    {
        edtUsername =findViewById(R.id.s_username);
        edtEmail = findViewById(R.id.s_email);
        edtPass = findViewById(R.id.s_pass);
        btCreateAccount = findViewById(R.id.btn_create_account);
        backToLogin = findViewById(R.id.back_to_login);

        btCreateAccount.setOnClickListener(this);
        backToLogin.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        if(mAuth.getCurrentUser() != null)
        {
            startActivity(new Intent(SignUp.this,Login.class));
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating account...");
        progressDialog.setCanceledOnTouchOutside(false);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_create_account: progressDialog.show();
                createNewAccount();

                break;
            case R.id.back_to_login:  sendBackToLogIn();
                break;
        }

    }

    private void sendBackToLogIn() {
        Intent intent =new Intent(SignUp.this,Login.class);
        startActivity(intent);
        finish();
    }

    private void storeUSerData(HashMap userData)
    {
        database.getReference().child("users").child(mAuth.getCurrentUser().getUid()).setValue(userData).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                  //  setUserForNotification();
                    sendForMailVerification();
                    progressDialog.dismiss();
                }
                else
                {
                     Toast.makeText(SignUp.this,task.getException()+"", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                }

            }
        });
    }
    private void createNewAccount() {

        if(! emptyField()) {

            String email = edtEmail.getText().toString();
            String password = edtPass.getText().toString();
            String userName = edtUsername.getText().toString();
            final HashMap<String, String> userData = new HashMap<>();
            userData.put("email", email);
            userData.put("userName", userName);
            userData.put("date",getCurrentDate());
            userData.put("image","na");


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        storeUSerData(userData);
                        Toast.makeText(SignUp.this, "account created", Toast.LENGTH_LONG).show();


                    } else {
                        Toast.makeText(SignUp.this, task.getException()+"", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else{
            Toast.makeText(SignUp.this,"no field should be empty", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }

    private void sendForMailVerification()
    {
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    final PrettyDialog dialog = new PrettyDialog(SignUp.this);
                    dialog.setTitle("Done").setMessage("registration link han been sent to your mail(Promotion section). Please click on that link to activate your account")
                            .setIcon(R.drawable.ic_launcher_foreground)
                            .addButton("Ok", R.color.pdlg_color_white, R.color.pdlg_color_red, new PrettyDialogCallback() {
                                @Override
                                public void onClick() {
                                    dialog.dismiss();
                                }
                            }).show();

                }
                else
                {
                    Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private boolean emptyField()
    {
        return edtEmail.getText().toString().equals("") || edtPass.getText().toString().equals("")||edtUsername.getText().toString().equals("");
    }
    public String getCurrentDate() {

        DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Date date = new Date();

        return df.format(date);
    }
    public String todayPlus7()
    {
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 7);
        Date toDate = cal.getTime();
        //  String fromDate = df.format(toDate);
        return df.format(toDate);
    }

    public void getDateDifference(Date d1, Date d2)
    {
        //  Date d2 = new Date();
        //  Date d1 = new Date(604800000l);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        //  Date d3 = df.parse("08/06/20 20:03:15");
        long diff = d2.getTime() - d1.getTime();
        long diffSeconds = diff / 1000 % 60;
        long diffMinutes = diff / (60 * 1000) % 60;
        int diffInDays = (int) diff / (1000 * 60 * 60 * 24);
        long diffHours = diff / (60 * 60 * 1000);

    }

    public void setUserForNotification()
    {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {

                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult();

                        FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser()
                                .getUid()).child("token").setValue(token);
                    }
                });


        FirebaseMessaging.getInstance().subscribeToTopic("all")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "welcome";
                        if (!task.isSuccessful()) {
                            msg = "topic error";
                        }

                        Toast.makeText(SignUp.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
