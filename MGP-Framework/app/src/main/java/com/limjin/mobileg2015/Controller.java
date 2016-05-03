package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 3/5/2016.
 */

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.Matrix;

/*************************************************************************************************
 * Controller class. Controls and manages all scenes
 *************************************************************************************************/
public class Controller implements Renderer
{
    /*************************************************************************************************
     * Variables
     *************************************************************************************************/


    /*************************************************************************************************
     * Necessary abstract methods
     *************************************************************************************************/
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
