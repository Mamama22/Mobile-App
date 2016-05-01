package com.limjin.mobileg2015;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class SpriteAnimation {
    private Bitmap bitmap; // The animation sequence
    private Rect sourceRect; // The rectangle to be drawn from the animation bitmap
    private int frame; // Number of frames in animation
    private int currentFrame; // The current frame
    private long frameTicker; // The time of the last frame update
    private int framePeriod; // milliseconds between each frame(1000/fps)

    private int spriteWidth; // The width  of the sprite to calculate the cutout rectangle
    private int spriteHeight; // The height of the sprite

    private int x; // The x-coordinate of the object (top left of image)
    private int y; // The y-coorindate of the object (top left of image)

    public SpriteAnimation(Bitmap bitmap, int x, int y, int fps, int frameCount){
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;

        currentFrame = 0;

        frame = frameCount;

        spriteWidth = bitmap.getWidth() / frameCount; // Assumed that each frame has the same width
        spriteHeight = bitmap.getHeight();

        sourceRect = new Rect(0, 0, spriteWidth, spriteHeight);

        framePeriod = 1000 / fps;
        frameTicker = 0;
    }

    public  void setBitmap(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public Bitmap getBitmap(){
        return bitmap;
    }

    public void setSourceRect(Rect sourceRect){
        this.sourceRect = sourceRect;
    }

    public Rect getSourceRect(){
        return sourceRect;
    }

    public void setFrame(int frame){
        this.frame = frame;
    }

    public int getFrame(){
        return frame;
    }

    public void setCurrentFrame(int currentFrame){
        this.currentFrame = currentFrame;
    }

    public int getCurrentFrame(){
        return currentFrame;
    }

    public void setFramePeriod(int framePeriod){
        this.framePeriod = framePeriod;
    }

    public int getFramePeriod(){
        return framePeriod;
    }

    public void setSpriteWidth(int spriteWidth){
        this.spriteWidth = spriteWidth;
    }

    public int getSpriteWidth(){
        return spriteWidth;
    }

    public void setSpriteHeight(int spriteHeight){
        this.spriteHeight = spriteHeight;
    }

    public int getSpriteHeight(){
        return spriteHeight;
    }

    public void setX(int x){
        this.x = x;
    }

    public int getX(){
        return x;
    }

    public void setY(int y){
        this.y = y;
    }

    public int getY(){
        return y;
    }

    public void Update(long gameTime) // gameTime = system time from the thread which is running
    {
        if(gameTime > frameTicker + framePeriod)
        {
            frameTicker = gameTime;
            currentFrame++; // Increment the frame

            if(currentFrame >= frame) // frame = total number of frames and currentFrame starts from 0
            {
                currentFrame = 0; // Reached end of frame, reset to 0
            }
        }

        // Define the rectangle to cut out sprite
        this.sourceRect.left = currentFrame * spriteWidth;
        this.sourceRect.right = this.sourceRect.left + spriteWidth;
    }

    public void Draw(Canvas canvas)
    {
        // Image of each frame is defined by sourceRect
        // destRect is the area defined for the image of each frame to be drawn

        Rect destRect = new Rect(getX(), getY(), getX() + spriteWidth, getY() + spriteHeight);
        canvas.drawBitmap(bitmap, sourceRect, destRect, null);
    }
}
