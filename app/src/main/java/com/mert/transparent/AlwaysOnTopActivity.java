package com.mert.transparent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ViewSwitcher;

public class AlwaysOnTopActivity extends AppCompatActivity implements OnClickListener {
    /** Called when the activity is first created. */

    private Switch sw;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

      /*  startService(new Intent(this, AlwaysOnTopService.class));
        finish();*/
//
        sw= (Switch) findViewById(R.id.gifSwitch);


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(sw.isChecked()){

                   startService(new Intent(AlwaysOnTopActivity.this, AlwaysOnTopService.class));
                    finish();
                }

            }
        });

  //


        findViewById(R.id.start).setOnClickListener(this);
        findViewById(R.id.end).setOnClickListener(this);
        int x=0;


    }



    @Override
	public void onClick(View v) {
    	int view = v.getId();
    	if(view == R.id.start) {
    		startService(new Intent(this, AlwaysOnTopService.class));
    		finish();
    	}
    	else {
    		stopService(new Intent(this, AlwaysOnTopService.class));
    	}
    }


}