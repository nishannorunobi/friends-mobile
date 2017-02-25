package com.hello.nishan.test;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.AsyncTask;

/**
 * Created by nishan on 2/18/17.
 */
public class PlayRecordingStream{
    private int sampleRate = 16000 ; // 44100 for music
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minBufSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    AudioTrack speaker = null;
    byte[] buffer = null;
    public PlayRecordingStream(byte[] buffer) {
        this.buffer = buffer;
    }

    public Void play() {
       // atrack = new AudioTrack(AudioManager.STREAM_VOICE_CALL, 11025, AudioFormat.CHANNEL_CONFIGURATION_MONO, AudioFormat.ENCODING_PCM_16BIT, buffersize, AudioTrack.MODE_STREAM);
        speaker = new AudioTrack(AudioManager.STREAM_MUSIC,sampleRate,channelConfig,audioFormat,minBufSize,AudioTrack.MODE_STREAM);
        speaker.setPlaybackRate(11025);
        speaker.play();
        if (buffer != null && buffer.length > 0){
            speaker.write(buffer, 0, buffer.length);
        }
        return null;
    }

    protected void onPostExecute() {
        if (speaker != null) {
            speaker.stop();
            speaker = null;
        }
    }
}
