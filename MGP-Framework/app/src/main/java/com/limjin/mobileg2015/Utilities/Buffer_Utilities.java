package com.limjin.mobileg2015.Utilities;

/**
 * Created by tanyiecher on 13/5/2016.
 */

import android.opengl.GLES20;
import android.opengl.GLUtils;

import com.limjin.mobileg2015.Mesh;
import com.limjin.mobileg2015.MeshMan;

/*********************************************************************************************
 * Some utilities and variables for buffers etc.
 *********************************************************************************************/
public class Buffer_Utilities
{
    /* OpenGL es extensions */
    public static final int DEPTH_STENCIL_OES                          = 0x84F9;
    public static final int DEPTH24_STENCIL8_OES                       = 0x88F0;
    public static final int UNSIGNED_INT_24_8_OES                        = 0x84FA;

    /*************************************************************************************************
     * Combine buffers: ASSUMES BUFFER HAVE THE FOLLOWING
     *************************************************************************************************/
    public static float[] combinedBuffer(float[][] buffers, int[] attributes)
    {
        /* total elements per vertex */
        int total_Elements = 0;
        for(int i = 0; i < attributes.length; ++i)
            total_Elements += MeshMan.DataSize_Attrib[attributes[i]];

        /* total vertices */
        int total_vert = buffers[0].length / MeshMan.DataSize_Attrib[attributes[0]];

        /* combined float buffer */
        float[] combined = new float[total_Elements * total_vert];

        /* count array */
        int[] dataCount = new int[attributes.length];
        int comCounter = 0;

        //For each vertex of combined buffer--------------------//
        for(int i = 0; i < total_vert; ++i)
        {
            //for each data type of vertex----------------------------------//
            for(int j = 0; j < attributes.length; ++j)
            {
                //for each value of data ype------------------------------------//
                for(int k = 0; k < MeshMan.DataSize_Attrib[attributes[j]]; ++k)
                {
                   combined[comCounter++] = buffers[j][dataCount[j]++];
                }
            }
        }

        return combined;
    }

    /*************************************************************************************************
     * enerates a texture that is suited for attachments to a framebuffer
     * add depth and stencil (FUTURE)
     *************************************************************************************************/
    public static int[] generateAttachmentTexture(int screenWidth, int screenHeight)
    {
        int[] textureID = new int[1];
        GLES20.glGenTextures(1, textureID, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureID[0]);
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGB, screenWidth, screenHeight, 0, GLES20.GL_RGB, GLES20.GL_UNSIGNED_BYTE, null);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);

        return textureID;
    }
}
