package com.example.inclass09_group19;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FirstFragment.OnFragmentInteractionListener, SecondFragment.OnFragmentInteractionListener
        , ThirdFragment.OnFragmentInteractionListener, FourthFragment.OnFragmentInteractionListener {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;
    DatabaseReference contactListRef;
    ArrayList<String> keyList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();
        //   mAuth = FirebaseAuth.getInstance();
//                currentUser = mAuth.getCurrentUser();
//                uid = currentUser.getUid();
//                contactListRef = FirebaseDatabase
//                        .getInstance()
//                        .getReference("ContactList")
//                        .child(uid);

        if (mAuth.getCurrentUser() != null) {
            currentUser = mAuth.getCurrentUser();
            uid = currentUser.getUid();
            contactListRef = FirebaseDatabase
                    .getInstance()
                    .getReference("ContactList")
                    .child(uid);

            contactListRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<ContactDetails> arrayList = new ArrayList<>();
                    keyList = new ArrayList<>();
                    for (DataSnapshot contactSnap : dataSnapshot.getChildren()) {
                        ContactDetails contactDetails = contactSnap.getValue(ContactDetails.class);
                        arrayList.add(contactDetails);
                        keyList.add(contactSnap.getKey());
                    }

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.Container, new ThirdFragment(arrayList, contactListRef, keyList), "third")
                            .commit();

                    setTitle("Contacts");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.Container, new FirstFragment(), "first")
                    .commit();
            setTitle("Login");
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public void openSignUpForm() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new SecondFragment(), "second")
                .commit();

        setTitle("Sign Up");
    }

    @Override
    public void Login(String email, String password) {


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            uid = currentUser.getUid();
                            contactListRef = FirebaseDatabase
                                    .getInstance()
                                    .getReference("ContactList")
                                    .child(uid);
                            contactListRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<ContactDetails> arrayList = new ArrayList<>();
                                    keyList = new ArrayList<>();
                                    for (DataSnapshot contactSnap : dataSnapshot.getChildren()) {
                                        ContactDetails contactDetails = contactSnap.getValue(ContactDetails.class);
                                        arrayList.add(contactDetails);
                                        keyList.add(contactSnap.getKey());
                                    }

                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.Container, new ThirdFragment(arrayList, contactListRef, keyList), "third")
                                            .commit();

                                    setTitle("Contacts");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        } else {

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void openLoginScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new FirstFragment(), "first")
                .commit();

        setTitle("Login");
    }

    @Override
    public void SignUP(String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            currentUser = mAuth.getCurrentUser();
                            uid = currentUser.getUid();
                            contactListRef = FirebaseDatabase
                                    .getInstance()
                                    .getReference("ContactList")
                                    .child(uid);

                            contactListRef.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    ArrayList<ContactDetails> arrayList = new ArrayList<>();
                                    keyList = new ArrayList<>();
                                    for (DataSnapshot contactSnap : dataSnapshot.getChildren()) {
                                        ContactDetails contactDetails = contactSnap.getValue(ContactDetails.class);
                                        arrayList.add(contactDetails);
                                        keyList.add(contactSnap.getKey());
                                    }

                                    getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.Container, new ThirdFragment(arrayList, contactListRef, keyList), "third")
                                            .commit();

                                    setTitle("Contacts");
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void CreateContactFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new FourthFragment(), "fourth")
                .commit();

        setTitle("Create New Contact");
    }

    @Override
    public void SignOut() {

        mAuth.signOut();
        mAuth = FirebaseAuth.getInstance();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new FirstFragment(), "first")
                .commit();

        setTitle("Login");
    }

    @Override
    public void SaveContact(final ContactDetails contactDetails) {
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        contactListRef = FirebaseDatabase
                .getInstance()
                .getReference("ContactList")
                .child(uid);
        contactListRef.push().setValue(contactDetails);

        contactListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<ContactDetails> arrayList = new ArrayList<>();
                for (DataSnapshot contactSnap : dataSnapshot.getChildren()) {
                    ContactDetails contactDetails1 = contactSnap.getValue(ContactDetails.class);
                    keyList.add(contactSnap.getKey());
                    arrayList.add(contactDetails1);
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Container, new ThirdFragment(arrayList, contactListRef, keyList), "third")
                        .commit();

                setTitle("Contacts");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
