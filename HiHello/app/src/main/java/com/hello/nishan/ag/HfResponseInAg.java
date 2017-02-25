package com.hello.nishan.ag;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.hello.nishan.Constants;
import com.hello.nishan.listener.HelperListener;
import com.hello.nishan.model.SocketTypeEnum;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by root on 2/14/17.
 */
public class HfResponseInAg extends AsyncTask<Void,Void,Void>{
    private HelperListener logger;
    private InputStream inputStream = null;
    private Context context = null;
    private BluetoothSocket _socketAg = null;
    private String TAG = HfResponseInAg.class.getCanonicalName();
    private BluetoothServerSocket _socketServer = null;
    private BluetoothAdapter _bluetooth = null;
    public HfResponseInAg(Context context, BluetoothAdapter bluetooth, HelperListener logger){
        this.logger = logger;
        this.context = context;
        //this._serverSocket = serverSocket;
        this._bluetooth = bluetooth;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        byte[] buffer = new byte[1024];
        byte[] message = null;
        Log.d(TAG,"entered->-> doInBackground");
        try {
            _socketServer = _bluetooth.listenUsingRfcommWithServiceRecord(Constants.NAME_SECURE,Constants.UUID_HFP_AG);
        } catch (IOException e) {
            e.printStackTrace();
            //logger.error("failed to create server socket : "+e.getMessage());
            logger.setSocket(null,SocketTypeEnum.AG);
            return null;
        }
        try {
            if (_socketServer != null) {
                Log.d(TAG,"info->-> waiting for _socketAg to be created");
                _socketAg = _socketServer.accept();
                Log.d(TAG,"info ->-> _socketAg created");
                logger.setSocket(_socketAg, SocketTypeEnum.AG);
            } else {
                //logger.error("failed to initiate socket");
                Log.d(TAG,"failed to initiate socket");
            }
        } catch (IOException e) {
            e.printStackTrace();
            //logger.error("could not create ag socket->"+e.getMessage());
            try {
                if (_socketAg != null)
                    _socketAg.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            logger.setSocket(null,SocketTypeEnum.AG);
            return null;

        } finally {
            try {
                if (_socketServer != null)
                    _socketServer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            Log.d(TAG,"info ->-> going to create input stream from _socketAg");
            inputStream = _socketAg.getInputStream();
            Log.d(TAG,"info ->-> created input stream from _socketAg successful");
        } catch (IOException e) {
            e.printStackTrace();
            //logger.error("could not create input stream -> "+e.getMessage());
            try {
                if (_socketAg != null)
                    _socketAg.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
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
                String data = new String(message, "UTF-8");
                //Toast.makeText(context, data ,Toast.LENGTH_SHORT).show();
                //homeActivity.success("data : "+data);
                Log.e("received command :",data);
                // logger.success("Hf Response:"+data);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        try {
            Log.d(TAG,"info -->> begin to close _socketAg");
            if (_socketAg != null)
                _socketAg.close();
            logger.setSocket(null,SocketTypeEnum.AG);
            Log.d(TAG,"info -->> closed _socketAg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
