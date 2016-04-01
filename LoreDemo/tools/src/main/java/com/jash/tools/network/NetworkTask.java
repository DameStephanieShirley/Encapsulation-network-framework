package com.jash.tools.network;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

/**
 * Created by jash on 16-4-1.
 */
public class NetworkTask<T> extends AsyncTask<NetworkTask.Callback<T>, Void, Object> {
    private static Gson gson;
    static {
        gson = new GsonBuilder().setVersion(1).registerTypeAdapter(String[].class, new TypeAdapter<String[]>() {
            @Override
            public void write(JsonWriter out, String[] value) throws IOException {
                if (value != null && value.length > 0 ) {
                    StringBuilder builder = new StringBuilder();
                    for (String s : value) {
                        builder.append(s).append(',');
                    }
                    builder.deleteCharAt(builder.length() - 1);
                    out.value(builder.toString());
                } else {
                    out.nullValue();
                }
            }

            @Override
            public String[] read(JsonReader in) throws IOException {
                String s = in.nextString();
                String[] result = null;
                if (s != null) {
                    result = s.split(",");
                }
                return result;
            }
        }).registerTypeAdapter(Date.class, new TypeAdapter<Date>() {
            @Override
            public void write(JsonWriter out, Date value) throws IOException {
                out.value(value.getTime());
            }

            @Override
            public Date read(JsonReader in) throws IOException {
                long l = in.nextLong();
                return new Date(l);
            }
        }).create();
    }

    public Callback<T> callback;
    public String url;
    public Type type;
    public NetworkTask(String url, Type type) {
        this.url = url;
        this.type = type;
    }

    @Override
    protected Object doInBackground(NetworkTask.Callback<T>... params) {
        try {
            callback = params[0];
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode == 200) {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                InputStream is = connection.getInputStream();
                byte[] buffer = new byte[10 << 10];
                int length;
                while ((length = is.read(buffer)) != -1){
                    bos.write(buffer, 0, length);
                }
                return gson.fromJson(bos.toString("UTF-8"), type);
            } else {
                return new Exception("ResponseCode:" + responseCode);
            }
        } catch (IOException e) {
            return e;
        }
    }

    @Override
    protected void onPostExecute(Object response) {
        if (response instanceof Exception) {
            callback.onFailure((Exception) response);
        } else {
            callback.onResponse((T) response);
        }
    }
    public interface Callback<T>{
        void onResponse(T t);
        void onFailure(Exception e);
    }
}
