package com.sonu.advancesonu.signUpAndLogin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sonu.advancesonu.R;
import com.sonu.advancesonu.main.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class PhoneVerification extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private EditText edtPhoneNumber;
    private Button sendOtp;
    private TextView textView2;
    private String mVerificationId;
    private SharedPreferences sets;
    private SharedPreferences.Editor editor;
    private LinearLayout numberContainer;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        SharedPreferences pref;
        pref = PreferenceManager.getDefaultSharedPreferences(PhoneVerification.this);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("p_price","499");
        editor.putString("p_des","Channels : entertainment, sports , news , music");
        editor.apply();

        init();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_verification_code:
                //initiatePhoneNumberVerification();
                if(edtPhoneNumber.getText().toString().length()==10)
                {
                    FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("mob").setValue(edtPhoneNumber.getText().toString());
                    editor.putBoolean("mob",true).commit();
                    Intent intent = new Intent(PhoneVerification.this, Home.class);
                    startActivity(intent);
                    finish();
                    progressDialog.dismiss();
                }

                else{
                    Toast.makeText(PhoneVerification.this,"invalid phone number", Toast.LENGTH_SHORT).show();

                }

                break;
        }
    }

    private void init() {

        sendOtp = findViewById(R.id.send_verification_code);
        edtPhoneNumber = findViewById(R.id.phone_number);
        mAuth = FirebaseAuth.getInstance();
        numberContainer= findViewById(R.id.phone_text);
        sendOtp.setOnClickListener(this);
        sets = getSharedPreferences("mob",MODE_PRIVATE);
        editor = sets.edit();
        checkMob();
        progressDialog =new ProgressDialog(this);
        progressDialog.setMessage("Verifying..");
        progressDialog.setCanceledOnTouchOutside(false);


    }

    public void initiatePhoneNumberVerification() {
        if (edtPhoneNumber.getText().toString().equals("")) {
            Toast.makeText(PhoneVerification.this, "please enter a number", Toast.LENGTH_SHORT).show();
        } else {
            if (edtPhoneNumber.getText().toString().length() == 10) {
                String phoneNumber = "+91"+edtPhoneNumber.getText().toString();
                verifyPhoneNumber(phoneNumber);
            } else {
                Toast.makeText(PhoneVerification.this, "invalid phone number", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void verifyPhoneNumber(final String phoneNumber) {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {


                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("mob").setValue(phoneNumber);
                Toast.makeText(PhoneVerification.this,"phone verified", Toast.LENGTH_SHORT).show();
                editor.putBoolean("mob",true).commit();
                Intent intent = new Intent(PhoneVerification.this, Home.class);
                startActivity(intent);
                finish();
                progressDialog.dismiss();

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(PhoneVerification.this, "error" +e.getMessage(), Toast.LENGTH_SHORT).show();


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(PhoneVerification.this,e.getMessage()+"", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(PhoneVerification.this,"sms quota exceeded", Toast.LENGTH_SHORT).show();
                }

                progressDialog.dismiss();


            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later
               mVerificationId = verificationId;
               PhoneAuthProvider.ForceResendingToken mResendToken = token;
               Toast.makeText(PhoneVerification.this,"code sent", Toast.LENGTH_SHORT).show();
               numberContainer.setVisibility(View.GONE);
               sendOtp.setVisibility(View.GONE);




                // ...
            }

        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }
    public void verifyManually()
    {



    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        Toast.makeText(PhoneVerification.this,"2", Toast.LENGTH_SHORT).show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(PhoneVerification.this,"manual verification", Toast.LENGTH_SHORT).show();

                            FirebaseUser user = task.getResult().getUser();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(PhoneVerification.this,"error", Toast.LENGTH_SHORT).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }

    public void checkMob()
    {
        if(sets.getBoolean("mob",false))
        {
            Intent intent = new Intent(PhoneVerification.this, Home.class);
            startActivity(intent);
            finish();
        }

    }
}
