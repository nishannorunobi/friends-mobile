package com.camera.nishan.selfie_stand.interfaces;

import android.media.Image;

/**
 * Created by nishan on 11/28/15.
 */
public interface OnCameraButtonClickListener {
    void turnOn();
    void turnOff();
    void setImageStream(byte[] bytes,Image image);
}
