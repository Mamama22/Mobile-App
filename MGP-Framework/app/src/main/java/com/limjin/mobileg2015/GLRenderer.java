package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 2/5/2016.
 */
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*************************************************************************************************
 * Gameloop abstract class, inherit to create your own
 *************************************************************************************************/
public abstract class GLRenderer implements Renderer {

    /*************************************************************************************************
     * Variables
     *************************************************************************************************/
    //shaders------------------------------------------//
    protected final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    protected final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public int mProgram = 0;

    //Flags----------------------------//
    private boolean mFirstDraw;
    private boolean mSurfaceCreated;

    //Info----------------------------//
    private int mWidth;
    private int mHeight;

    //FPS------------------------------//
    private long mLastTime;
    private int mFPS;

    /*************************************************************************************************
     * Constructor
     *************************************************************************************************/
    public GLRenderer() {
        mFirstDraw = true;
        mSurfaceCreated = false;
        mWidth = -1;
        mHeight = -1;
        mLastTime = System.currentTimeMillis();
        mFPS = 0;
    }

    /*************************************************************************************************
     * Load shader
     *************************************************************************************************/
    public static int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }

    /*************************************************************************************************
     * Init shaders
     *************************************************************************************************/
    private void initShaders() {

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);
    }

    /*************************************************************************************************
     * Necessary abstract methods
     *************************************************************************************************/
    public void onSurfaceCreated(GL10 notUsed,
                                 EGLConfig config) {

        mSurfaceCreated = true;
        mWidth = -1;
        mHeight = -1;
    }

    @Override
    public void onSurfaceChanged(GL10 notUsed, int width, int height)
    {
        if (!mSurfaceCreated && width == mWidth && height == mHeight)
            return;

        mWidth = width;
        mHeight = height;

        onCreate(mWidth, mHeight, mSurfaceCreated);
        mSurfaceCreated = false;
    }

    @Override
    public void onDrawFrame(GL10 notUsed) {
        onDrawFrame(mFirstDraw);

        //cal FPS-----------------------------//
        mFPS++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - mLastTime >= 1000) {
            mFPS = 0;
            mLastTime = currentTime;
        }

        if (mFirstDraw) {
            mFirstDraw = false;
        }
    }

    public int getFPS() {return mFPS;}

    /*************************************************************************************************
     * Implement these and call super if applicable!!!
     *************************************************************************************************/
    public void onCreate(int width, int height,
                                  boolean contextLost)
    {
        //init shaders------------------------------//
        initShaders();
    }

    public abstract void onDrawFrame(boolean firstDraw);
}
