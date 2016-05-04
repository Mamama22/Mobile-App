package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 3/5/2016.
 */

import android.opengl.GLES20;
import android.opengl.Matrix;

import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/*************************************************************************************************
 * View, utility OpenGL library for easier and cleaner rendering code
 * STRICTLY RENDERING ONLY
 * Instructions:
 *  1) call onSurfaceCreated(..)
 *  2) call various utility functions to render stuff
 *  3) Remember to call onSurfaceChanged(..) in corrosponding func in Controller
 *************************************************************************************************/
public class View
{
    //=============================================================================================================================//
    //|||||||||||||||||||||||||||||||||||||| *** VARIABLES *** ||||||||||||||||||||||||||||||||||||||//
    //=============================================================================================================================//
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
    protected static int programHandle = 0;    //program
    protected static int vertexShaderHandle = 0;  //shaders
    protected static int fragmentShaderHandle = 0;

    //New class members
    /** This will be used to pass in the transformation matrix. */
    protected static int mMVPMatrixHandle = 0;

    /** This will be used to pass in model position information. */
    protected static int mPositionHandle = 0;

    /** This will be used to pass in model color information. */
    protected static int mColorHandle = 0;

    //Matrices---------------------------------------------------------------------//
    /** Store the projection matrix. This is used to project the scene onto a 2D viewport. */
    protected static float[] mProjectionMatrix = new float[16];

    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    protected static float[] mViewMatrix = new float[16];

    /**
     * Store the model matrix. This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space. LIKE MODEL STACK
     */
    protected static float[] mModelMatrix = new float[16];

    /** Allocate storage for the final combined matrix. This will be passed into the shader program. */
    protected static float[] mMVPMatrix = new float[16];

    //Flags----------------------------//
    private static boolean mSurfaceCreated = false;

    //Info----------------------------//
    private static int mWidth = -1;
    private static int mHeight = -1;

    //=============================================================================================================================//
    //|||||||||||||||||||||||||||||||||||||| *** INIT *** ||||||||||||||||||||||||||||||||||||||//
    //=============================================================================================================================//

    /*************************************************************************************************
     * Init shaders
     *************************************************************************************************/
    private static void initShaders() {

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
     * Load shader
     * param type: shader type
     *************************************************************************************************/
    private static int LoadShader(int type, String shaderCode){

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
     * Surface created: call in corrosponding func in Controller
     *************************************************************************************************/
    public static void onSurfaceCreated(GL10 notUsed,
                                 EGLConfig config)
    {
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

        //clear the screen-------------------------------------------------//
        GLES20.glClearColor(255.f, 0f, 0f, 1f);
    }

    //=============================================================================================================================//
    //|||||||||||||||||||||||||||||||||||||| *** RUNTIME *** ||||||||||||||||||||||||||||||||||||||//
    //=============================================================================================================================//

    /*************************************************************************************************
     * Surface changed: call in corrosponding func in Controller
     *************************************************************************************************/
    public static void onSurfaceChanged(GL10 notUsed, int width, int height)
    {
        if (!mSurfaceCreated && width == mWidth && height == mHeight)
            return;

        mWidth = width;
        mHeight = height;

        //Need recreate shaders everytime reset?-----------------------//
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

    //=============================================================================================================================//
    //|||||||||||||||||||||||||||||||||||||| *** RENDER FUNCTIONS *** ||||||||||||||||||||||||||||||||||||||//
    //=============================================================================================================================//

    /*************************************************************************************************
     * Draw a mesh
     *************************************************************************************************/
    public static void drawMesh(Mesh mesh)
    {
        // Pass in the position information
        mesh.vertices.position(mesh.mPositionOffset);
        GLES20.glVertexAttribPointer(mPositionHandle, mesh.mPositionDataSize, GLES20.GL_FLOAT, false,
                mesh.mStrideBytes, mesh.vertices);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        mesh.vertices.position(mesh.mColorOffset);
        GLES20.glVertexAttribPointer(mColorHandle, mesh.mColorDataSize, GLES20.GL_FLOAT, false,
                mesh.mStrideBytes, mesh.vertices);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }

    /*************************************************************************************************
     * Set to mModelMatrix(TRANSFORMATION MAT) Identity
     *************************************************************************************************/
    public static void SetTransMat_toIdentity(){Matrix.setIdentityM(mModelMatrix, 0);}

    /*************************************************************************************************
     * Transform mModelMatrix(TRANSFORMATION MAT)
     *************************************************************************************************/
    public static void SetTransMat_toTranslate(float X, float Y, float Z){
        Matrix.translateM(mModelMatrix, 0, X, Y, Z);
    }

    public static void SetTransMat_toRotate(float angle, float X, float Y, float Z){
        Matrix.rotateM(mModelMatrix, 0, angle, X, Y, Z);
    }
}
