package com.hello.nishan;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hello.nishan.ag.HfResponseInAg;
import com.hello.nishan.ag.SendDataFromAgToHF;
import com.hello.nishan.hf.AgResponseInHF;
import com.hello.nishan.hf.DeviceDiscovery;
import com.hello.nishan.hf.SendDataFromHFToAG;
import com.hello.nishan.hf.TargetDeviceRegisterListener;
import com.hello.nishan.listener.HelperListener;
import com.hello.nishan.model.AtCommands;
import com.hello.nishan.model.AtTypeEnum;
import com.hello.nishan.model.SocketTypeEnum;
import com.hello.nishan.test.SendRecordingStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    //LogWriter logger = LogWriter.getInstance();
    private BluetoothAdapter _bluetooth = null;
    private BluetoothSocket _socketHf = null;
    private BluetoothServerSocket _socketServer = null;
    private List<BluetoothDevice> _devicesListAG = new ArrayList<>();
    private BluetoothDevice _deviceChosenAG = null;
    DeviceDiscovery deviceDiscovery = null;
    private BluetoothSocket _socketAg = null;
    SendRecordingStream sendStreamThread = null;
    private static final String TAG = HomeActivity.class.getCanonicalName();

    private void searchNearDevices() {
        enableBT();
        final IntentFilter intentFilter = new IntentFilter();
        deviceDiscovery = new DeviceDiscovery(this,intentFilter,new TargetDeviceRegisterListener(){

            @Override
            public void addDevice(BluetoothDevice device) {
                _devicesListAG.add(device);
                logger.success("found device name : "+device.getName());
            }

            @Override
            public void notifyCompletion() {
                _bluetooth.cancelDiscovery();
                unregisterReceiver(deviceDiscovery);
            }

            @Override
            public void updateDevice(BluetoothDevice device) {
                _deviceChosenAG = device;
                logger.success("Nearby AG : "+device.getName());
            }
        });
        registerReceiver(deviceDiscovery,intentFilter);
        if (_bluetooth != null) {
            _bluetooth.startDiscovery();
        } else {
            logger.warn("Enable of your device's bluetooth first");
        }
    }

    private void startHF() {
        if (_deviceChosenAG != null && BluetoothDevice.BOND_BONDED == _deviceChosenAG.getBondState()) {
            logger.success("client connected");
            new AgResponseInHF(this,_deviceChosenAG,logger).execute();
        }
    }
    HfResponseInAg hfResponseInAg = null;
    private void startAG() {
        enableBT();
        hfResponseInAg = new HfResponseInAg(this,_bluetooth,logger);
        hfResponseInAg.execute();
    }

    private void sendCommandToAG() {
        if (_socketHf == null){
            logger.error("No hf socket found");
        }
        //String command = etCmd.getText().toString().trim();
        String command = AtCommands.requestToAgInHF.get(AtTypeEnum.SERVICE_LABEL_CONNECTION_INIT);
        try {
            new SendDataFromHFToAG(this,_socketHf.getOutputStream(),command.getBytes(),logger).execute();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Failed to get socket hf output stream");
        }
    }

    private void sendCommandToHF() {
        if (_socketAg == null){
            logger.error("No ag socket found");
        }
        String command = etCmd.getText().toString().trim();
        try {
            new SendDataFromAgToHF(this,_socketAg.getOutputStream(),command.getBytes(),logger).execute();
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("Failed to get socket ag output stream");
        }
    }

    public void enableBT(){
        _bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (_bluetooth == null) {
            logger.warn("Device doesnt Support Bluetooth");
        } else {
            if(!_bluetooth.isEnabled()) {
                _bluetooth.enable();
                Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                startActivityForResult(enabler, Constants.REQUEST_DISCOVERABLE);
            } else {
                logger.info("Bluetooth is already enabled");
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViews();
    }

    private void stopBluetooth() {
        Log.d(TAG,"info -->> called stopBluetooth()");
        if (_bluetooth == null){
            _bluetooth = BluetoothAdapter.getDefaultAdapter();
        }
        if (_bluetooth.isEnabled()){
            Log.d(TAG,"info -->> bluetooth already enabled");
            _bluetooth.disable();
        }
        logger.info("Bluetooth turned off");
    }

    private void stopHf(){
        if (_socketHf != null){
            try {
                _socketHf.getOutputStream().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                _socketHf.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        stopBluetooth();
    }

    private void stopAg(){
        if (hfResponseInAg != null){
            hfResponseInAg.cancel(true);
            hfResponseInAg = null;
        }
        stopBluetooth();
    }

    @Override
    protected void onPause() {
        if (deviceDiscovery != null){
            unregisterReceiver(deviceDiscovery);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        stopBluetooth();
        if (deviceDiscovery != null){
            unregisterReceiver(deviceDiscovery);
        }
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        stopBluetooth();
        super.onStop();
    }

    private Button btnEnableBtHf;
    private Button btnStopHf;
    private Button btnSearchAg;
    private Button btnCmdToAg;
    private Button btnEnableBtAg;
    private Button btnStopAg;
    private Button btnCmdToHf;
    private Button btnStartRecord;
    private Button btnStopRecord;
    private EditText etCmd;
    private TextView tvResponse;

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-02-14 00:47:43 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        btnEnableBtHf = (Button)findViewById( R.id.btn_enable_bt_hf );
        btnStopHf = (Button)findViewById( R.id.btn_stop_hf );
        btnSearchAg = (Button)findViewById( R.id.btn_search_ag );
        btnCmdToAg = (Button)findViewById( R.id.btn_cmd_to_ag );
        btnEnableBtAg = (Button)findViewById( R.id.btn_enable_bt_ag );
        btnStopAg = (Button)findViewById( R.id.btn_stop_ag );
        btnCmdToHf = (Button)findViewById( R.id.btn_cmd_to_hf );
        btnStartRecord = (Button)findViewById( R.id.btn_start_record );
        btnStopRecord = (Button)findViewById( R.id.btn_stop_record );
        etCmd = (EditText)findViewById( R.id.et_cmd );

        tvResponse = (TextView)findViewById( R.id.tv_response );
        //logger.setTextView(tvResponse);
        //logger.setContext(this);

        btnEnableBtHf.setOnClickListener( this );
        btnStopHf.setOnClickListener( this );
        btnSearchAg.setOnClickListener( this );
        btnCmdToAg.setOnClickListener( this );
        btnEnableBtAg.setOnClickListener( this );
        btnStopAg.setOnClickListener( this );
        btnCmdToHf.setOnClickListener( this );
        btnStartRecord.setOnClickListener( this );
        btnStopRecord.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        if ( v == btnEnableBtHf ) {
            startHF();
        } else if ( v == btnStopHf ) {
            stopHf();
        } else if ( v == btnSearchAg ) {
            searchNearDevices();
        } else if ( v == btnCmdToAg ) {
            sendCommandToAG();
        } else if ( v == btnEnableBtAg ) {
            startAG();
        } else if ( v == btnStopAg ) {
            stopAg();
        } else if ( v == btnCmdToHf ) {
            sendCommandToHF();
        } else if ( v == btnStartRecord ) {
            if (_socketAg != null && _socketAg.isConnected()) {
                try {
                    sendStreamThread = new SendRecordingStream(_socketAg.getOutputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sendStreamThread.execute();
            } else {
                logger.error("AG not connected yet");
            }
        } else if ( v == btnStopRecord ) {
            if (sendStreamThread != null){
                sendStreamThread.cancel(true);
                sendStreamThread = null;
            }
        } else {
            logger.info("No match for view id "+v.getId());
        }
    }

    HelperListener logger = new HelperListener() {

        @Override
        public void error(String message){
            Log.e("ERROR->#### :",message);
            if (tvResponse != null){
                tvResponse.setText(message);
                tvResponse.setTextColor(Color.RED);
            } else {
                Toast.makeText(HomeActivity.this,"Text view not found",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void success(String message){
            Log.e("SUCCESS->#### :",message);
            if (tvResponse != null){
                tvResponse.setText(message);
                tvResponse.setTextColor(Color.GREEN);
            } else {
                Toast.makeText(HomeActivity.this,"Text view not found",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void setSocket(BluetoothSocket socket, SocketTypeEnum socketTypeEnum) {
            if (socketTypeEnum == SocketTypeEnum.AG){
                _socketAg = socket;
                Log.d(TAG,"info ->-> _socketAg updated");
            } else if (socketTypeEnum == SocketTypeEnum.HF){
                _socketHf = socket;
                Log.d(TAG,"info ->-> _socketHF updated");
            }
        }

        @Override
        public void warn(String message){
            Log.e("WARN->#### :",message);
            if (tvResponse != null){
                tvResponse.setText(message);
                tvResponse.setTextColor(Color.YELLOW);
            } else {
                Toast.makeText(HomeActivity.this,"Text view not found",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void info(String message){
            Log.e("INFO->#### :",message);
            if (tvResponse != null){
                tvResponse.setText(message);
                tvResponse.setTextColor(Color.BLACK);
            } else {
                Toast.makeText(HomeActivity.this,"Text view not found",Toast.LENGTH_SHORT).show();
            }
        }
    };


}
