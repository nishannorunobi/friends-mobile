package com.hello.nishan.test;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by nishan on 2/18/17.
 */
public class SendRecordingStream extends AsyncTask<Void,Void,Void>{
    AudioRecord recorder;
    OutputStream agOutputStream = null;

    private int sampleRate = 16000 ; // 44100 for music
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);

    public SendRecordingStream(OutputStream outputStream) {
        this.agOutputStream = outputStream;
    }


    @Override
    protected Void doInBackground(Void... voids) {
            //DatagramSocket socket = new DatagramSocket();
            //Log.d("VS", "Socket Created");

            byte[] buffer = new byte[minBufSize];

            //Log.d("VS", "Buffer created of size " + minBufSize);
            //DatagramPacket packet;

            //final InetAddress destination = InetAddress.getByName("192.168.1.5");
            //Log.d("VS", "Address retrieved");

            recorder = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, minBufSize * 10);
            Log.d("VS", "Recorder initialized");

            recorder.startRecording();

            while (true) {

                //reading data from MIC into buffer
                minBufSize = recorder.read(buffer, 0, buffer.length);
                try {
                    agOutputStream.write(buffer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                //putting buffer in the packet
                //packet = new DatagramPacket(buffer,buffer.length,destination,port);

            }
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (recorder != null){
            recorder.stop();
        }
        super.onPostExecute(aVoid);
    }
}
