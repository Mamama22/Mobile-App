package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 13/5/2016.
 */

import com.limjin.mobileg2015.MyTypes.Color;
import com.limjin.mobileg2015.Utilities.Misc_Utilities;

/*************************************************************************************************
 * Mesh manager:
 * View render functions DO NOT TAKE IN MESH OBJECTS,
 * TAKES IN TEXTURE ID/COLOR ID and MESH ID
 *************************************************************************************************/
public class MeshMan
{
    /*************** Mesh ID enum ***************/
    public static final int MESH_CUBE = 0;
    public static final int MESH_QUAD = 1;
    public static final int TOTAL_MESH = MESH_QUAD + 1;

    /*************** Texture ID enum ***************/
    public static final int TEX_NONE = 0;   //no texture? pass this in
    public static final int TEX_FACEBOOK = 1;
    public static final int TEX_FONT_1 = 2;
    public static final int TEX_RECYCLE = 3;
    public static final int TOTAL_TEX = TEX_RECYCLE + 1;

    /*************** Color ID enum ***************/
    public static final int COLOR_NONE = 0;   //no texture? pass this in
    public static final int COLOR_RED = 1;
    public static final int COLOR_GREEN = 2;
    public static final int TOTAL_COLOR = COLOR_GREEN + 1;

    /*************** Attributes ***************/
    public static final int ATTRIBUTE_VERT = 3;
    public static final int ATTRIBUTE_COLOR = 1;
    public static final int ATTRIBUTE_NORMAL = 2;
    public static final int ATTRIBUTE_TEXTURE = 0;
    public static final int TOTAL_ATTRIBUTES = 4;


    /*************** Attributes size bytes ***************/
    public static final int mBytesPerFloat = 4; //How many bytes per float.
    public static final int[] DataSize_Attrib = {2, 4, 3, 3};   //FOLLOW ORDER ABOVE
    public static final String[] DataName_Attrib = {"a_TexCoordinate", "a_Color", "a_Normal", "a_Position"};

    /*************** ID array list ***************/
    public static MeshVBO_combined[] mesh_List;
    public static int[] textureID_List;
    public static Color[] color_List;

    /*************************************************************************************************
     * Init pre-defined values
     *************************************************************************************************/
    public static void Init()
    {
        /* create public list */
        mesh_List = new MeshVBO_combined[TOTAL_MESH];
        textureID_List = new int[TOTAL_TEX];
        color_List = new Color[TOTAL_COLOR];

        /* Mesh */
        //mesh_List[MESH_CUBE]

        /* Textures */
        textureID_List[TEX_NONE] = -1;
        textureID_List[TEX_FACEBOOK] = Misc_Utilities.loadTexture(Misc_Utilities.GetCurrentContext(),
                R.drawable.com_facebook_button_icon_blue);
        textureID_List[TEX_FONT_1] = Misc_Utilities.loadTexture(Misc_Utilities.GetCurrentContext(),
                R.drawable.ar_christy);
        textureID_List[TEX_RECYCLE] = Misc_Utilities.loadTexture(Misc_Utilities.GetCurrentContext(),
                R.drawable.rubbish);
    }
}
