package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 5/5/2016.
 */
import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/*************************************************************************************************
 * Mesh class: loads mesh into it
 *************************************************************************************************/
public abstract class Mesh
{
    /* Important variables */
    public int Texture_Handle = MeshMan.TEX_NONE;  //points to texture
    protected int[] Attributes;

    /*************************************************************************************************
     * Assign buffer with native mem utility
     *************************************************************************************************/
    protected static FloatBuffer SetupBuffer(float[] data)
    {
        //1) Initialize the buffers.------------------------------------------------//
        //.......................................................//
        // Allocate a direct block of memory on the native heap,
        // size in bytes is equal to cubePositions.length * BYTES_PER_FLOAT.
        // BYTES_PER_FLOAT is equal to 4, since a float is 32-bits, or 4 bytes.
        //-------------------------------------------------------//
        // Floats can be in big-endian or little-endian order.
        // We want the same as the native platform.
        //.......................................................//
        FloatBuffer theBuffer = ByteBuffer.allocateDirect(data.length * MeshMan.mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        //2) Copy data from the Java heap to the native heap.
        theBuffer.put(data);
        theBuffer.position(0); //3) Reset the buffer position to the beginning of the buffer.
        return theBuffer;
    }

    //public void LoadTexture(Context context, int resourceID)
    //{
    //    Texture_Handle = Misc_Utilities.loadTexture(context, resourceID);
    //}

    abstract void PreRender(int[] AttribHandles);

    /*************************************************************************************************
     * Bind whole buffer to VBO
     * param 1: the buffer index in GPU
     * param 2: data size (eg. texCoord = 2)
     * param 3: the buffer handle to corresponding attribute in shader
     *************************************************************************************************/
    protected static void Bind_buffer_to_VBO(int bufferIndex, int dataSize, int bufferHandle, int stride, int startFromByte)
    {
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, bufferIndex);
        GLES20.glEnableVertexAttribArray(bufferHandle);

        //glVertexAttribPointer params:------//
        //1) mPositionHandle: The OpenGL index of the position attribute of our shader program.
        //2) POSITION_DATA_SIZE: How many elements (floats) define this attribute. (texCoord == 2 floats)
        //3) GL_FLOAT: The type of each element.
        //4) false: Should fixed-point data be normalized? Not applicable since we are using floating-point data.
        //5) stride: set to 0 means read all elements sequentially (eg. pos, pos, pos) (BYTES)
        //6) mCubePositions: The pointer to our buffer, containing all of the positional data.
        GLES20.glVertexAttribPointer(bufferHandle, dataSize, GLES20.GL_FLOAT, false, stride, startFromByte);
    }

    /*************************************************************************************************
     * Set texture
     *************************************************************************************************/
    public void SetTexture(int textureID)
    {
        Texture_Handle = textureID;
    }
}
