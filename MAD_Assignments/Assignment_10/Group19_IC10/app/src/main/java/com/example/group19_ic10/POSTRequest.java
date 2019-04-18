package com.example.group19_ic10;

import android.app.DownloadManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.textclassifier.TextClassification;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class POSTRequest extends Thread {
    @Override
    public void run() {
        try {
            FormBody.Builder formBody = new FormBody.Builder();


            for (String key : MainActivity.body.keySet()) {
                formBody.add(key, MainActivity.body.get(key));

            }


            RequestBody formbody = formBody.build();

            Request.Builder request = new Request.Builder()
                    .url(MainActivity.httpUrl)
                    .post(formbody);
            for (String key : MainActivity.request.keySet()) {
                Log.d("key",key);
                request.addHeader(key, MainActivity.request.get(key));
            }

            Request req = request.build();

            try {
                MainActivity.okHttpClient.newCall(req).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {

                        try (ResponseBody responseBody = response.body()) {
                            String jsonData = responseBody.string();

                            System.out.print(jsonData);
                            if (!response.isSuccessful())
                                throw new IOException("Unexpected code " + response);

                            Headers responseHeaders = response.headers();
                            for (int i = 0, size = responseHeaders.size(); i < size; i++) {

                                MainActivity.response.put(responseHeaders.name(i), responseHeaders.value(i));
                                System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                            }


                            MainActivity.request.clear();
                            MainActivity.body.clear();

                            try {

                                JSONObject obj=new JSONObject(jsonData);

                                Iterator<?> iterator = obj.keys();
                                while (iterator.hasNext()) {
                                    String key = (String)iterator.next();
                                    String val=obj.getString(key);
                                    MainActivity.response.put(key,val);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
