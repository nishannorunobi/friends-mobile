package com.hello.nishan.hf;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.util.Log;

import com.hello.nishan.Constants;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2/12/17.
 */
public class DeviceDiscovery extends BroadcastReceiver{
    ProgressDialog progressDialog = null;
    private String TAG = this.getClass().getName();
    IntentFilter filter = null;
    TargetDeviceRegisterListener targetDeviceRegisterListener;
    Context context = null;
    List<BluetoothDevice> deviceListAG = null;
    public DeviceDiscovery(Context context, IntentFilter filter,
                           TargetDeviceRegisterListener targetDeviceRegisterListener){
        this.targetDeviceRegisterListener = targetDeviceRegisterListener;
        this.context = context;
        this.filter = filter;
        progressDialog = new ProgressDialog(context);
        deviceListAG = new ArrayList<>();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //filter.addAction(BluetoothDevice.ACTION_NAME_CHANGED);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
            //discovery starts, we can show progress dialog or perform other tasks
            progressDialog.show();
        } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
            progressDialog.dismiss();
            targetDeviceRegisterListener.notifyCompletion();
        } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
            //bluetooth device found
            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
            //String uuid = intent.getStringExtra(BluetoothDevice.EXTRA_UUID);
            if (device != null) {
                deviceListAG.add(device);
                if (targetDeviceRegisterListener != null){
                    targetDeviceRegisterListener.addDevice(device);
                    if (device.getAddress().equalsIgnoreCase(Constants.SAMSUNG_GT_MAC)
                            && isAgSupportHFP(device)){
                        targetDeviceRegisterListener.updateDevice(device);
                        progressDialog.dismiss();
                        return;
                    }
                }
            }
        }
    }

    private boolean isAgSupportHFP(BluetoothDevice device) {
        ParcelUuid[] parcelUuidArr = device.getUuids();
        if (parcelUuidArr != null)
            for (ParcelUuid parcelUuid : parcelUuidArr){
                String strUUID = parcelUuid.getUuid().toString();
                Log.d(TAG,"Supported service uuid : >>>>>>>>>>>>>>>>>>>> "+strUUID);
                if (strUUID.equalsIgnoreCase(Constants.HSP_AG.getUuid().toString())){
                    return true;
                }
            }
        return false;
    }
}
