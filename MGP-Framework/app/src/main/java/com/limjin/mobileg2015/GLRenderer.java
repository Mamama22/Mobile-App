package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 2/5/2016.
 */
import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

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
    protected final static String vertexShader =
            "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.

                    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.

                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"
                    + "   v_Color = a_Color;          \n"     // Pass the color through to the fragment shader.
                    // It will be interpolated across the triangle.
                    + "   gl_Position = u_MVPMatrix   \n"     // gl_Position is a special variable used to store the final position.
                    + "               * a_Position;   \n"     // Multiply the vertex by the matrix to get the final point in
                    + "}                              \n";    // normalized screen coordinates.

    protected final static String fragmentShader =
            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                    + "}                              \n";

    //Handles----------------------------------------------------------------//
    protected int programHandle = 0;    //program
    protected int vertexShaderHandle = 0;  //shaders
    protected int fragmentShaderHandle = 0;

    //New class members
    /** This will be used to pass in the transformation matrix. */
    protected int mMVPMatrixHandle;

    /** This will be used to pass in model position information. */
    protected int mPositionHandle;

    /** This will be used to pass in model color information. */
    protected int mColorHandle;

    //Matrices---------------------------------------------------------------------//
    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    protected float[] mProjectionMatrix = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    protected float[] mViewMatrix = new float[16];

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space. LIKE MODEL STACK
     */
    protected float[] mModelMatrix = new float[16];

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
     * param type: shader type
     *************************************************************************************************/
    public static int LoadShader(int type, String shaderCode){

        // Load in the vertex shader.
        int ShaderHandle = GLES20.glCreateShader(type);

        if (ShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES20.glShaderSource(ShaderHandle, shaderCode);

            // Compile the shader.
            GLES20.glCompileShader(ShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES20.glGetShaderiv(ShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES20.glDeleteShader(ShaderHandle);
                ShaderHandle = 0;
            }
        }

        if (ShaderHandle == 0)
            throw new RuntimeException("Error creating vertex shader.");

        return ShaderHandle;
    }

    /*************************************************************************************************
     * Init shaders
     *************************************************************************************************/
    private void initShaders() {

        vertexShaderHandle = LoadShader(GLES20.GL_VERTEX_SHADER, vertexShader);
        fragmentShaderHandle = LoadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader);

        // Create a program object and store the handle to it.
        programHandle = GLES20.glCreateProgram();

        if (programHandle != 0)
        {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
            GLES20.glBindAttribLocation(programHandle, 1, "a_Color");

            // Link the two shaders together into a program.
            GLES20.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                GLES20.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }
    }

    /*************************************************************************************************
     * Necessary abstract methods
     *************************************************************************************************/
    public void onSurfaceCreated(GL10 notUsed,
                                 EGLConfig config) {
        mSurfaceCreated = true;
        mWidth = -1;
        mHeight = -1;

        //init shaders------------------------------//
        initShaders();

        // Set program handles. These will later be used to pass in values to the program.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(programHandle, "u_MVPMatrix");
        mPositionHandle = GLES20.glGetAttribLocation(programHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(programHandle, "a_Color");

        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(programHandle);

        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        // Set the view matrix. This matrix can be said to represent the camera position.
        // NOTE: In OpenGL 1, a ModelView matrix is used, which is a combination of a model and
        // view matrix. In OpenGL 2, we can keep track of these matrices separately if we choose.
        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX, lookY, lookZ, upX, upY, upZ);
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

        //projection------------------------------------------------------------------//
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
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
    }

    /*************************************************************************************************
     * Implement these and call super if applicable!!!
     *************************************************************************************************/
    public abstract void onCreate(int width, int height,
                                  boolean contextLost);

    public abstract void onDrawFrame(boolean firstDraw);

    /*************************************************************************************************
     * Getters
     *************************************************************************************************/
    public int getFPS() {return mFPS;}
}
