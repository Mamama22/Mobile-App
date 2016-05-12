package com.limjin.mobileg2015;

import android.opengl.GLES20;
import java.nio.FloatBuffer;

/**
 * Created by tanyiecher on 12/5/2016.
 */
/*************************************************************************************************
 * Mesh class: uses VBO method
 *************************************************************************************************/
public class MeshVBO extends Mesh
{
    /** Buffer contains vertices */
    public FloatBuffer vertices;
    public FloatBuffer color_buffer;
    public FloatBuffer normal_buffer;
    public FloatBuffer texCoord_buffer;

    /** index for buffers in GPU */
    public int PositionsBufferIdx = 0;
    public int ColorBufferIdx = 0;
    public int NormalsBufferIdx = 0;
    public int TexCoordsBufferIdx = 0;

    /*************************************************************************************************
     * Init:
     * Store buffers with VBO method
     *************************************************************************************************/
    public void Init(float[] verticesData, float[] colorData, float[] normalData, float[] texcoordData)
    {
        vertices = SetupBuffer(verticesData);
        color_buffer = SetupBuffer(colorData);
        normal_buffer = SetupBuffer(normalData);
        texCoord_buffer = SetupBuffer(texcoordData);

        // Second, copy these buffers into OpenGL's memory. After, we don't need to keep the client-side buffers around.
        final int buffers[] = new int[4];
        GLES20.glGenBuffers(4, buffers, 0);

        // Transfer data from client memory to the buffer.
        // We can release the client memory after this call.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[0]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.capacity() * mBytesPerFloat, vertices, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[1]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, color_buffer.capacity() * mBytesPerFloat, color_buffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[2]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normal_buffer.capacity() * mBytesPerFloat, normal_buffer, GLES20.GL_STATIC_DRAW);

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, buffers[3]);
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texCoord_buffer.capacity() * mBytesPerFloat, texCoord_buffer, GLES20.GL_STATIC_DRAW);

        // IMPORTANT: Unbind from the buffer when we're done with it.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

        //index/handle to buffers in GPU---------------------------------------//
        PositionsBufferIdx = buffers[0];
        ColorBufferIdx = buffers[1];
        NormalsBufferIdx = buffers[2];
        TexCoordsBufferIdx = buffers[3];

        //release client side buffers-----------------------------------------//
        vertices.limit(0);
        vertices = null;
        color_buffer.limit(0);
        color_buffer = null;
        normal_buffer.limit(0);
        normal_buffer = null;
        texCoord_buffer.limit(0);
        texCoord_buffer = null;
    }

    /*************************************************************************************************
     * Binding etc. before transformation
     *************************************************************************************************/
    void PreRender(int posHand, int colorHand, int normalHand, int texHand)
    {
        // Pass in the position information
        Bind_buffer_to_VBO(PositionsBufferIdx, mPositionDataSize, posHand, 0, 0);
        Bind_buffer_to_VBO(ColorBufferIdx, mColorDataSize, colorHand,0, 0);
        Bind_buffer_to_VBO(NormalsBufferIdx, mNormalDataSize, normalHand,0, 0);
        Bind_buffer_to_VBO(TexCoordsBufferIdx,mTextureCoordinateDataSize, texHand,0, 0);

        // Clear the currently bound buffer (so future OpenGL calls do not use this buffer).
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
    }
}
