package com.limjin.mobileg2015.Utilities;

/**
 * Created by tanyiecher on 10/5/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES30;
import android.opengl.GLUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/*********************************************************************************************
 * Some utilities necessary but dunno put where
 *********************************************************************************************/
public class MiscUtilities
{
    /*********************************************************************************************
     * Load in texture from resource ID
     *********************************************************************************************/
    public static int loadTexture(final Context context, final int resourceId)
    {
        //Create and generate texture handle--------------------------------//
        final int[] textureHandle = new int[1];
        GLES30.glGenTextures(1, textureHandle, 0);  //can create multiple, but we need just 1

        if (textureHandle[0] != 0)
        {
            //Setup----------------------------------------------------------------------------//
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;   // No pre-scaling

            //Get texture in format OpenGL understands-----------------------------------------//
            // Read in the resource
            final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

            // Bind to the texture in OpenGL (It is now the 'main' texture represented by GL_TEXTURE_2D)
            GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, textureHandle[0]);

            // Set filtering
            //GL_TEXTURE_***_FILTER: tells openGL what kind of filtering to apply when...
            //====
            //GL_TEXTURE_MIN_FILTER: drawing texture SMALLER than original size in pixels
            //GL_TEXTURE_MIN_FILTER: drawing texture LARGER than original size in pixels
            //====
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_NEAREST);
            GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D, GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_NEAREST);

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GLES30.GL_TEXTURE_2D, 0, bitmap, 0);

            // Recycle the bitmap, since its data has been loaded into OpenGL.
            bitmap.recycle();
        }

        if (textureHandle[0] == 0)
        {
            throw new RuntimeException("Error loading texture.");
        }

        return textureHandle[0];
    }

    /*********************************************************************************************
     * Load in text file like Shaders
     *********************************************************************************************/
    public static String readTextFileFromRawResource(final Context context,
                                                     final int resourceId)
    {
        final InputStream inputStream = context.getResources().openRawResource(
                resourceId);
        final InputStreamReader inputStreamReader = new InputStreamReader(
                inputStream);
        final BufferedReader bufferedReader = new BufferedReader(
                inputStreamReader);

        String nextLine;
        final StringBuilder body = new StringBuilder();

        try
        {
            while ((nextLine = bufferedReader.readLine()) != null)
            {
                body.append(nextLine);
                body.append('\n');
            }
        }
        catch (IOException e)
        {
            return null;
        }

        return body.toString();
    }
}
