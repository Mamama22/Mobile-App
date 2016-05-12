package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 12/5/2016.
 */

import android.opengl.GLES20;

import java.nio.FloatBuffer;

/*************************************************************************************************
 * Mesh class: uses VBO method combined buffer
 *************************************************************************************************/
public class MeshVBO_combined extends Mesh
{
    /** How many elements per vertex. */
    public final int mStride = (mPositionDataSize + mColorDataSize + mNormalDataSize + mTextureCoordinateDataSize) * mBytesPerFloat;

    public int BufferIdx = 0;

    /** Buffer contains vertices */
    public FloatBuffer combinedBuffer;

    /*************************************************************************************************
     * Init:
     * Store buffers with VBO method
     *************************************************************************************************/
    public void Init(float[] combined_data)
    {
        //Setup combined buffer-----------------------------//
        combinedBuffer = SetupBuffer(combined_data);

        // Second, copy these buffers into OpenGL's memory. After, we don't need to keep the client-side buffers around.
        final int buffers[] = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);

        // Transfer data from client memory to the buffer.
        // We can release the client memory after this call.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, combinedBuffer.capacity() * mBytesPerFloat, combinedBuffer, GLES20.GL_STATIC_DRAW);

        // IMPORTANT: Unbind from the buffer when we're done with it.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        //index/handle to buffers in GPU---------------------------------------//
        BufferIdx = buffers[0];

        //release client side buffers-----------------------------------------//
        combinedBuffer.limit(0);
        combinedBuffer = null;
    }

    /*************************************************************************************************
     * Binding etc. before transformation
     *************************************************************************************************/
    void PreRender(int posHand, int colorHand, int normalHand, int texHand)
    {
        // Pass in the position information--------------------------------------------//
        int startFrom = 0;
        Bind_buffer_to_VBO(BufferIdx, mPositionDataSize, posHand, mStride, startFrom);

        startFrom += mPositionDataSize * mBytesPerFloat;
        Bind_buffer_to_VBO(BufferIdx, mColorDataSize, colorHand, mStride, startFrom);

        startFrom += mColorDataSize * mBytesPerFloat;
        Bind_buffer_to_VBO(BufferIdx, mNormalDataSize, normalHand, mStride, startFrom);

        startFrom += mNormalDataSize * mBytesPerFloat;
        Bind_buffer_to_VBO(BufferIdx, mTextureCoordinateDataSize, texHand, mStride, startFrom);

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }
}
