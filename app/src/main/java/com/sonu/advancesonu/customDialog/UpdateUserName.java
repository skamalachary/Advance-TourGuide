package com.sonu.advancesonu.customDialog;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.sonu.advancesonu.R;
import com.sonu.advancesonu.main.Home;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UpdateUserName#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UpdateUserName extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText newName;
    private Button btnUpdate;
    private Button btnCancel;
    private UpdateNameListener updateNameListener;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UpdateUserName() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment UpdateUserName.
     */
    // TODO: Rename and change types and number of parameters
    public static UpdateUserName newInstance(String param1, String param2) {
        UpdateUserName fragment = new UpdateUserName();
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
        View view = inflater.inflate(R.layout.fragment_update_user_name, container, false);
        inItUi(view);
        return view;
    }

    private void inItUi(View view) {

        newName = view.findViewById(R.id.name_input_field);
        btnCancel = view.findViewById(R.id.name_btn_cancel);
        btnUpdate = view.findViewById(R.id.name_btn_update);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = newName.getText().toString();
                if(TextUtils.isEmpty(name))
                {
                    Toast.makeText(getContext(),"Please enter a name", Toast.LENGTH_SHORT).show();
                }
                else{
                    FirebaseDatabase.getInstance().getReference().child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("userName").setValue(name);
                    updateNameListener.updateName(name);
                    Home.userInfo.setUserName(name);
                    dismiss();
                }

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        updateNameListener = (UpdateNameListener) context;

    }

    public interface UpdateNameListener{
        void updateName(String name);
    }
}
