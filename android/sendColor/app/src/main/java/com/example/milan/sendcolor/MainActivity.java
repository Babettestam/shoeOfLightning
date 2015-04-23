package com.example.milan.sendcolor;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.UUID;


public class MainActivity extends ActionBarActivity {

    //widgets
    Button connectBtn;
    TextView infoText;

    //vars
    private BluetoothSocket btSocket;
    private static String address;
    boolean connected;
    Intent discoverableIntent;
    BluetoothDevice dev;

    final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //widgets
        connectBtn  = (Button)findViewById(R.id.connectBtn);
        infoText    = (TextView)findViewById(R.id.informationText);

        //vars
        btSocket = null;
        address = "00:13:12:30:44:39";

        //create button listener
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBluetoothAdapter != null) checkBluetooth();
            }
        };

        //set button listener
        connectBtn.setOnClickListener(onClickListener);

    }

    private void checkBluetooth() {
        if(mBluetoothAdapter.isEnabled()){
            searchForDevices();
        }else{
            discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3000);
            startActivityForResult(discoverableIntent, 90);
        }
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){
        Log.d("Test","Test 3");
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode)
        {
            case 90:
                if(resultCode==RESULT_CANCELED){
                    infoText.setText("Connection failed! Please try again");
                }
                else
                {
                    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                    registerReceiver(mReceiver, filter);
                    mBluetoothAdapter.startDiscovery();

                    searchForDevices();
                }
                break;
        }
    }


    private void searchForDevices() {
        dev = mBluetoothAdapter.getRemoteDevice(address);
        infoText.setText("Connecting to: " + dev);
        mBluetoothAdapter.cancelDiscovery();

        ConnectToDevice();
    }

    private void ConnectToDevice() {

        try
        {
            btSocket = dev.createRfcommSocketToServiceRecord(MY_UUID);
//            btSocket.connect();
            loadDefault();

        } catch (IOException e)
        {
            try
            {
                btSocket.close();
            }
            catch (IOException e2)
            {
                infoText.setText("Trying to connect with:"+dev+" ... \nConnection failed");
            }
            infoText.setText("Trying to connect with:"+dev+" ... \nConnection failed");
        }
    }

    private void loadDefault() {
        Intent intent = new Intent(this, DefaultActivity.class);
        startActivity(intent);
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action))
            {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.d("Test",""+device.getName() + " - " + device.getAddress());
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
