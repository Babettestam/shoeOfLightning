package com.example.milan.sendcolor;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.IOException;


public class ColorActivity extends ActionBarActivity {

    //widgets
    Button red;
    Button blue;
    Button green;

    //handlers
    View.OnClickListener colorHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color);

        Log.d("Test","help");

        //widgets
        red     = (Button)findViewById(R.id.redBtn);
        blue    = (Button)findViewById(R.id.blueBtn);
        green   = (Button)findViewById(R.id.greenBtn);

        //handlers
        colorHandler = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getButton(v);
            }
        };

        red.setOnClickListener(colorHandler);
        blue.setOnClickListener(colorHandler);
        green.setOnClickListener(colorHandler);

    }

    private void getButton(View v) {
        String color = "";
        switch(v.getId()){
            case R.id.redBtn:
                color = "red";
                break;
            case R.id.blueBtn:
                color = "blue";
                break;
            case R.id.greenBtn:
                color = "green";
                break;
        }

        Intent intent = new Intent(this,DefaultActivity.class);
        intent.putExtra("color", color);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
