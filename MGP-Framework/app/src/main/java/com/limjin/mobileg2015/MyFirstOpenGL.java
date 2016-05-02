package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 2/5/2016.
 */
import android.opengl.GLES20;

public class MyFirstOpenGL extends GLRenderer {

    /*************************************************************************************************
     * On create
     *************************************************************************************************/
    public void onCreate(int width, int height,
                         boolean contextLost) {
        super.onCreate(width, height, contextLost);
        GLES20.glClearColor(255.f, 0f, 0f, 1f);
    }

    /*************************************************************************************************
     * Called every frame
     *************************************************************************************************/
    public void onDrawFrame(boolean firstDraw) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT
                | GLES20.GL_DEPTH_BUFFER_BIT);
    }
}
