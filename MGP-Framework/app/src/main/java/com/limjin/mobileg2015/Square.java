package com.limjin.mobileg2015;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import android.opengl.GLES20;

/**
 * Created by tanyiecher on 2/5/2016.
 */

/*************************************************************************************************
 * Square object
 * -Render
 *************************************************************************************************/
public class Square {

    /*************************************************************************************************
     * Variables
     *************************************************************************************************/
    //buffer------------------------------//
    private FloatBuffer vertexBuffer;
    private ShortBuffer drawListBuffer;

    //handles-------------------------//
    private int mPositionHandle;
    private int mColorHandle;

    // Set color with red, green, blue and alpha (opacity) values--------------------//
    float color[] = { 0.63671875f, 0.76953125f, 0.22265625f, 1.0f };

    //COORDs-----------------------------------------------------------------//
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -0.5f,  0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f,  0.5f, 0.0f }; // top right

    private short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    //CONST var-----------------------------------------------------------------//
    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;

    //Stride: remember??
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public Square() {
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);
    }

    public void draw(GLRenderer gl_renderer)
    {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(gl_renderer.mProgram);

        // get handle to vertex shader's vPosition member
        mPositionHandle = GLES20.glGetAttribLocation(gl_renderer.mProgram, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Prepare the SQUARE coordinate data---------------------------------//
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(gl_renderer.mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // Draw the square (PASS IN DRAW ORDER LOLZ)------------------------------//
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}
