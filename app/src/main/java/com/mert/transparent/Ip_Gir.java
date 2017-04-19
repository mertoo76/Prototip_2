package com.mert.transparent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Ip_Gir extends Activity {

    public EditText editText;
    public Button button;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ip__gir);

        editText = (EditText)findViewById(R.id.editText);
        button = (Button)findViewById(R.id.button);
        sharedPreferences= getSharedPreferences("islem",MODE_PRIVATE);
        editor=sharedPreferences.edit();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tmp = editText.getText().toString();
                editor.putString("islem",tmp);
                editor.commit();
                Intent i = new Intent(getApplicationContext(),AlwaysOnTopActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
