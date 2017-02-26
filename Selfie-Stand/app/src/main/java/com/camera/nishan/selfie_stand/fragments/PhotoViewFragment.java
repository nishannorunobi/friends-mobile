package com.camera.nishan.selfie_stand.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.camera.nishan.selfie_stand.R;

import java.io.InputStream;

/**
 * Created by nishan on 11/17/15.
 */
public class PhotoViewFragment extends Fragment {
    public static final String TAKEN_PHOTO = "TAKEN_PHOTO";
    private View fragment;
    private ImageView ivCard;
    private byte[] data = null;
    public PhotoViewFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragment = inflater.inflate(R.layout.photo_view_fragment, container, false);
        ivCard = (ImageView) fragment.findViewById(R.id.iv_card);
        Bundle bundle = this.getArguments();
        data = bundle.getByteArray(TAKEN_PHOTO);
        Bitmap image = BitmapFactory.decodeByteArray(data,0,data.length);
        Drawable drawable = null;
        drawable =  new BitmapDrawable(image);
        ivCard.setImageBitmap(getImage());
        return fragment;
    }

    private Bitmap getImage(){
        Bitmap bitmap = null;
        try {
            InputStream stream = getActivity().openFileInput("picture.jpg");
            bitmap = BitmapFactory.decodeStream(stream);
            stream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
