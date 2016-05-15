package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 3/5/2016.
 */

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.limjin.mobileg2015.Utilities.Buffer_Utilities;
import com.limjin.mobileg2015.Utilities.Misc_Utilities;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*************************************************************************************************
 * Controller class. Controls and manages all scenes
 *************************************************************************************************/
public class Controller implements Renderer
{
    //the context "hosting" this Controller---------------------//
    Context context;
    SceneBasicOpenGL scene1 = new SceneBasicOpenGL();

    Controller(Context context)
    {
        this.context = context;
    }

    /*************************************************************************************************
     * Necessary abstract methods
     *************************************************************************************************/
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        //MUST CALL B4 DOING ANYTHING ELSE----------//
        Misc_Utilities.Init(context);
        MeshMan.Init();

        //View---------------------------------------//
        View.onSurfaceCreated();

        //Active scene Init-------------------------------//
        scene1.Init();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

        //View---------------------------------------//
        View.onSurfaceChanged(gl, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {

        //Update scene Init-------------------------------//
        scene1.Update();

        //pre render-------------------------------------------------//
        View.Pass1();

        //Render active scene----------------------------------------------//
        scene1.Render();

        //Setup pass2-----------------------------------//
        View.Pass2();

        //Render the combined G-Buffer-----------------------------------//
        View.Render_G_Buffer();

        //pre render-------------------------------------------------//
        View.PostRender();
    }
}
