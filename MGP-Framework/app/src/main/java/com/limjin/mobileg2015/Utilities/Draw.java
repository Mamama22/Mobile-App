package com.limjin.mobileg2015.Utilities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.DisplayMetrics;
import com.limjin.mobileg2015.Vector2;

/*********************************************************************************************
 * More like a view + input utility, but name wrongly fuk it
 *********************************************************************************************/
public class Draw
{
    //Size of screen depending on phone-------------------------------//
    public static int ScreenWidth;
    public static int ScreenHeight;

    //View space coordinates----------------------------------------//
    public static float ViewWidth;
    public static float ViewHeight;

    //Pixels per unit---------------------------------//
    public static float unitX;
    public static float unitY;

    //Units per pixel---------------------------------//
    public static float convertX;
    public static float convertY;
    public static Paint paint;

    //FPS---------------------------------//
    public static float FPS;

    public static Vector2 top = new Vector2();


    //use this canvas so no need pass around by value?
    public static Canvas canvas = null;

    /*********************************************************************************************
     * Set up screen coords and other stuff
     *********************************************************************************************/
    public static void Set(float ViewWidth, float ViewHeight, Context context)
    {
        paint = new Paint();

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        Draw.ViewWidth = ViewWidth;
        Draw.ViewHeight = ViewHeight;
        Draw.unitX = (float)ScreenWidth / ViewWidth;
        Draw.unitY = (float)ScreenHeight / ViewHeight;
        convertX = ViewWidth / (float)ScreenWidth;
        convertY = ViewHeight / (float)ScreenHeight;
    }

    /*********************************************************************************************
     * Initialize bitmap to scale accordingly with screen: View --> Screen
     *********************************************************************************************/
    public static Bitmap SetBitmap(int xScale, int yScale, int resourceID, Resources res)
    {
        Bitmap mm = BitmapFactory.decodeResource(res, resourceID);
        mm = Bitmap.createScaledBitmap(mm, RealX(xScale), RealY(yScale), true);
        return mm;
    }

    public static Bitmap SetBitmap(float xScale, float yScale, int resourceID, Resources res)
    {
        Bitmap mm = BitmapFactory.decodeResource(res, resourceID);
        mm = Bitmap.createScaledBitmap(mm, (int)RealX(xScale), (int)RealY(yScale), true);
        return mm;
    }

    /*********************************************************************************************
     * Draw: view to WORLD: View --> Screen + cam offset
     *********************************************************************************************/
    public static void DrawBitmap(Bitmap bit, Vector2 pos, Vector2 scale)
    {
        //Param: bitmap, left, top
        Draw.canvas.drawBitmap(bit, RealX(pos.x),
                ScreenHeight - (RealY(pos.y + scale.y)), null);
    }

    /*********************************************************************************************
     * Draw text:
     * note: scaleY is the scale factor for both X and Y
     *********************************************************************************************/
    public static void DrawText(String word, float posX, float posY, float scaleY)
    {
        Draw.paint.setARGB(255, 255, 0, 0);
        Draw.paint.setStyle(Paint.Style.FILL);
        Draw.paint.setStrokeWidth(RealX(120)); // how thick you want the text to be in terms of pixel
        Draw.paint.setTextSize(scaleY);
        Draw.canvas.drawText(word, RealX(posX), ScreenHeight - RealX(posY), Draw.paint);
    }

    /*********************************************************************************************
     * Pre Update: call every frame
     *********************************************************************************************/
    public static void PreUpdate()
    {
       //dragVel.SetZero();
    }

    //screen space to view space-------------------//
    public static float RealX(float x){return x * unitX;}
    public static float RealY(float y){return y * unitY;}
    public static int RealX(int x){return (int)((float)x * unitX);}
    public static int RealY(int y){return (int)((float)y * unitY);}

    //view space to screen space--------------------//
    public static float ConvertX(float x){return x * convertX;}
    public static float ConvertY(float y){return y * convertY;}
}
