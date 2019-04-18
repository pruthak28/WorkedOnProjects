package com.example.inclass09_group19;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


public class SecondFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    EditText txtEmail, txtPwd, txtFirstName, txtLastName, txtConfirmPwd;
    public SecondFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_second, container, false);

        view.findViewById(R.id.btnSignUpMain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!validateForm(view)) return;
                else
                {

                    mListener.SignUP(txtEmail.getText().toString(), txtPwd.getText().toString());
                }

            }
        });

        view.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openLoginScreen();
            }
        });

        return view;
    }

    private boolean validateForm(View v) {
        boolean valid = true;

        txtFirstName = (EditText) v.findViewById(R.id.txtFirstName);
        txtLastName = (EditText) v.findViewById(R.id.txtLastName);
        txtEmail = (EditText) v.findViewById(R.id.txtEmail);
        txtPwd = (EditText) v.findViewById(R.id.txtPwd);
        txtConfirmPwd = (EditText) v.findViewById(R.id.txtConfirmPwd);

        if (txtFirstName.getText().toString().isEmpty())
        {
            txtFirstName.setError("Required");
            return false;
        }


        if (txtLastName.getText().toString().isEmpty())
        {
            txtLastName.setError("Required");
            return false;
        }

        if (txtEmail.getText().toString().isEmpty())
        {
            txtEmail.setError("Required");
            return false;
        }

        if (txtPwd.getText().toString().isEmpty())
        {
            txtPwd.setError("Required");
            return false;
        }

        if (txtConfirmPwd.getText().toString().isEmpty())
        {
            txtConfirmPwd.setError("Required");
            return false;
        }

        if(! txtPwd.getText().toString().equals(txtConfirmPwd.getText().toString()))
        {
            txtConfirmPwd.setError("Passwords don't match.");
            return false;
        }

        return valid;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

            mListener = (OnFragmentInteractionListener) context;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void openLoginScreen();
        void SignUP(String EmailID, String Password);
    }
}
