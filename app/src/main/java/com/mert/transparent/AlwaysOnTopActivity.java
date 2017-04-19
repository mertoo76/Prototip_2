package com.mert.transparent;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;

import android.widget.CompoundButton;
import android.widget.Switch;


public class AlwaysOnTopActivity extends AppCompatActivity  {
    /** Called when the activity is first created. */
    int i=0;
    private Switch sw;
    String tmp;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


//

        sharedPreferences= getPreferences(MODE_PRIVATE);
        editor=sharedPreferences.edit();
        tmp = getSharedPreferences("islem",MODE_PRIVATE).getString("islem"," ");


        WebView webView = (WebView) findViewById(R.id.webView);


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDomStorageEnabled(true);
        webView.loadUrl("http://"+tmp+":8081/video");


      /*  String gifName = "https://media.giphy.com/media/l0HlwDVdW0wHPTWNy/giphy.gif";
        String yourData = "<html style=\"margin: 0;\">\n" +
                "    <body style=\"margin: 0;\">\n" +
                "    <img src=" + gifName + " style=\"width: 100%; height: 100%\" />\n" +
                "    </body>\n" +
                "    </html>";

        webView.loadData(yourData, "text/html; charset=utf-8", "UTF-8");*/



        sw= (Switch) findViewById(R.id.gifSwitch);


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(sw.isChecked()){

                 /*  Intent serviceIntent = new Intent(AlwaysOnTopService.class.getName());
                  serviceIntent.setPackage("com.mert.transparent");

                    serviceIntent.putExtra("islemservis", tmp);
                startService(serviceIntent);*/

                    startService(new Intent(AlwaysOnTopActivity.this, AlwaysOnTopService.class).putExtra("test", tmp));



                  // startService(new Intent(AlwaysOnTopActivity.this, AlwaysOnTopService.class));
                    finish();
                }

            }
        });

  //



      /*  findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
        */


    }












}