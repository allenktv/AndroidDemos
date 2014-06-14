package com.allen.bluetoothme;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity {
    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> pairedDevices;
    private ListView mPairedDevices;
    private ArrayAdapter<String> mPairedAdapter;

    public static final int REQUEST_ENABLE_BT = 1;
    public static final String TAG = "bluetoothme.MainActivity";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mPairedDevices = (ListView)findViewById(R.id.paired_devices_list);
        //Get default Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if(mBluetoothAdapter==null){
            //Device does not support bluetooth

        }else if(!mBluetoothAdapter.isEnabled()){
            Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(bluetoothIntent,REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_ENABLE_BT){
            if(resultCode == RESULT_OK){
                //Successfully enabled bluetooth
                Log.d(TAG,"Successfully turned on bluetooth");
                getPairedDevices();
            }
        }
    }

    private void getPairedDevices(){
        pairedDevices = mBluetoothAdapter.getBondedDevices();
        //If there are paired devices
        if(pairedDevices.size()>0){
            Log.d(TAG,"Number of paired devices found:"+pairedDevices.size());
            //loop through paired devices
            List<String> mStrings = new ArrayList<String>();
            for(BluetoothDevice device: pairedDevices){
                mStrings.add(device.getName()+ "\n" + device.getAddress());
            }
            mPairedAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mStrings);
            mPairedDevices.setAdapter(mPairedAdapter);
        }
    }
}
