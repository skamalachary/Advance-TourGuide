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

public class Login extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail;
    private EditText edtPass;
    private TextView createAccount;
    private TextView forgotPass;
    private Button btnLogIn;
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUi();
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified())
        {sendForPhoneVerification();}
    }

    private void initUi()
    {
        edtEmail = findViewById(R.id.email_input);
        edtPass = findViewById(R.id.pass_input);
        createAccount = findViewById(R.id.create_new_account);
        forgotPass = findViewById(R.id.forgot_pass);
        btnLogIn =findViewById(R.id.btn_login);
        btnLogIn.setOnClickListener(this);
        forgotPass.setOnClickListener(this);
        createAccount.setOnClickListener(this);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_login:  progressDialog.show();
                logIn();
                break;
            case R.id.create_new_account: sendToCreateAccount();
                break;
            case R.id.forgot_pass:  sendToResetPassword();
                break;
        }
    }

    private void sendToResetPassword() {
        Intent intent = new Intent(Login.this,ForgotPassword.class);
        startActivity(intent);
        finish();

    }

    private void sendToCreateAccount() {
        Intent intent = new Intent(Login.this,SignUp.class);
        startActivity(intent);
        finish();
    }

    private void logIn() {
        if(!emptyField()) {

            String email = edtEmail.getText().toString();
            String pass = edtPass.getText().toString();
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        if(mAuth.getCurrentUser().isEmailVerified())
                        {
                            setUserForNotification();
                            sendForPhoneVerification();
                            progressDialog.dismiss();
                        }
                        else {

                            Toast.makeText(Login.this,"Please verify your email.", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();
                        }
                        } else {
                        Toast.makeText(Login.this, task.getException()+"", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
        else{
            Toast.makeText(Login.this,"no fields can be left empty", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }

    }
    private void sendForPhoneVerification()
    {
        Intent intent = new Intent(Login.this,PhoneVerification.class);
        startActivity(intent);
        finish();

    }
    private boolean emptyField()
    {
        return edtEmail.getText().toString().equals("") || edtPass.getText().toString().equals("");
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

                        Toast.makeText(Login.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
