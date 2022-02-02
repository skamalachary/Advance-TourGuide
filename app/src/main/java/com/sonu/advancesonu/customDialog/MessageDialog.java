package com.sonu.advancesonu.customDialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;


import com.sonu.advancesonu.R;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageDialog extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView titleBox;
    private TextView messageBox;
    private Button btnOk;
    private String title;
    private String message;

    public MessageDialog(String message) {

        this.message = message;
    }

    public MessageDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageDialog.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageDialog newInstance(String param1, String param2) {
        MessageDialog fragment = new MessageDialog();
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

        View view = inflater.inflate(R.layout.fragment_message_dialog, container, false);
        titleBox = view.findViewById(R.id.title);
        messageBox = view.findViewById(R.id.message);
        btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        messageBox.setText(message);

        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(true);
        return view;
    }


}
