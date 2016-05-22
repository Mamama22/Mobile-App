package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 3/5/2016.
 */

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import com.limjin.mobileg2015.Utilities.Buffer_Utilities;
import com.limjin.mobileg2015.Utilities.Misc_Utilities;

import java.text.DecimalFormat;

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

    //FPS counter-----------------------------------------//
    DecimalFormat df = new DecimalFormat("#.00");
    int frameCount = 0;
    double dt = 0.0;
    float fps;
    long lastTime,lastFPSTime;

    Controller(Context context)
    {
        this.context = context;
        fps = 0.f;
        lastTime = lastFPSTime = 0;
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

        ///////////////////////////////////////////////////////////////////////////////////////////

        //pre render-------------------------------------------------//
        View.Pass1(true);

        //Render active scene----------------------------------------------//
        scene1.Render();

        ///////////////////////////////////////////////////////////////////////////////////////////

        //Setup pass1-----------------------------------//
        View.PostProcessingSetup(0, false);

        //Render the combined G-Buffer-----------------------------------//
        View.Render_G_Buffer();

        //Setup pass2-----------------------------------//
        View.PostProcessingSetup(1, true);

        //Render the combined G-Buffer-----------------------------------//
        View.Render_G_Buffer();

        ///////////////////////////////////////////////////////////////////////////////////////////
        //render GUI
        String fpsString;
        fpsString = "FPS: " + df.format(fps);

        View.GUI_Setup();
        View.RenderText(fpsString, MeshMan.TEX_FONT_1, -1.2f, 1.3f, 0.3f);

        //pre render-------------------------------------------------//
        View.PostRender();

        ///////////////////////////////////////////////////////////////////////////////////////////
        //calculate the FPS-----------------------------------------------//
        calculateFPS();
    }

    /*************************************************************************************************
     * Misc: calculate the entire update + render FPS
     *************************************************************************************************/
    public void calculateFPS()
    {
        frameCount++;

        long currentTime = System.currentTimeMillis();
        dt = (currentTime - lastTime) / 1000.f;
        lastTime = currentTime;

        if(currentTime - lastFPSTime > 1000)
        {
            fps = (frameCount * 1000.f) / (currentTime - lastFPSTime);
            //  yolo = Float.parseFloat("fps");
            lastFPSTime = currentTime;
            frameCount = 0;
        }
    }
}
