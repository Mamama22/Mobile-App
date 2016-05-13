package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 13/5/2016.
 */
/*************************************************************************************************
 * Mesh class: tile based mesh
 *************************************************************************************************/
public class MeshTile extends MeshVBO_combined
{
    private int totalX;
    private int totalY;

    /*************************************************************************************************
     * Init:
     * store X and Y amount of tiles (eg.
     *************************************************************************************************/
    public void Init(float[] combined_data, int xTiles, int yTiles)
    {
        totalX = xTiles;
        totalY = yTiles;


    }

    int GetX(){return totalX;}
    int GetY(){return totalY;}
}
