package com.example.group19_ic10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class FirstFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    EditText txtEmail, txtPwd;

    @SuppressLint("ValidFragment")
    public FirstFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_first, container, false);


        view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm(view)) return;
                else {

                    mListener.Login(txtEmail.getText().toString(), txtPwd.getText().toString());
                }


            }
        });

        view.findViewById(R.id.btnSignUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.openSignUpForm();
            }
        });

        return view;
    }


    private boolean validateForm(View v) {
        boolean valid = true;


        txtEmail = (EditText) v.findViewById(R.id.txtEmailLogin);
        txtPwd = (EditText) v.findViewById(R.id.txtPwdLogin);


        if (txtEmail.getText().toString().isEmpty()) {
            txtEmail.setError("Required");
            return false;
        }

        if (txtPwd.getText().toString().isEmpty()) {
            txtPwd.setError("Required");
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
        void openSignUpForm();

        void Login(String Email, String Pwd);
    }
}
