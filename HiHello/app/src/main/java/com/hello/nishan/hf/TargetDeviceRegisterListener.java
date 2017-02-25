package com.hello.nishan.hf;

import android.bluetooth.BluetoothDevice;

/**
 * Created by root on 2/12/17.
 */
public interface TargetDeviceRegisterListener {
    void updateDevice(BluetoothDevice device);
    void addDevice(BluetoothDevice device);
    void notifyCompletion();
}
