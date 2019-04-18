package com.example.inclass09_group19;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class ThirdFragment extends Fragment {
     private OnFragmentInteractionListener mListener;
    ArrayList<ContactDetails> arrContactList = new ArrayList<>();
    DatabaseReference myRef;
    ArrayList<String> keyList = new ArrayList<>();

    Lvadapter lvadapter;
    Context cont;
    @SuppressLint("ValidFragment")
    public ThirdFragment(ArrayList<ContactDetails> arrExpenseDetails, DatabaseReference ref, ArrayList<String> keyList) {
        this.arrContactList = arrExpenseDetails;
        this.myRef = ref;
        this.keyList = keyList;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        cont = container.getContext();
        View view = inflater.inflate(R.layout.fragment_third, container, false);

        view.findViewById(R.id.btnCreate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.CreateContactFragment();
            }
        });

        view.findViewById(R.id.btnLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.SignOut();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ListView listView = (ListView) getActivity().findViewById(R.id.contactList);

        lvadapter = new Lvadapter(cont, R.layout.contact_list, arrContactList, ThirdFragment.this);

        listView.setAdapter(lvadapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                myRef.child(keyList.get(position)).removeValue();
                arrContactList.remove(position);
                listView.setAdapter(lvadapter);
                Toast.makeText(cont, "Contact deleted.", Toast.LENGTH_SHORT).show();
                return true;
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
        void CreateContactFragment();
        void SignOut();
    }
}
