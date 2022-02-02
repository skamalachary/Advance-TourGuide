package com.sonu.advancesonu.customDialog;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.sonu.advancesonu.R;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link permissionDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class permissionDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button btnCancel;
    private Button btnOk;
    private Context context;
    private permissionSettingInterface psObject;




    public permissionDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment permissionDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static permissionDialog newInstance(String param1, String param2) {
        permissionDialog fragment = new permissionDialog();
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
        View view= inflater.inflate(R.layout.permission_dialog, container, false);
        btnCancel = view.findViewById(R.id.ps_btn_cancel);
        btnOk = view.findViewById(R.id.ps_btn_ok);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                psObject.permissionSettingCancelButton();
                dismiss();

            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS)
                        .setData(Uri.parse("package:" +context.getPackageName()))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                dismiss();




            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context=context;
        psObject=(permissionSettingInterface) context;
    }
    public interface permissionSettingInterface{
        void permissionSettingCancelButton();
    }
}
