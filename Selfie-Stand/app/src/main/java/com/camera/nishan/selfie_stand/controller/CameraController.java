package com.camera.nishan.selfie_stand.controller;

import android.app.Activity;
import android.hardware.Camera;
import android.view.SurfaceHolder;

/**
 * Created by nishan on 11/16/15.
 */
public class CameraController {
    private Camera camera = null;
    private boolean inPreview = false;
    private boolean cameraConfigured = false;
    private Activity context;

    public CameraController(Activity context){
        this.context = context;
    }

    public SurfaceHolder.Callback getSurfaceCallback() {
        return surfaceCallback;
    }

    public Camera getCamera() {
        return camera;
    }

    private void startPreview() {
        if (cameraConfigured && camera!=null) {
            camera.setDisplayOrientation(90);
            camera.startPreview();
            inPreview=true;
        }
    }
    private Camera.Size getBestPreviewSize(int width, int height,
                                           Camera.Parameters parameters) {
        Camera.Size result=null;

        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width<=width && size.height<=height) {
                if (result==null) {
                    result=size;
                }
                else {
                    int resultArea=result.width*result.height;
                    int newArea=size.width*size.height;

                    if (newArea>resultArea) {
                        result=size;
                    }
                }
            }
        }

        return(result);
    }

    private SurfaceHolder.Callback surfaceCallback =  new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
                camera= Camera.open();
                startPreview();
            }

        @Override
        public void surfaceChanged(SurfaceHolder previewHolder, int format, int width, int height) {
            if (camera != null && previewHolder.getSurface() != null) {
                try {
                    camera.setPreviewDisplay(previewHolder);
                }catch (Exception exception) {
                    System.err.println("exception"+exception);
                }

                if (!cameraConfigured) {
                    Camera.Parameters parameters=camera.getParameters();
                    Camera.Size size=getBestPreviewSize(width, height,
                            parameters);
                    if (size!=null) {
                        parameters.setPreviewSize(size.width, size.height);
                        camera.setParameters(parameters);
                        cameraConfigured=true;
                    }
                }
            }
            startPreview();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (inPreview) {
                camera.stopPreview();
            }

            camera.release();
            camera=null;
            inPreview=false;

        }
    };
}
