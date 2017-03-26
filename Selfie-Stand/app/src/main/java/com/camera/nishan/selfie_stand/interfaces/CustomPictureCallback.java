package com.camera.nishan.selfie_stand.interfaces;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.os.Bundle;

import com.camera.nishan.selfie_stand.R;
import com.camera.nishan.selfie_stand.fragments.PhotoViewFragment;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by nishan on 3/25/17.
 */
public class CustomPictureCallback implements android.hardware.Camera.PictureCallback{
    private FragmentNavigator fragmentNavigator = null;
    private OnCameraButtonClickListener onCameraButtonClickListener;
    private Activity context;

    public CustomPictureCallback(FragmentNavigator fragmentNavigator,
                                 OnCameraButtonClickListener onCameraButtonClickListener,
                                 Activity context){
        this.fragmentNavigator = fragmentNavigator;
        this.onCameraButtonClickListener = onCameraButtonClickListener;
        this.context = context;
    }

    @Override
    public void onPictureTaken(byte[] data, android.hardware.Camera camera) {
        if (onCameraButtonClickListener != null) {
            onCameraButtonClickListener.turnOff();

        Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bitMapData = stream.toByteArray();

        try {
            FileOutputStream out = context.openFileOutput("picture.jpg", Activity.MODE_PRIVATE);
            out.write(bitMapData);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bundle bundle = new Bundle();
        bundle.putByteArray(PhotoViewFragment.TAKEN_PHOTO, data);
        if(fragmentNavigator != null) {
            fragmentNavigator.setFragment(R.layout.photo_view_fragment, bundle);
        }
    }
    }
}
