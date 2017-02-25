package com.hello.nishan.listener;

import android.bluetooth.BluetoothSocket;

import com.hello.nishan.model.SocketTypeEnum;

/**
 * Created by nishan on 2/18/17.
 */
public interface HelperListener {
    void warn(String message);
    void info(String message);
    void error(String message);
    void success(String message);
    void setSocket(BluetoothSocket socket, SocketTypeEnum socketTypeEnum);
}
