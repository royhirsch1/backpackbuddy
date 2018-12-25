package com.example.coursefreak.coursefreak;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

public class UGView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ugview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String course_name = intent.getStringExtra("course_name");
        TextView course_title = findViewById(R.id.textView_ug);
        course_title.setText(course_name);

        String course_num = intent.getStringExtra("course_id");
        String url = "https://ug3.technion.ac.il/rishum/course/"+course_num;
        WebView webView = findViewById(R.id.webview);
        webView.loadUrl(url);
    }

}