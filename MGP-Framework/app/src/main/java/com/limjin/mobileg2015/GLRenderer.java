package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 2/5/2016.
 */
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.Matrix;

/*************************************************************************************************
 * Gameloop abstract class, inherit to create your own
 *
 * Note:
 * Need recreate shaders everytime reset?
 *************************************************************************************************/
public abstract class GLRenderer implements Renderer {

    /*************************************************************************************************
     * Variables
     *************************************************************************************************/
    //shaders------------------------------------------//
    protected final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    protected final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public int mProgram = 0;

    // Use to access and set the view transformation
    public int mMVPMatrixHandle = 0;

    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    public final float[] mMVPMatrix = new float[16];
    public final float[] mProjectionMatrix = new float[16];
    public final float[] mViewMatrix = new float[16];

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

        //Need recreate shaders everytime reset?-----------------------//
        onCreate(mWidth, mHeight, mSurfaceCreated);
        mSurfaceCreated = false;

        //Projection-----------------------------------//
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
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

        //Flags---------------------------------------------//
        if (mFirstDraw) {
            mFirstDraw = false;
        }

        //Projection and camera View---------------------------------------------//
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
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
