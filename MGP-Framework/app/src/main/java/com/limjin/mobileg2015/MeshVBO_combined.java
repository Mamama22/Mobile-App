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
    //(mPositionDataSize + mColorDataSize + mNormalDataSize + mTextureCoordinateDataSize) * mBytesPerFloat
    public int mStride = 0;

    public int BufferIdx = 0;

    /** Buffer contains vertices */
    public FloatBuffer combinedBuffer;

    /*************************************************************************************************
     * Init:
     * Store buffers with VBO method
     * Attributes: list of attributes possible in shader
     * combined_data: buffer with data arranged in the ORDER SPECIFIED IN ATTRIBUTES
     *************************************************************************************************/
    public void Init(int[] _attributes, float[] combined_data)
    {
        Attributes = _attributes;
        for(int i = 0; i < Attributes.length; ++i)
        {
            mStride += MeshMan.DataSize_Attrib[Attributes[i]];
        }
        mStride *= MeshMan.mBytesPerFloat;

        //Setup combined buffer-----------------------------//
        combinedBuffer = SetupBuffer(combined_data);

        // Second, copy these buffers into OpenGL's memory. After, we don't need to keep the client-side buffers around.
        final int buffers[] = new int[1];
        GLES20.glGenBuffers(1, buffers, 0);

        // Transfer data from client memory to the buffer.
        // We can release the client memory after this call.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, combinedBuffer.capacity() * MeshMan.mBytesPerFloat, combinedBuffer, GLES20.GL_STATIC_DRAW);

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
    void PreRender(int[] AttribHandles)
    {
        int startFrom = 0;
        for(int i = 0; i < Attributes.length; ++i)
        {
            Bind_buffer_to_VBO(BufferIdx, MeshMan.DataSize_Attrib[Attributes[i]], AttribHandles[Attributes[i]], mStride, startFrom);
            startFrom += MeshMan.DataSize_Attrib[Attributes[i]] * MeshMan.mBytesPerFloat;
        }

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }
}
