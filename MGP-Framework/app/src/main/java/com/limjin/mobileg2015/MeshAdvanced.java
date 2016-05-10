package com.limjin.mobileg2015;

import android.content.Context;

import com.limjin.mobileg2015.Utilities.MiscUtilities;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by tanyiecher on 7/5/2016.
 */
public class MeshAdvanced extends Mesh
{
    /* Important variables */
    public int Texture_Handle = 0;  //points to texture

    /* Buffer for color and normal */
    public FloatBuffer color_buffer;
    public FloatBuffer normal_buffer;
    public FloatBuffer texCoord_buffer;

    /** Size of the normal data in elements. */
    public final int mNormalDataSize = 3;

    /** Size of the texture coordinate data in elements. */
    public final int mTextureCoordinateDataSize = 2;

    /*************************************************************************************************
     * Init:
     * param verticesData: the vertice data in this format: // X, Y, Z,
     // R, G, B, A
     *************************************************************************************************/
    public void Init(float[] verticesData, float[] colorData, float[] normalData, float[] texcoordData)
    {
        //change some data----------------------------------------------------//
        mStrideBytes = 3 * mBytesPerFloat;  //XYZ/RGB/normal

        // Initialize the buffers.------------------------------------------------//
        super.Init(verticesData);

        color_buffer = ByteBuffer.allocateDirect(colorData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        normal_buffer = ByteBuffer.allocateDirect(normalData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        texCoord_buffer = ByteBuffer.allocateDirect(texcoordData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        color_buffer.put(colorData).position(0);
        normal_buffer.put(normalData).position(0);
        texCoord_buffer.put(texcoordData).position(0);
    }

    /*************************************************************************************************
     * Load texture in
     *************************************************************************************************/
    public void LoadTexture(Context context, int resourceID)
    {
        Texture_Handle = MiscUtilities.loadTexture(context, resourceID);
    }
}
