package com.limjin.mobileg2015;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;

/*************************************************************************************************
 * Android page hosting game thread
 *************************************************************************************************/
public class GamePage extends Activity{

    //OpenGL surfaceView----------------------------------------------//
    //private GLSurfaceView mSurfaceView;
    private GLSurfaceView mGLView;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //Page Setup-------------------------------------------------------------------//
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        //OpenGL surfaceView setup----------------------------//
        initialize();

        //Create game 'thread'-------------------------------//
        //setContentView(new Controller(this));
        setContentView(mGLView);
    }

    /*************************************************************************************************
     * Init OpenGL 2.0
     *************************************************************************************************/
    private void initialize() {
        if (hasGLES20()) {
            mGLView = new GLSurfaceView(this);
            mGLView.setEGLContextClientVersion(2);
            mGLView.setPreserveEGLContextOnPause(true);
            mGLView.setRenderer(new Controller());   //surfaceView to render
        } else {
            // Time to get a new phone, OpenGL ES 2.0 not supported.
        }
    }

    protected void onResume() {
        super.onResume();

        if (mGLView != null) {
            mGLView.onResume();
        }
    }

    protected void onPause() {
        super.onPause();

        if (mGLView != null) {
            mGLView.onPause();
        }
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }

    /*************************************************************************************************
     * check if device supports OpenGL 2.0
     *************************************************************************************************/
    private boolean hasGLES20() {
        ActivityManager am = (ActivityManager)
                getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x20000;
    }
}
