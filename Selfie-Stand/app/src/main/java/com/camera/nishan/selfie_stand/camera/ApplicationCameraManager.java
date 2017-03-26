package com.camera.nishan.selfie_stand.camera;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;

/**
 * Created by nishan on 3/26/17.
 */
public abstract class ApplicationCameraManager {
    private static CameraManager cameraManager;
    private static String cameraId;
    private ApplicationCameraManager(){}
    public static CameraManager getCameraManager(Activity context){
        if (cameraManager == null){
            cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        }
        return cameraManager;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static String getCameraId(){
        if (cameraId == null) {
            try {
                String cameraIdList[] = cameraManager.getCameraIdList();
                for (String id : cameraIdList) {
                    CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(id);
                    if (cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE)) {
                        cameraId = id;
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cameraId;
    }
}
