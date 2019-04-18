package com.example.group19_ic10;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity implements FirstFragment.OnFragmentInteractionListener, SecondFragment.OnFragmentInteractionListener
        , AddNotesFragment.OnFragmentInteractionListener, PostNote.OnFragmentInteractionListener, NotesAdapter.MessageOperations, DisplayNote.OnFragmentInteractionListener {

    static String httpUrl = null;
    static HashMap<String, String> request = new HashMap<>();

    static HashMap<Object, Object> response = new HashMap<>();

    static HashMap<String, String> body = new HashMap<>();

    static SharedPreferences sharedPreferences;
    static OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        httpUrl = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login";

        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        final String token = sharedPreferences.getString(getString(R.string.HTTPtoken), null);

        if (token != null) {
            httpUrl = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me";
            request.put("x-access-token", token);
            Thread p = new GETRequest();
            p.start();

            while (true) {
                if (request.size() == 0) {
                    break;
                } else {
                    try {
                        synchronized (p) {
                            Thread.sleep(400);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (response.containsKey("_id")) {

                response.put("token", token);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.Container, new AddNotesFragment(null), "addNotes")
                        .commit();

            } else {
                sharedPreferences.edit().remove("token");
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.Container, new FirstFragment(), "first")
                        .commit();
                setTitle("Login");

                Toast.makeText(getApplicationContext(), "Token might have expired Please login", Toast.LENGTH_SHORT).show();

            }
        }

        if (!sharedPreferences.contains("token")) {

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.Container, new FirstFragment(), "first")
                    .commit();
            setTitle("Login");
        }

    }

    @Override
    public void openSignUpForm() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new SecondFragment(), "second")
                .commit();
        setTitle("Sign Up");
    }

    @Override
    public void Login(String Email, String Pwd) {
        body.put("email", Email);
        body.put("password", Pwd);

        httpUrl = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/login";
        Thread p = new POSTRequest();
        p.start();

        while (true) {
            if (body.size() == 0) {
                break;
            } else {
                try {
                    synchronized (p) {
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        if (response.get("auth") == "true" && response.containsKey("token")) {

            String token1 = response.get("token").toString();
            sharedPreferences.edit().putString("token", token1).commit();

            httpUrl = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/me";
            request.put("x-access-token", token1);
            Thread g = new GETRequest();
            g.start();
            while (true) {
                if (request.size() == 0) {
                    break;
                } else {
                    try {
                        synchronized (g) {
                            Thread.sleep(400);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Container, new AddNotesFragment(token1), "AddNotes")
                    .commit();

            setTitle("Notes");


        } else {
            Toast.makeText(getApplicationContext(), "Please Enter correct Email and Password. ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void openLoginScreen() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new FirstFragment(), "first")
                .commit();

        setTitle("Login");
    }

    @Override
    public void SignUP(String EmailID, String Password, String Name) {
        body.put("email", EmailID);
        body.put("password", Password);
        body.put("name", Name);

        response.put("name", Name);
        httpUrl = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/auth/register";
        Thread p = new POSTRequest();
        p.start();


        while (true) {
            if (body.size() == 0) {
                break;
            } else {
                try {
                    Thread.sleep(400);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (response.get("auth") == "true" && response.containsKey("token")) {
            String token1 = response.get("token").toString();
            sharedPreferences.edit().putString("token", token1).commit();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Container, new AddNotesFragment(token1), "AddNotes")
                    .commit();
            setTitle("Notes");

        }

    }


    @Override
    public void OpenAddNotesFrag() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new PostNote(), "NewNote")
                .commit();

        setTitle("Add Note");
    }

    @Override
    public void SignOut() {
        SharedPreferences.Editor SPeditor = sharedPreferences.edit();
        SPeditor.remove("token").commit();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new FirstFragment(), "first")
                .commit();
        setTitle("Login");

    }

    @Override
    public void addNote(String noteTxt) {
        Thread p = new POSTRequest();
        httpUrl = "http://ec2-3-91-77-16.compute-1.amazonaws.com:3000/api/note/post";
        request.clear();
        request.put("x-access-token", response.get("token").toString());
        body.clear();
        body.put("text", noteTxt);
        p.start();
        while (true) {
            if (request.size() == 0 && body.size() == 0)
                break;
            try {
                Thread.sleep(400);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new AddNotesFragment(null), "AddNotes")
                .commit();
        setTitle("Notes");
    }

    @Override
    public void DisplayNote(Note notes) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new DisplayNote(notes), "DisplayNote")
                .commit();

        setTitle("Display Note");
    }

    @Override
    public void CloseClick() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.Container, new AddNotesFragment(null), "AddNotes")
                .commit();
        setTitle("Notes");
    }
}
