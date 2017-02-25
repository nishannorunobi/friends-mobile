package com.hello.nishan.hf;

import android.content.Context;
import android.os.AsyncTask;

import com.hello.nishan.listener.HelperListener;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by root on 2/14/17.
 */
public class SendDataFromHFToAG extends AsyncTask<Void,Void,Void>{
    private OutputStream outputStream = null;
    private Context context;
    private byte[] data = null;
    //private LogWriter logger = LogWriter.getInstance();
    HelperListener logger = null;
    public SendDataFromHFToAG(Context context, OutputStream outputStream, byte[] data,HelperListener logger){
        this.logger = logger;
        this.context = context;
        this.outputStream = outputStream;
        this.data = data;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        if (outputStream != null){
            try {
                outputStream.write(data);
            } catch (IOException e) {
                e.printStackTrace();
                logger.error("fail to write from hf to ag : ");
            }
        }
        return null;
    }
}
