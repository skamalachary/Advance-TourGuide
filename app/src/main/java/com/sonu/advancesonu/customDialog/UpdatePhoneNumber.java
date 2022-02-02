package com.sonu.advancesonu.customDialog;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.sonu.advancesonu.R;
import com.sonu.advancesonu.main.Home;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdatePhoneNumber#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdatePhoneNumber extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText edtPhone;
    private Button btnOk;
    private Button btnCancel;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;
    private LinearLayout firstLayout;
    private LinearLayout secondLayout;
    private UpdatePhoneNumberListener updatePhoneNumberListener;

    public UpdatePhoneNumber() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdatePhoneNumber.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdatePhoneNumber newInstance(String param1, String param2) {
        UpdatePhoneNumber fragment = new UpdatePhoneNumber();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_phone_number, container, false);
        inItUi(view);
        return view;
    }
    private void inItUi(View view)
    {
        progressBar=view.findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        edtPhone = view.findViewById(R.id.phone_input_field);
        btnOk =view.findViewById(R.id.phone_btn_update);
        btnCancel = view.findViewById(R.id.phone_btn_cancel);
        firstLayout = view.findViewById(R.id.first_layout);
        secondLayout = view.findViewById(R.id.second_layout);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiatePhoneNumberVerification();



            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        dismiss();
            }
        });
    }

    public void initiatePhoneNumberVerification() {
        if (edtPhone.getText().toString().equals("")) {
            Toast.makeText(getContext(), "please enter a number", Toast.LENGTH_SHORT).show();
        } else {
            if (edtPhone.getText().toString().length() == 10) {
                String phoneNumber = "+91"+edtPhone.getText().toString();
                firstLayout.setVisibility(View.GONE);
                secondLayout.setVisibility(View.VISIBLE);
                verifyPhoneNumber(phoneNumber);
            } else {
                Toast.makeText(getContext(), "invalid phone number", Toast.LENGTH_SHORT).show();

            }
        }

    }

    public void verifyPhoneNumber(final String phoneNumber) {
        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {


                FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("mob").setValue(phoneNumber);
                Toast.makeText(getContext(),"phone verified", Toast.LENGTH_SHORT).show();
                updatePhoneNumberListener.updateNumber(phoneNumber);
                Home.userInfo.setUserMob(phoneNumber);
                dismiss();




            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

                Toast.makeText(getContext(), "error" +e.getMessage(), Toast.LENGTH_SHORT).show();


                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    Toast.makeText(getContext(),e.getMessage()+"", Toast.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    Toast.makeText(getContext(),"sms quota exceeded", Toast.LENGTH_SHORT).show();
                }




            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                // Save verification ID and resending token so we can use them later

                PhoneAuthProvider.ForceResendingToken mResendToken = token;






                // ...
            }

        };
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                getActivity(),               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        updatePhoneNumberListener = (UpdatePhoneNumberListener) context;

    }

    public interface UpdatePhoneNumberListener{
         void updateNumber(String number);
    }
}
