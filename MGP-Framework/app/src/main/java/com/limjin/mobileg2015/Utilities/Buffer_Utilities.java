package com.limjin.mobileg2015.Utilities;

/**
 * Created by tanyiecher on 13/5/2016.
 */

import com.limjin.mobileg2015.Mesh;
import com.limjin.mobileg2015.MeshMan;

/*********************************************************************************************
 * Some utilities and variables for buffers etc.
 *********************************************************************************************/
public class Buffer_Utilities
{
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

//    /*************************************************************************************************
//     * Combine buffers: ASSUMES BUFFER HAVE THE FOLLOWING
//     *************************************************************************************************/
//    public static float[] combinedBuffer(float[] vertices, float[] color, float[] normal, float[] texcoord)
//    {
//        /* total elements per vertex */
//        int total_Elements = MeshMan.DataSize_Attrib[MeshMan.ATTRIBUTE_VERT] + MeshMan.DataSize_Attrib[MeshMan.ATTRIBUTE_COLOR]
//                + MeshMan.DataSize_Attrib[MeshMan.ATTRIBUTE_NORMAL] + MeshMan.DataSize_Attrib[MeshMan.ATTRIBUTE_TEXTURE];
//
//        /* total vertices */
//        int total_vert = vertices.length / MeshMan.DataSize_Attrib[MeshMan.ATTRIBUTE_VERT];
//
//        /* combined float buffer */
//        float[] combined = new float[total_Elements * total_vert];
//
//        int vertCount = 0, colorCount = 0, normalCount = 0, texCount = 0;
//        for(int i = 0; i < combined.length;)
//        {
//            //Pos-------------------------------------//
//            combined[i++] = vertices[vertCount++];
//            combined[i++] = vertices[vertCount++];
//            combined[i++] = vertices[vertCount++];
//
//            //Color-------------------------------------//
//            combined[i++] = color[colorCount++];
//            combined[i++] = color[colorCount++];
//            combined[i++] = color[colorCount++];
//            combined[i++] = color[colorCount++];
//
//            //Normal-------------------------------------//
//            combined[i++] = normal[normalCount++];
//            combined[i++] = normal[normalCount++];
//            combined[i++] = normal[normalCount++];
//
//            //Tex-------------------------------------//
//            combined[i++] = texcoord[texCount++];
//            combined[i++] = texcoord[texCount++];
//        }
//
//        return combined;
//    }
//
//    /*************************************************************************************************
//     * Combine buffers: ASSUMES BUFFER HAVE THE FOLLOWING
//     *************************************************************************************************/
//    public static float[] combinedBuffer2(float[] vertices, float[] color, float[] normal)
//    {
//        /* total elements per vertex */
//        int total_Elements = MeshMan.DataSize_Attrib[MeshMan.ATTRIBUTE_VERT] + MeshMan.DataSize_Attrib[MeshMan.ATTRIBUTE_COLOR]
//                + MeshMan.DataSize_Attrib[MeshMan.ATTRIBUTE_NORMAL];
//
//        /* total vertices */
//        int total_vert = vertices.length / MeshMan.DataSize_Attrib[MeshMan.ATTRIBUTE_VERT];
//
//        /* combined float buffer */
//        float[] combined = new float[total_Elements * total_vert];
//
//        int vertCount = 0, colorCount = 0, normalCount = 0;
//        for(int i = 0; i < combined.length;)
//        {
//            //Pos-------------------------------------//
//            combined[i++] = vertices[vertCount++];
//            combined[i++] = vertices[vertCount++];
//            combined[i++] = vertices[vertCount++];
//
//            //Color-------------------------------------//
//            combined[i++] = color[colorCount++];
//            combined[i++] = color[colorCount++];
//            combined[i++] = color[colorCount++];
//            combined[i++] = color[colorCount++];
//
//            //Normal-------------------------------------//
//            combined[i++] = normal[normalCount++];
//            combined[i++] = normal[normalCount++];
//            combined[i++] = normal[normalCount++];
//        }
//
//        return combined;
//    }
}
