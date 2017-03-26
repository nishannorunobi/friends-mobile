package com.camera.nishan.selfie_stand.activty;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.camera.nishan.selfie_stand.R;
import com.camera.nishan.selfie_stand.camera.CameraFragment2;
import com.camera.nishan.selfie_stand.controller.FlashController2;
import com.camera.nishan.selfie_stand.fragments.PhotoViewFragment;
import com.camera.nishan.selfie_stand.interfaces.FragmentNavigator;
import com.camera.nishan.selfie_stand.interfaces.OnCameraButtonClickListener;
import com.camera.nishan.selfie_stand.interfaces.OnFlashButtonClickListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MyCameraActivity extends AppCompatActivity implements View.OnClickListener {

    //private CameraController cameraController;
    private FlashController2 flashController;
    private Button captureBtn;
    private ImageView flash_on_btn;
    private ImageView flash_off_btn;
    private ImageView camera_on_btn;
    private ImageView camera_off_btn;

    private CameraFragment2 cameraFragment;
    private PhotoViewFragment photoViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);
        cameraFragment = new CameraFragment2();
        cameraFragment.setOnCameraButtonClickListener(onCameraButtonClickListener);
        photoViewFragment = new PhotoViewFragment();
        /*Init view*/
        captureBtn = (Button) findViewById(R.id.btn_capture);
        captureBtn.setOnClickListener(this);
        flashController = new FlashController2(this,onFlashButtonClickListener);
        flash_on_btn=(ImageView)findViewById(R.id.flash_on_btn);
        flash_on_btn.setOnClickListener(this);
        flash_off_btn=(ImageView)findViewById(R.id.flash_off_btn);
        flash_off_btn.setOnClickListener(this);

        camera_on_btn=(ImageView)findViewById(R.id.camera_on_btn);
        camera_on_btn.setOnClickListener(this);
        camera_off_btn=(ImageView)findViewById(R.id.camera_off_btn);
        camera_off_btn.setOnClickListener(this);
    }

    private void loadFragment(android.support.v4.app.Fragment fragment) {
        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fl_fragment, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.flash_on_btn:
                if (onFlashButtonClickListener != null){
                    flashController.changeFlashMode();
                    onFlashButtonClickListener.turnOn();
                }
                break;
            case R.id.flash_off_btn:
                if (onFlashButtonClickListener != null){
                    flashController.changeFlashMode();
                    onFlashButtonClickListener.turnOff();
                }
                break;
            case R.id.btn_capture:
                capture();
                break;
            case R.id.camera_on_btn:
                if (onCameraButtonClickListener != null) {
                        loadFragment(cameraFragment);
                    onCameraButtonClickListener.turnOn();
                }
                break;
            case R.id.camera_off_btn:
                if (onCameraButtonClickListener != null) {
                    cameraFragment = null;
                    onCameraButtonClickListener.turnOff();
                }
                break;
            default:
                break;
        }
    }

    private void capture() {
        if (cameraFragment != null) {
            cameraFragment.takePicture();
            cameraFragment = null;
            onCameraButtonClickListener.turnOff();
            /*Bundle bundle = new Bundle();
            bundle.putByteArray(PhotoViewFragment.TAKEN_PHOTO,imageStreamBytes);
            photoViewFragment.setArguments(bundle);*/
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            loadFragment(photoViewFragment);
        }
    }

    private byte[] imageStreamBytes;

    private OnCameraButtonClickListener onCameraButtonClickListener = new OnCameraButtonClickListener() {
        @Override
        public void turnOn() {
            camera_on_btn.setVisibility(View.GONE);
            camera_off_btn.setVisibility(View.VISIBLE);
        }

        @Override
        public void turnOff() {
            camera_off_btn.setVisibility(View.GONE);
            camera_on_btn.setVisibility(View.VISIBLE);
        }

        @Override
        public void setImageStream(byte[] bytes,Image image) {
            imageStreamBytes = bytes;
        }
    };

    Image image = null;

    private OnFlashButtonClickListener onFlashButtonClickListener = new OnFlashButtonClickListener() {
        @Override
        public void turnOn() {
            flash_on_btn.setVisibility(View.GONE);
            flash_off_btn.setVisibility(View.VISIBLE);
        }

        @Override
        public void turnOff() {
            flash_off_btn.setVisibility(View.GONE);
            flash_on_btn.setVisibility(View.VISIBLE);
        }
    };
}
