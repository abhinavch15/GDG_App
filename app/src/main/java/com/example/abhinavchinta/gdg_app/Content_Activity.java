package com.example.abhinavchinta.gdg_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Content_Activity extends AppCompatActivity {
    private String a;
    private String content;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_);
        Intent intent = getIntent();
        String data=intent.getStringExtra("data");
        String body=intent.getStringExtra("body");
        textView = (TextView)findViewById(R.id.content);
        textView.setText(body);

    }

}
