package com.example.testtss;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {

    private ImageView avatar;
    private TextView email;
    private TextView name;
    private RecyclerView rView;
    private Button button;
    private String url = "https://reqres.in/api/users?page=2";
    private NotesAdapter adapter;
    private String jsonResponse;
    private Bitmap gIcon;
    private static ArrayList<Note> notes;
    private String avt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar = getSupportActionBar();
        if(bar != null){bar.hide();}
        avatar = findViewById(R.id.avatar);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        rView = findViewById(R.id.rView);
        button = findViewById(R.id.button);
        notes = new ArrayList<>();
        notes.clear();
        adapter = new NotesAdapter(notes);
        rView.setLayoutManager(new LinearLayoutManager(this));
        rView.setAdapter(adapter);
    }

    public void onClickButton(View view) {
        notes.clear();
        adapter.notifyDataSetChanged();
        DownloadJSONTask task = new DownloadJSONTask();
        try {
            jsonResponse = task.execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private class DownloadJSONTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            java.net.URL url = null;//объект URL
            HttpURLConnection urlConnection = null;//объект соединения
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bReader = new BufferedReader(reader);
                String line = bReader.readLine();
                while (line != null) {
                    result.append(line);
                    line = bReader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("POST_EX", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                for (int i = 0; i < 6; i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    String mail = data.getString("email");
                    String first_name = data.getString("first_name");
                    String last_name = data.getString("last_name");
                    avt = data.getString("avatar");
                    notes.add(new Note(i, avt, mail, first_name+" "+last_name));
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
