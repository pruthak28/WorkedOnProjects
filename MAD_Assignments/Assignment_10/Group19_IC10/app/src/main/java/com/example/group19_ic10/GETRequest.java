package com.example.group19_ic10;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class GETRequest extends Thread {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void run() {

        try {
            HttpUrl.Builder httpBuider = HttpUrl.parse(MainActivity.httpUrl).newBuilder();
            if (MainActivity.body != null) {
                for (Map.Entry<String, String> param : MainActivity.body.entrySet()) {
                    httpBuider.addQueryParameter(param.getKey(), param.getValue());
                }
            }

            Request.Builder request = new Request.Builder()
                    .url(httpBuider.build());

            for (String key : MainActivity.request.keySet()) {
                request.addHeader(key, MainActivity.request.get(key));
            }
            Request r1 = request.build();

            try (Response response = MainActivity.okHttpClient.newCall(r1).execute()) {
                MainActivity.request.clear();
                MainActivity.body.clear();
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            } catch (IOException e) {
                e.printStackTrace();
            }

            MainActivity.okHttpClient.newCall(r1).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful())
                            throw new IOException("Unexpected code " + response);


                        String jsonData = responseBody.string();
                        JSONObject obj = null;
                        try {
                            obj = new JSONObject(jsonData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Iterator<?> iterator = obj.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            String val = null;
                            try {
                                val = obj.getString(key);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            MainActivity.response.put(key, val);
                            //do what you want with the key.
                        }
                        MainActivity.response.put("json", jsonData);
                        Headers responseHeaders = response.headers();
                        for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                            MainActivity.response.put(responseHeaders.name(i), responseHeaders.value(i));
                            //System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
                        }


                    }
                }
            });
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
