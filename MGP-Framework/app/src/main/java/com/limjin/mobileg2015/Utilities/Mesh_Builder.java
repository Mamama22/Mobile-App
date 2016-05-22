package com.limjin.mobileg2015.Utilities;

/**
 * Created by tanyiecher on 13/5/2016.
 */

import com.limjin.mobileg2015.MeshMan;
import com.limjin.mobileg2015.MeshVBO_combined;

/*********************************************************************************************
 * Generate meshes
 *********************************************************************************************/
public class Mesh_Builder
{
    /*************************************************************************************************
     * Cube
     *************************************************************************************************/
    // Define points for a cube.

    // X, Y, Z
    final static float[] cubePositionData =
            {
                    // In OpenGL counter-clockwise winding is default. This means that when we look at a triangle,
                    // if the points are counter-clockwise we are looking at the "front". If not we are looking at
                    // the back. OpenGL has an optimization where all back-facing triangles are culled, since they
                    // usually represent the backside of an object and aren't visible anyways.

                    // Front face
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,

                    // Right face
                    1.0f, 1.0f, 1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    1.0f, -1.0f, -1.0f,
                    1.0f, 1.0f, -1.0f,

                    // Back face
                    1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,
                    1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,

                    // Left face
                    -1.0f, 1.0f, -1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
                    -1.0f, -1.0f, 1.0f,
                    -1.0f, 1.0f, 1.0f,

                    // Top face
                    -1.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, 1.0f,
                    1.0f, 1.0f, -1.0f,
                    -1.0f, 1.0f, -1.0f,
                    1.0f, 1.0f, 1.0f,

                    // Bottom face
                    1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
                    1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, 1.0f,
                    -1.0f, -1.0f, -1.0f,
            };

    // R, G, B, A
    final static float[] cubeColorData =
            {
                    // Front face (red)
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,
                    1.0f, 0.0f, 0.0f, 1.0f,

                    // Right face (green)
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,
                    0.0f, 1.0f, 0.0f, 1.0f,

                    // Back face (blue)
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,
                    0.0f, 0.0f, 1.0f, 1.0f,

                    // Left face (yellow)
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,
                    1.0f, 1.0f, 0.0f, 1.0f,

                    // Top face (cyan)
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,
                    0.0f, 1.0f, 1.0f, 1.0f,

                    // Bottom face (magenta)
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f,
                    1.0f, 0.0f, 1.0f, 1.0f
            };

    // X, Y, Z
    // The normal is used in light calculations and is a vector which points
    // orthogonal to the plane of the surface. For a cube model, the normals
    // should be orthogonal to the points of each face.
    final static float[] cubeNormalData =
            {
                    // Front face
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,
                    0.0f, 0.0f, 1.0f,

                    // Right face
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,
                    1.0f, 0.0f, 0.0f,

                    // Back face
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,
                    0.0f, 0.0f, -1.0f,

                    // Left face
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,
                    -1.0f, 0.0f, 0.0f,

                    // Top face
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,
                    0.0f, 1.0f, 0.0f,

                    // Bottom face
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
                    0.0f, -1.0f, 0.0f,
            };

    // S, T (or X, Y)
    // Texture coordinate data.
    // Because images have a Y axis pointing downward (values increase as you move down the image) while
    // OpenGL has a Y axis pointing upward, we adjust for that here by flipping the Y axis.
    // What's more is that the texture coordinates are the same for every face.
    final static float[] TextureCoordinateData =
            {
                    // Front face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Front face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Front face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Front face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Front face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,

                    // Front face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
            };

    /*************************************************************************************************
     * Quad
     *************************************************************************************************/
    final static float[] quad_PositionData =
            {
                    // Front face
                    -0.5f, 0.5f, 1.0f,
                    -0.5f, -0.5f, 1.0f,
                    0.5f, 0.5f, 1.0f,
                    -0.5f, -0.5f, 1.0f,
                    0.5f, -0.5f, 1.0f,
                    0.5f, 0.5f, 1.0f,
            };

    final static float[] quadFBO_TextureCoordinateDataFliped =
            {
                    // Front face
                    0.0f, 1.0f,
                    0.0f, 0.0f,
                    1.0f, 1.0f,
                    0.0f, 0.0f,
                    1.0f, 0.0f,
                    1.0f, 1.0f,
            };

    final static float[] quad_PositionData_startLeft =
            {
                    // Front face
                    0.f, 1.f, 1.0f,
                    0.f, 0.f, 1.0f,
                    1.f, 1.f, 1.0f,
                    0.f, 0.f, 1.0f,
                    1.f, 0.f, 1.0f,
                    1.f, 1.f, 1.0f,
            };

    final static float[] quadFBO_TextureCoordinateData =
            {
                    // Front face
                    0.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 0.0f,
                    0.0f, 1.0f,
                    1.0f, 1.0f,
                    1.0f, 0.0f,
            };

    /*************************************************************************************************
     * Generate a copy of the array
     *************************************************************************************************/
    private static float[] CopyArray(float[] copyMe, int times)
    {
        float[] returnMe = new float[copyMe.length * times];
        int count = 0;

        for(int k = 0; k < times; ++k)
        {
            for (int i = 0; i < copyMe.length; ++i)
            {
                returnMe[count++] = copyMe[i];
            }
        }

        return returnMe;
    }

