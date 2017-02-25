package com.hello.nishan.ag;

import android.content.Context;
import android.os.AsyncTask;

import com.hello.nishan.listener.HelperListener;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by root on 2/14/17.
 */
public class SendDataFromAgToHF extends AsyncTask<Void,Void,Void>{
    private OutputStream outputStream = null;
    private Context context;
    private byte[] data = null;
    //private LogWriter logger = LogWriter.getInstance();
    HelperListener logger = null;
    public SendDataFromAgToHF(Context context,OutputStream outputStream,byte[] data,HelperListener logger){
        this.context = context;
        this.outputStream = outputStream;
        this.data = data;
        this.logger = logger;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        if (outputStream != null){
            try {
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("fail to write from ag to hf : ");
            }
        }
        return null;
    }
}
