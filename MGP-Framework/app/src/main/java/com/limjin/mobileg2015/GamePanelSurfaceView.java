package com.limjin.mobileg2015;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.limjin.mobileg2015.Utilities.Draw;

/*************************************************************************************************
 * Implement this interface to receive information about changes to the surface.
 * Runs as a seperate thread from Android
 *************************************************************************************************/
public class GamePanelSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    /**********************************************
     * System
     *********************************************/
    // Thread to run----------------------//
    private GameThread myThread = null;

    // Variables for FPS---------------------------//
    public float FPS;

    /**********************************************
     * Gameplay
     ********************************************/

    // Variables used for background rendering-------------//
    private Bitmap bg, scaledbg;

    // background start and end point
    private short bgX = 0, bgY = 0;

    //test object
    private Bitmap testObj;
    Vector2 testPos = new Vector2();
    Vector2 testScale = new Vector2();

    // Variable for Game State check
    public static int GameState = 0;

    /**************************************************************************************
     * constructor for this GamePanelSurfaceView class
     **************************************************************************************/
    public GamePanelSurfaceView(Context context) {

        // Context is the current state/'screen' of the application/object
        super(context);

        // Adding the callback (this) to the surface holder to intercept events----------------//
        getHolder().addCallback(this);

        // Set information to get screen size------------//
        Draw.Set(1400, 787, context);

        // Load the image when this class is being instantiated-----------------//
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
        scaledbg = Bitmap.createScaledBitmap(bg, Draw.ScreenWidth, Draw.ScreenHeight, true);

        // Create the game loop thread--------------------------//
        myThread = new GameThread(getHolder(), this);

        //test object-----------------------//
        testPos.Set(800, 0);
        testScale.Set(100, 787);
        testObj = Draw.SetBitmap(testScale.x, testScale.y, R.drawable.rubbish, getResources());

        // Make the GamePanel focusable so it can handle events------------------------//
        setFocusable(true);
    }

    /**************************************************************************************
     * Necessary abstract methods
     **************************************************************************************/
    public void surfaceCreated(SurfaceHolder holder) {

        // Create the thread
        if (!myThread.isAlive()) {
            myThread.startRun(true);
            myThread.start();
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // Destroy the thread
        if (myThread.isAlive()) {
            myThread.startRun(false);
        }

        boolean retry = true;
        while (retry) {
            try {
                myThread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**************************************************************************************
     * Update
     **************************************************************************************/
    public void Update(float dt, float fps) {
        FPS = fps;

        switch (GameState) {
            case 0: {

                // Update the background to allow panning effect
                bgX -= 600 * dt; // Allow panning speed
                if (bgX < -Draw.ScreenWidth) {
                    bgX = 0;
                }
            }
            break;
        }
    }

    /**************************************************************************************
     * Input
     **************************************************************************************/
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //touch pos in SCREEN SPACE----------------------------//
        float x = event.getX();
        float y = event.getY();

        //convert to VIEW SPACE-----------------------//
        x = Draw.ConvertX(x);
        y = Draw.ViewHeight - Draw.ConvertY(y); //invert Y to set origin at bottom

        //pass in--------------------------------------//
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //game touch-------------------------------------//

                break;

            case MotionEvent.ACTION_UP:

                //game touch-------------------------------------//

                break;

            case MotionEvent.ACTION_MOVE:

                //game touch-------------------------------------//

                break;
        }
        return true;
    }

    /**************************************************************************************
     * Draw
     **************************************************************************************/
    public void Draw(Canvas canvas) {
        switch (GameState) {
            case 0:
            {
                //temp. fix, why draw game when is still in splash page? CRASH-----------------------//
                if (Draw.canvas == null || Draw.paint == null) {
                    break;
                }

                //render gameplay-----------------------------//
                RenderGameplay(canvas);
            }
            break;
        }
    }


    /**************************************************************************************
     * Gameplay draw
     **************************************************************************************/
    public void RenderGameplay(Canvas canvas) {

        // Re-draw 2nd image after the 1st image ends
        if (canvas == null) {
            return;
        }

        //bg----------------------------------------------------------//
        Draw.canvas.drawBitmap(scaledbg, bgX, bgY, null);
        Draw.canvas.drawBitmap(scaledbg, bgX + Draw.ScreenWidth, bgY, null);

        //draw test obj-------------------//
        Draw.DrawBitmap(testObj, testPos, testScale);

        //text---------------------------//
        String FPS_str = "FPS: " + Draw.FPS;
        Draw.DrawText(FPS_str, 0.f, 0.f, 100.f);
    }
}
