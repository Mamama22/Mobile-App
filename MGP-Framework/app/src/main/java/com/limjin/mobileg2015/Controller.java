package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 3/5/2016.
 */

import android.graphics.*;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*************************************************************************************************
 * Controller class. Controls and manages all scenes
 *************************************************************************************************/
public class Controller implements Renderer
{
    float angle;

    /*************************************************************************************************
     * Variables
     *************************************************************************************************/
    // This triangle is red, green, and blue.
    final float[] triangle1VerticesData = {
            // X, Y, Z,
            // R, G, B, A
            -0.5f, -0.25f, 0.0f,
            1.0f, 0.0f, 0.0f, 1.0f,

            0.5f, -0.25f, 0.0f,
            0.0f, 0.0f, 1.0f, 1.0f,

            0.0f, 0.559016994f, 0.0f,
            0.0f, 1.0f, 0.0f, 1.0f};

    Mesh triangle = new Mesh();

    /*************************************************************************************************
     * Necessary abstract methods
     *************************************************************************************************/
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //View---------------------------------------//
        View.onSurfaceCreated(gl, config);

        //Mesh-------------------------------------//
        triangle.Init(triangle1VerticesData);

        angle = 0.f;
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        //View---------------------------------------//
        View.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        //Render mesh----------------------------------------------//
        View.SetTransMat_toIdentity();
        View.SetTransMat_toTranslate(-1.f, 0.f, 0.f);
        View.SetTransMat_toRotate(angle, 0.f, 0.f, 1.f);
        View.drawMesh(triangle);
        angle += 1.f;

        if(angle >= 360.f)
            angle = 0.f;
        else if(angle < 0.f)
            angle = 360.f;

        View.SetTransMat_toIdentity();
        View.SetTransMat_toTranslate(1.f, 0.f, 0.f);
        View.SetTransMat_toRotate(-140.f, 0.f, 0.f, 1.f);
        View.drawMesh(triangle);
    }
}
