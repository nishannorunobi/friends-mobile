package com.hello.nishan.hf;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;

import com.hello.nishan.Constants;
import com.hello.nishan.listener.HelperListener;
import com.hello.nishan.model.SocketTypeEnum;
import com.hello.nishan.test.PlayRecordingStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by root on 2/14/17.
 */
public class AgResponseInHF extends AsyncTask<Void,Void,Void>{
    //private LogWriter logger = LogWriter.getInstance();
    HelperListener logger = null;
    private InputStream inputStream = null;
    private Context context;
    private BluetoothDevice device;
    private BluetoothSocket _socketHf = null;

    public AgResponseInHF(Context context, BluetoothDevice device,HelperListener logger){
        this.logger = logger;
        this.context = context;
        this.device = device;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        byte[] buffer = new byte[1024];
        byte[] message = null;
        try {
            _socketHf = device.createRfcommSocketToServiceRecord(Constants.UUID_HFP);
        } catch (IOException e) {
            e.printStackTrace();
            //logger.error("failed to crate client socket");
        }
        if (_socketHf != null){
            try {
                _socketHf.connect();
            } catch (IOException e) {
                e.printStackTrace();
              //  logger.error("failed to connect client socket");
            }
            logger.setSocket(_socketHf, SocketTypeEnum.HF);
        }
        logger.success("Connected to device : "+device.getName());
        try {
            inputStream = _socketHf.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        //    logger.error("failed to create input stream hf ->"+e.getMessage());
        }

        while (!Thread.interrupted()) {
            int bytesRead = 0;
            try {
                bytesRead = inputStream.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            message = new byte[bytesRead];
            System.arraycopy(buffer, 0, message, 0, bytesRead);
            try {
                //PlayRecordingStream playRecordingStream = new PlayRecordingStream(buffer);
                //playRecordingStream.play();
                String str = String.format("%02x", buffer);
                logger.success("Ag Response1 :"+str);
                str = new String(buffer,"UTF-8");
                logger.success("Ag Response2 :"+str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (inputStream != null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
