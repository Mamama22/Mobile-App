package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 5/5/2016.
 */

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/*************************************************************************************************
 * Mesh class: loads mesh into it
 *************************************************************************************************/
public class Mesh
{
    /** How many bytes per float. */
    public int mBytesPerFloat = 4;

    /** How many elements per vertex. */
    public int mStrideBytes = 7 * mBytesPerFloat;

    /** Size of the position data in elements. */
    public int mPositionDataSize = 3;

    /** Offset of the position data. */
    public int mPositionOffset = 0;

    /** Offset of the color data. */
    public int mColorOffset = 3;

    /** Size of the color data in elements. */
    public int mColorDataSize = 4;

    /** Buffer contains vertices */
    public FloatBuffer vertices;

    /*************************************************************************************************
     * Init:
     * param verticesData: the vertice data in this format: // X, Y, Z,
                                                            // R, G, B, A
     *************************************************************************************************/
    public void Init(float[] verticesData)
    {
        // Initialize the buffers.------------------------------------------------//
        vertices = ByteBuffer.allocateDirect(verticesData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        vertices.put(verticesData).position(0);
    }
}
