package com.camera.nishan.selfie_stand.fragments;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.camera.nishan.selfie_stand.R;
import com.camera.nishan.selfie_stand.controller.CameraController;
import com.camera.nishan.selfie_stand.interfaces.FragmentNavigator;
import com.camera.nishan.selfie_stand.interfaces.OnCameraButtonClickListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nishan on 11/17/15.
 */
public class CameraFragment  extends Fragment{
    private final String tag = "CameraFragment";
    private SurfaceView preview = null;
    private SurfaceHolder previewHolder = null;
    private CameraController cameraController;
    private FragmentNavigator fragmentNavigator = null;
    private OnCameraButtonClickListener onCameraButtonClickListener;

    private View fragment;

    public CameraFragment() {
    }

    public void setOnCameraButtonClickListener(OnCameraButtonClickListener onCameraButtonClickListener){
        this.onCameraButtonClickListener = onCameraButtonClickListener;
    }

    public void setFragmentNavigator(FragmentNavigator fragmentNavigator) {
        this.fragmentNavigator = fragmentNavigator;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.camera_fragment, container, false);
        populateUI(fragment);
        return fragment;
    }

    private void populateUI(View fragment) {
        getActivity().getWindow().setFormat(PixelFormat.UNKNOWN);
        preview=(SurfaceView)fragment.findViewById(R.id.surfaceView_camera);
        previewHolder=preview.getHolder();
        previewHolder.addCallback(cameraController.getSurfaceCallback());
        previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

}
