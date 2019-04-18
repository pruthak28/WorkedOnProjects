package com.example.group19_hw06;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


@SuppressLint("ValidFragment")
public class ChatFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    String userName;Uri contentURI;
    static Bitmap bitmap;
    Context cont;
    MessageAdapter rvadapter;
    ArrayList<Message> arrMsg = new ArrayList<>();
    ArrayList<String> keyList = new ArrayList<>();

    @SuppressLint("ValidFragment")
    public ChatFragment(String name, ArrayList<Message> arrMsg, ArrayList<String> keyList) {
        userName = name;
        this.arrMsg = arrMsg;
        this.keyList = keyList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cont = container.getContext();
        View v = inflater.inflate(R.layout.fragment_chat, container, false);

        TextView txtName = (TextView) v.findViewById(R.id.txtName);

        txtName.setText(userName);
        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final EditText txtMsg = (EditText) getActivity().findViewById(R.id.txtMsgValue);

        final RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);

        rvadapter = new MessageAdapter(arrMsg,getActivity(), keyList, userName);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(cont);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(rvadapter);


        getActivity().findViewById(R.id.ivSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SignOut();
            }
        });

        getActivity().findViewById(R.id.ivImgSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(galleryIntent, 1);


            }
        });

        getActivity().findViewById(R.id.ivMsgSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if(txtMsg.getText().toString().isEmpty())
//                {
//                    Toast.makeText(cont, "Please type a message.", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                mListener.sendMsg(txtMsg.getText().toString(),getBitmapString(bitmap));
                bitmap = null;

            }
        });


        txtMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    txtMsg.setHint("");
                else{
//                    txtMsg.setHint(getActivity().getResources().getString(R.string.txtJintMsg));
                    txtMsg.setHint("Type message to send...");
                }

            }
        });

    }

    public String getBitmapString(Bitmap bmp){
        if(bmp == null) return "";
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteFormat = stream.toByteArray();
        return Base64.encodeToString(byteFormat, Base64.NO_WRAP);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

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
        void SignOut();
        void sendMsg(String Msg, String bmpImg);
    }
}