    /*************************************************************************************************
    * Utilities: Gen Attributes:
     * 0 = vert, 1 = normal, 2 = color, 3 (or 2 if no color) = tex
     *************************************************************************************************/
    private static int[] GenAttributes(int color_ID, int texture_ID)
    {
        int[] attributes_List;
        int totalDataTypes = 2;
        if(color_ID != MeshMan.COLOR_NONE)
        {
            totalDataTypes++;
        }
        if(texture_ID != MeshMan.TEX_NONE)
        {
            totalDataTypes++;
        }

        attributes_List = new int[totalDataTypes];

        int count = 0;
        attributes_List[count++] = MeshMan.ATTRIBUTE_VERT;
        attributes_List[count++] = MeshMan.ATTRIBUTE_NORMAL;

        if(color_ID != MeshMan.COLOR_NONE)
            attributes_List[count++] = MeshMan.ATTRIBUTE_COLOR;
        if(texture_ID != MeshMan.TEX_NONE)
            attributes_List[count++] = MeshMan.ATTRIBUTE_TEXTURE;

        return attributes_List;
    }

    /*************************************************************************************************
     * Cube Generator: if texture provided, color and alpha will not be counted
     *************************************************************************************************/
    public static MeshVBO_combined GenerateCube(int color_ID, int texture_ID)
    {
        /** Attributes-----------------------------------------------------------**/
        int[] attributes = GenAttributes(color_ID, texture_ID);


        int totalBufferDataType = 2;    //vert and normal

        //check if got color
        if(color_ID != MeshMan.COLOR_NONE) {
            totalBufferDataType++;
        }
        if(texture_ID != MeshMan.TEX_NONE) {
            totalBufferDataType++;
        }

        //populate buffer (FOLLOW THE ORDER IN ATTRUBTES)------------------------------------//
        float[][] bufferList = new float[totalBufferDataType][];
        int count = 0;
        bufferList[count++] = cubePositionData;
        bufferList[count++] = cubeNormalData;

        if(color_ID != MeshMan.COLOR_NONE)
            bufferList[count++] = cubeColorData;
        if(texture_ID != MeshMan.TEX_NONE)
            bufferList[count++] = TextureCoordinateData;

        //Combine buffer------------------------------------------------------------//
        float[] combinedBuffer = Buffer_Utilities.combinedBuffer(bufferList, attributes);

        //Create mesh--------------------------------------------------------------//
        MeshVBO_combined mesh = new MeshVBO_combined();
        mesh.Init(attributes, combinedBuffer);

        mesh.Texture_Handle = MeshMan.textureID_List[texture_ID];

        return mesh;
    }

    /*************************************************************************************************
     * Quad for FBO Generator: if texture provided, color and alpha will not be counted
     *************************************************************************************************/
    public static MeshVBO_combined GenerateQuad_FBO(int texture_ID)
    {
        /** Attributes-----------------------------------------------------------**/
        int[] attributes = new int[2];
        attributes[0] = MeshMan.ATTRIBUTE_VERT;
        attributes[1] = MeshMan.ATTRIBUTE_TEXTURE;

        //populate buffer (FOLLOW THE ORDER IN ATTRUBTES)------------------------------------//
        float[][] bufferList = new float[2][];
        bufferList[0] = quad_PositionData;
        bufferList[1] = quadFBO_TextureCoordinateDataFliped;

        //Combine buffer------------------------------------------------------------//
        float[] combinedBuffer = Buffer_Utilities.combinedBuffer(bufferList, attributes);

        //Create mesh--------------------------------------------------------------//
        MeshVBO_combined mesh = new MeshVBO_combined();
        mesh.Init(attributes, combinedBuffer);

        mesh.Texture_Handle = MeshMan.textureID_List[texture_ID];

        return mesh;
    }

    /*************************************************************************************************
     * Quad for FBO Generator: tilemap
     *************************************************************************************************/
    public static MeshVBO_combined GenerateTilemap_FBO(int texture_ID, int xNum, int yNum)
    {
        /** Attributes-----------------------------------------------------------**/
        int[] attributes = new int[2];
        attributes[0] = MeshMan.ATTRIBUTE_VERT;
        attributes[1] = MeshMan.ATTRIBUTE_TEXTURE;

        //populate buffer (FOLLOW THE ORDER IN ATTRUBTES)------------------------------------//
        int total = xNum * yNum;
        float[][] bufferList = new float[2][];
        bufferList[0] = CopyArray(quad_PositionData_startLeft, total);
        bufferList[1] = CopyArray(quadFBO_TextureCoordinateData, total);

        int tCount = 0;
        float xLength = 1.f / xNum; //total lenght per unit X
        float yLength = 1.f / yNum;

        for(int y = 0; y < yNum; ++y)
        {
            for(int x = 0; x < xNum; ++x)
            {
                //tex---------------------------------------//
                for(int i = 0; i < quadFBO_TextureCoordinateData.length; i += 2)
                {
                    //for example: ori value is 0.f and X count is 0, xLen * 0 + xLen * 0
                    bufferList[1][tCount + 0] = (xLength * x) + (xLength * bufferList[1][tCount + 0]);
                    bufferList[1][tCount + 1] = (yLength * y) + (yLength * bufferList[1][tCount + 1]);
                    tCount += 2;
                }
            }
        }

        //Combine buffer------------------------------------------------------------//
        float[] combinedBuffer = Buffer_Utilities.combinedBuffer(bufferList, attributes);

        //Create mesh--------------------------------------------------------------//
        MeshVBO_combined mesh = new MeshVBO_combined();
        mesh.Init(attributes, combinedBuffer);

        mesh.Texture_Handle = MeshMan.textureID_List[texture_ID];

        return mesh;
    }
}
