package com.example.milan.sendcolor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class DefaultActivity extends ActionBarActivity {

    //widgets
    Button disconnectBtn;
    Button blinkBtn;
    TextView infoText;

    //lists
    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    //Handles
    View.OnClickListener disconnecting;
    View.OnClickListener blinking;

    //bluetooth
    BluetoothDevice dev;
    private static String address;
    private BluetoothSocket btSocket;
    private OutputStream outStream = null;
    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    //vars
    boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_default);

        //vars
        address = "00:13:12:30:44:39";

        //widgets
        disconnectBtn   = (Button)findViewById(R.id.disconnectBtn);
        blinkBtn        = (Button)findViewById(R.id.blinkBtn);
        infoText        = (TextView)findViewById(R.id.informationText2);
        expListView     = (ExpandableListView) findViewById(R.id.expandableListView);
        clicked         = false;

        //create list
        prepareListData();
        listAdapter = new com.example.milan.sendcolor.ExpandableListAdapter(this, listDataHeader,listDataChild);
        expListView.setAdapter(listAdapter);

        //handlers
        disconnecting = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplication(), "Disconnected!", Toast.LENGTH_LONG);
                toast.show();
                loadMain();
            }
        };

        blinking = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(clicked){
                    clicked = false;
                    //set button text to: Stop blinking
                    blinkBtn.setText("start blinking!");
                }else{
                    clicked = true;
                    //set button text to: restart blinking
                    blinkBtn.setText("stop blinking!");

                }

                writeToArduino(2, "fast");


            }
        };

         expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Log.d("Test","Parent: " + groupPosition);
                Log.d("Test","clicked: " + id);

                String color;
                if(groupPosition == 0)
                {
                    if(id == 1) color = "red";
                    else if(id == 2) color = "blue";
                    else if(id == 3) color = "green";
                    else if(id == 4) color = "yellow";
                    else if(id == 5) color = "aqua";
                    else if(id == 6) color = "pink";
                    else if(id == 7) color = "orange";
                    else if(id == 0) color = "none";
                    else color = "white";

                    changeColor(0, color);

                }
                else if(groupPosition == 1)
                {
                    if(id == 1) color = "red";
                    else if(id == 2) color = "blue";
                    else if(id == 3) color = "green";
                    else if(id == 4) color = "yellow";
                    else if(id == 5) color = "aqua";
                    else if(id == 6) color = "pink";
                    else if(id == 7) color = "orange";
                    else if(id == 0) color = "none";
                    else color = "white";

                    changeColor(1, color);
                }

                return false;
            }
        });

        ConnectToDevice();
    }

    private void changeColor(int type, String color){
        switch(type){
            case 0:

                Log.d("Test","Changing shoe to color: "+color);
                writeToArduino(type, color);

                break;
            case 1:

                Log.d("Test","Changing flap to color: "+color);
                writeToArduino(type, color);

                break;
        }

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Schoen");
        listDataHeader.add("Flap");

        // Adding child data
        List<String> schoen = new ArrayList<String>();
        schoen.add("None");
        schoen.add("Red");
        schoen.add("Blue");
        schoen.add("Green");
        schoen.add("Yellow");
        schoen.add("Aqua");
        schoen.add("Pink");
        schoen.add("Orange");

        List<String> flap = new ArrayList<String>();
        flap.add("None");
        flap.add("Red");
        flap.add("Blue");
        flap.add("Green");
        flap.add("Yellow");
        flap.add("Aqua");
        flap.add("Pink");
        flap.add("Orange");

        listDataChild.put(listDataHeader.get(0), schoen); // Header, Child data
        listDataChild.put(listDataHeader.get(1), flap);

    }

    private void loadMain() {
        Intent intent = new Intent(getApplication(), MainActivity.class);
        startActivity(intent);
    }

    private void loadData(){
        Log.d("Test","loadData");
        disconnectBtn.setOnClickListener(disconnecting);
        blinkBtn.setOnClickListener(blinking);
        Log.d("Test",""+dev);
        infoText.setText(" Connected to: Shoe\n Connection: "+dev);
    }

    private void ConnectToDevice() {
        dev = mBluetoothAdapter.getRemoteDevice(address);
        try{
            btSocket = dev.createRfcommSocketToServiceRecord(MY_UUID);
            btSocket.connect();
            loadData();
        } catch (IOException e){
            try{
                btSocket.close();
            }catch (IOException e2){
                Toast toast = Toast.makeText(this, "Bluetooth connection lost..", Toast.LENGTH_LONG);
                toast.show();
                loadMain();
            }

            Toast toast = Toast.makeText(this, "Bluetooth connection lost..", Toast.LENGTH_LONG);
            toast.show();
            loadMain();
        }
    }

    //function to send data to arduino
    private void writeToArduino(int type, String message) {
        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            Log.d("Test", "CATCH Before", e);
        }

        String msg = type+message;
        byte[] msgBuffer = msg.getBytes();

        try {
            outStream.write(msgBuffer);
            Log.d("Test", "Message: '"+msg+"' is send!");
        } catch (IOException e) {
            Log.d("Test", "CATCH While", e);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_default, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) { return true; }
        return super.onOptionsItemSelected(item);
    }
}
