package com.example.group19_hw06;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements FirstFragment.OnFragmentInteractionListener, SecondFragment.OnFragmentInteractionListener
        , ChatFragment.OnFragmentInteractionListener, MessageAdapter.MessageOperations {
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    String uid;
    DatabaseReference MsgListRef;
    ArrayList<String> keyList = new ArrayList<>();

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            currentUser = mAuth.getCurrentUser();
            OpenChatFragment();

        } else {
            OpenLoginFragment();
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
                            OpenChatFragment();

                        } else {

                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    public void openLoginScreen() {
        OpenLoginFragment();
    }

    @Override
    public void SignUP(String email, String password, final String Name) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {


                            currentUser = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(Name)
                                    .build();

                            currentUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("test", "User profile updated.");

                                                OpenChatFragment();
                                            }
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
    public void SignOut() {

        mAuth.signOut();
        mAuth = FirebaseAuth.getInstance();
        OpenLoginFragment();
    }

    @Override
    public void sendMsg(String Msg, String bmpImg) {
        String[] name;
        currentUser = mAuth.getCurrentUser();
        name = currentUser.getDisplayName().split(" ");

        Message message = new Message();
        message.msgText = Msg;
        message.imgUrl = (bmpImg.isEmpty() ? "" : bmpImg);
        message.firstName = name[0];
        message.lastName = name[1];
        message.dt = Calendar.getInstance().getTime();

        uid = currentUser.getUid();
        MsgListRef = FirebaseDatabase
                .getInstance()
                .getReference("Messages")
                .child(uid);


        MsgListRef.push().setValue(message);

        OpenChatFragment();


    }


    @Override
    public void deleteMessage(Message expenseDetails, String key) {
        currentUser = mAuth.getCurrentUser();
        uid = currentUser.getUid();
        MsgListRef = FirebaseDatabase
                .getInstance()
                .getReference("Messages")
                .child(uid);

        MsgListRef.child(key).removeValue();

        OpenChatFragment();
    }

    private void OpenChatFragment() {
        MsgListRef = FirebaseDatabase
                .getInstance()
                .getReference("Messages");

        MsgListRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<Message> arrayList = new ArrayList<>();
                keyList = new ArrayList<>();
                for (DataSnapshot contactSnap : dataSnapshot.getChildren()) {
                    for (DataSnapshot msg : contactSnap.getChildren()) {
                        Message message = msg.getValue(Message.class);
                        arrayList.add(message);
                        keyList.add(msg.getKey());
                        Log.d("test", message.toString());
                    }
                }

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.Container, new ChatFragment(currentUser.getDisplayName(), arrayList, keyList), "ChatFragment")
                        .commit();

                setTitle("Chat Room");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void OpenLoginFragment()
    {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new FirstFragment(), "first")
                .commit();

        setTitle("Login");
    }


}

