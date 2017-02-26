package com.camera.nishan.selfie_stand.activty;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.camera.nishan.selfie_stand.R;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private Button btMyCamera;
    private Button btFriendsCamera;
    private Button btShareCamera;
    private Button btCloseApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        findViews();
    }


    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2017-02-26 15:29:48 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        btMyCamera = (Button)findViewById( R.id.bt_my_camera );
        btFriendsCamera = (Button)findViewById( R.id.bt_friends_camera );
        btShareCamera = (Button)findViewById( R.id.bt_share_camera );
        btCloseApp = (Button)findViewById( R.id.bt_close_app );

        btMyCamera.setOnClickListener( this );
        btFriendsCamera.setOnClickListener( this );
        btShareCamera.setOnClickListener( this );
        btCloseApp.setOnClickListener( this );
    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2017-02-26 15:29:48 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == btMyCamera ) {
            Intent intent = new Intent(this, MyCameraActivity.class);
            startActivity(intent);
        } else if ( v == btFriendsCamera ) {
            // Handle clicks for btFriendsCamera
        } else if ( v == btShareCamera ) {
            // Handle clicks for btShareCamera
        } else if ( v == btCloseApp ) {
            // Handle clicks for btCloseApp
            super.onDestroy();
            System.exit(0);
        }
    }

}
