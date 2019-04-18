package com.example.inclass09_group19;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import static android.media.MediaRecorder.VideoSource.CAMERA;


public class FourthFragment extends Fragment {
       private OnFragmentInteractionListener mListener;
    ImageView iv;
    Context cont;
    EditText txtContactName, txtContactEmail, txtContactPhone;

    public FourthFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fourth, container, false);

        cont = container.getContext();


        txtContactName = (EditText) view.findViewById(R.id.txtContactName);
        txtContactEmail = (EditText) view.findViewById(R.id.txtContactEmail);
        txtContactPhone = (EditText) view.findViewById(R.id.txtContactPhone);
        iv = (ImageView) view.findViewById(R.id.ivOpenCamera);
        txtContactPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        view.findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                ContactDetails contactDetails = new ContactDetails();
                if(txtContactName.getText().toString().isEmpty())
                {
                    txtContactName.setError("Required");
                    return;
                }
                else
                {
                    contactDetails.Name = txtContactName.getText().toString();
                }

                if(txtContactEmail.getText().toString().isEmpty())
                {
                    txtContactEmail.setError("Required");
                    return;
                }
                else
                {
                    contactDetails.EmailID = txtContactEmail.getText().toString();
                }

                if(txtContactPhone.getText().toString().isEmpty())
                {
                    txtContactPhone.setError("Required");
                    return;
                }
                else
                {
                    contactDetails.ContactNo = txtContactPhone.getText().toString();
                }

                if(! ((iv.getDrawable()) instanceof VectorDrawable)){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    ((BitmapDrawable) iv.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] byteFormat = stream.toByteArray();
                    contactDetails.imgURL = Base64.encodeToString(byteFormat, Base64.NO_WRAP);
                }
                else
                {
                    contactDetails.imgURL = "";
                }


                mListener.SaveContact(contactDetails);
            }
        });

        view.findViewById(R.id.ivOpenCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        final ImageView iv = (ImageView) getActivity().findViewById(R.id.ivOpenCamera);
        if (requestCode == 1) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            iv.setImageBitmap(thumbnail);
            Toast.makeText(cont, "Image Saved!", Toast.LENGTH_SHORT).show();

        }
        else if(resultCode == Activity.RESULT_CANCELED)
        {
            return;
        }
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
        void SaveContact(ContactDetails contactDetails);
    }
}
