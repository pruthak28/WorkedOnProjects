package com.example.group19_ic10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("ValidFragment")
public class AddNotesFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    String token;
    Context cont;
    public ArrayList<Note> NotesList = null;

    @SuppressLint("ValidFragment")
    public AddNotesFragment(String token) {
        // Required empty public constructor
        this.token = token;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cont = container.getContext();
        return inflater.inflate(R.layout.add_notes, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Thread g = new GETRequest();
        MainActivity.httpUrl = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/getall";

//        String token = MainActivity.response.get("token").toString();

        MainActivity.request.put("x-access-token", token);

        g.start();

        while (true) {
            if (MainActivity.request.size() == 0)
                break;
            try {
                Thread.sleep(400);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String name = MainActivity.response.get("name").toString();

        TextView userMsg = getActivity().findViewById(R.id.txtName);
        userMsg.setText("Hey " + name + "!!!");

        try {
            JSONObject j = new JSONObject(MainActivity.response.get("json").toString());
            JSONArray jsonArray = j.getJSONArray("notes");
            NotesList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {


                JSONObject object = null;
                try {
                    object = jsonArray.getJSONObject(i);
                    String id = object.getString("_id");
                    String note = object.getString("userId");
                    String text = object.getString("text");
                    Note n = new Note(id, note, text);
                    NotesList.add(n);
                    System.out.println(note + "," + text + "," + id);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            RecyclerView rcv = getActivity().findViewById(R.id.recyclerView);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(cont);
            rcv.setLayoutManager(layoutManager);

            NotesAdapter notesAdapter = new NotesAdapter(NotesList, cont);
            rcv.setAdapter(notesAdapter);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        getActivity().findViewById(R.id.btnAddNotes).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.OpenAddNotesFrag();
            }
        });

        getActivity().findViewById(R.id.ivSignOut).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SignOut();
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
        void OpenAddNotesFrag();

        void SignOut();
    }
}
