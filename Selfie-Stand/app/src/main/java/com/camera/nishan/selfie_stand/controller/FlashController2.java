package com.camera.nishan.selfie_stand.controller;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.camera.nishan.selfie_stand.R;
import com.camera.nishan.selfie_stand.camera.ApplicationCameraManager;
import com.camera.nishan.selfie_stand.interfaces.OnFlashButtonClickListener;

/**
 * Created by nishan on 3/25/17.
 */
public class FlashController2 {
    CameraCharacteristics cameraCharacteristics;
    CameraDevice mCameraDevice;
    CameraCaptureSession mSession;
    CaptureRequest.Builder mBuilder;
    CameraManager cameraManager;
    String cameraId = null; // 0 back camera, 1 for front camera.
    private boolean isFlashOn;
    private boolean hasFlash;
    private Activity context;
    private OnFlashButtonClickListener onFlashButtonClickListener;

    public FlashController2(Activity context, OnFlashButtonClickListener onFlashButtonClickListener){
        this.context = context;
        this.onFlashButtonClickListener = onFlashButtonClickListener;
        hasFlash = context.getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
        if (hasFlash) {
            initCamera();
        } else {
            showAllertDiaglog();
        }
    }

    private void initCamera() {
        cameraManager = ApplicationCameraManager.getCameraManager(context);
        cameraId = ApplicationCameraManager.getCameraId();
    }

    public void changeFlashMode() {
        if (!hasFlash) {
            showAllertDiaglog();
            onFlashButtonClickListener.turnOff();
        } else {
            if (isFlashOn) {
                turnOffFlash();
                isFlashOn = false;
            } else {
                turnOnFlash();
                isFlashOn = true;
            }
        }
    }

    private void showAllertDiaglog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your mobile does not support flashlight")
                .setTitle("Error");
        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCancelable(true);
        dialog.show();
    }


    private void turnOffFlash() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
                //playOnOffSound();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void turnOnFlash() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
                //playOnOffSound();
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /*private void playOnOffSound(){

        MediaPlayer mp = MediaPlayer.create(context, R.raw.flash_sound);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                // TODO Auto-generated method stub
                mp.release();
            }
        });
        mp.start();
    }*/

}
