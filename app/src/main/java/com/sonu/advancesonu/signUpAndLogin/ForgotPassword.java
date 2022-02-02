package com.sonu.advancesonu.signUpAndLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.sonu.advancesonu.R;
import com.sonu.advancesonu.customDialog.MessageDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText edtEmail;
    private Button btnReset;
    private TextView backToLogin;
    private FirebaseAuth mAuth;
    private boolean check;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUi();
        mAuth = FirebaseAuth.getInstance();
    }

    private void initUi()
    {
        edtEmail = findViewById(R.id.f_email);
        backToLogin = findViewById(R.id.f_back_to_login);
        btnReset =findViewById(R.id.btn_reset_password);
        btnReset.setOnClickListener(this);
        backToLogin.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading..");
        progressDialog.setCanceledOnTouchOutside(false);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_reset_password: progressDialog.show();
                resetPassword();
                break;
            case R.id.f_back_to_login: sendBackToLogin();
                break;
        }
    }

    private void sendBackToLogin() {
        Intent intent = new Intent(ForgotPassword.this,Login.class);
        startActivity(intent);
        finish();
    }

    private void resetPassword() {

        String email = edtEmail.getText().toString();
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(ForgotPassword.this,"email not found", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        else{


          if(checkEmail(email))
          {

              mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                  @Override
                  public void onComplete(@NonNull Task<Void> task) {
                      if(task.isSuccessful())
                      {
                         // Toast.makeText(ForgotPassword.this,"password reset link has been sent to your link",Toast.LENGTH_SHORT).show();

                          MessageDialog dialog = new MessageDialog("password reset link has been sent to your email.");
                          dialog.setStyle(DialogFragment.STYLE_NO_FRAME,R.style.dialog);
                          dialog.show(getSupportFragmentManager(),"mail dialog");
                          progressDialog.dismiss();

                      }
                      else{
                            Toast.makeText(ForgotPassword.this,task.getException()+"", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                      }
                  }
              });
          }
          else{
              Toast.makeText(ForgotPassword.this,"email not found", Toast.LENGTH_SHORT).show();
              progressDialog.dismiss();
          }
        }

    }

    public boolean checkEmail(String email)
    {


        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                check = task.isSuccessful();

            }
        });

         return check;
    }
}
