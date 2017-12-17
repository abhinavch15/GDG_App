package com.example.abhinavchinta.gdg_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView lv;
    private RecyclerView recyclerView;
    ProgressBar progressBar;

    private FirebaseDatabase database;
    private DatabaseReference mFirebaseReference;
    private ChildEventListener mChildEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button button = (Button) findViewById(R.id.queryButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RetrieveFeedTask().execute();
            }
        });
    }

    class RetrieveFeedTask extends AsyncTask<Void, Void, String> {

        private Adapter mAdapter;
        private List<ListItem> movieList = new ArrayList<>();
        ArrayList<HashMap<String, String>> contactList;


        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... urls) {

            try {
                URL url = new URL("https://api.reliefweb.int/v1/reports?profile=full&limit=10");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();

                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }

        protected void onPostExecute(String response) {
            if(response == null) {
                response = "THERE WAS AN ERROR";
            }
            database = FirebaseDatabase.getInstance();
            mFirebaseReference = FirebaseDatabase.getInstance().getReference("data");

            progressBar.setVisibility(View.GONE);
            contactList = new ArrayList<>();
            Log.i("INFO", response);
            try {
                JSONObject jsonObj = new JSONObject(response);
                JSONArray data = jsonObj.getJSONArray("data");
                for (int i = 0; i < data.length(); i++) {
                    JSONObject c = data.getJSONObject(i);
                    JSONObject d = c.getJSONObject("fields");
                    String title = d.getString("title");
                    String id = d.getString("id");
                    String body=d.getString("body");
                    ListItem listItem = new ListItem(title,id,body);
                    String ab = mFirebaseReference.push().getKey();
                    mFirebaseReference.child(ab).setValue(listItem);
                    movieList.add(listItem);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            movieList.clear();
            database = FirebaseDatabase.getInstance();
            mFirebaseReference = FirebaseDatabase.getInstance().getReference("data");
            mFirebaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        //ListItem listitem= dataSnapshot1.getValue(ListItem.class);     //ERROR
                        //movieList.add(listitem);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"ERRER SAVING INTO DATABASE"+databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            });


            recyclerView = (RecyclerView) findViewById(R.id.list_itemmm);
            mAdapter = new Adapter(movieList);
            mAdapter.setData(response);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);

        }
    }
}

