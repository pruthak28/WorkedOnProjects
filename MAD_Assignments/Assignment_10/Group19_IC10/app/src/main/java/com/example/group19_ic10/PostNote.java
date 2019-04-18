package com.example.group19_ic10;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class PostNote extends Fragment {
    private OnFragmentInteractionListener mListener;
    Context cont;
    public PostNote() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cont= container.getContext();
        return inflater.inflate(R.layout.fragment_post_note, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final TextView txtNote = getActivity().findViewById(R.id.Notetxt);

        getActivity().findViewById(R.id.btnPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtNote.getText().toString().isEmpty())
                {
                    Toast.makeText(cont, "Please enter note.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mListener.addNote(txtNote.getText().toString());
                }
            }
        });

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
        void addNote(String noteTxt);
    }
}
