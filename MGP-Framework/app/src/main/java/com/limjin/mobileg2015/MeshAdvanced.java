package com.limjin.mobileg2015;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by tanyiecher on 7/5/2016.
 */
public class MeshAdvanced extends Mesh
{
    /* Buffer for color and normal */
    public FloatBuffer color_buffer;
    public FloatBuffer normal_buffer;

    /** Size of the normal data in elements. */
    public final int mNormalDataSize = 3;

    /*************************************************************************************************
     * Init:
     * param verticesData: the vertice data in this format: // X, Y, Z,
     // R, G, B, A
     *************************************************************************************************/
    public void Init(float[] verticesData, float[] colorData, float[] normalData)
    {
        //change some data----------------------------------------------------//
        mStrideBytes = 3 * mBytesPerFloat;  //XYZ/RGB/normal

        // Initialize the buffers.------------------------------------------------//
        super.Init(verticesData);

        color_buffer = ByteBuffer.allocateDirect(colorData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        normal_buffer = ByteBuffer.allocateDirect(normalData.length * mBytesPerFloat)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        color_buffer.put(colorData).position(0);
        normal_buffer.put(normalData).position(0);
    }
}
