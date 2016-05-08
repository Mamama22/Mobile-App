package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 3/5/2016.
 */

import android.opengl.GLES30;
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
    protected static final String vertexShader =
            "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
                    + "uniform mat4 u_MVMatrix;       \n"     // A constant representing the combined model/view matrix.
                    + "uniform vec3 u_LightPos;       \n"     // The position of the light in eye space.
                    + "uniform float u_Power;       \n"

                    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.
                    + "attribute vec3 a_Normal;       \n"     // Per-vertex normal information we will pass in

                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.

                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"
// Transform the vertex into eye space.
                    + "   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n"
// Transform the normal's orientation into eye space.
                    + "   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n"
// Will be used for attenuation.
                    + "   float distance = length(u_LightPos - modelViewVertex);             \n"
// Get a lighting direction vector from the light to the vertex.
                    + "   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n"
// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
// pointing in the same direction then it will get max illumination.
                    + "   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n"
// Attenuate the light based on distance.
                    + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"
// Multiply the color by the illumination level. It will be interpolated across the triangle.
                    + "   v_Color = a_Color * diffuse * u_Power;                                       \n"
// gl_Position is a special variable used to store the final position.
// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                    + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
                    + "}";

    protected static final String fragmentShader =
            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.
                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"
                    + "   gl_FragColor = v_Color;     \n"     // Pass the color directly through the pipeline.
                    + "}                              \n";


    // Define a simple shader program for our point.
    final static String pointVertexShader =
            "uniform mat4 u_MVPMatrix;      \n"
                    + "attribute vec4 a_Position;     \n"
                    + "void main()                    \n"
                    + "{                              \n"
                    + "   gl_Position = u_MVPMatrix   \n"
                    + "               * a_Position;   \n"
                    + "   gl_PointSize = 5.0;         \n"
                    + "}                              \n";

    final static String pointFragmentShader =
            "precision mediump float;       \n"
                    + "void main()                    \n"
                    + "{                              \n"
                    + "   gl_FragColor = vec4(1.0,    \n"
                    + "   1.0, 1.0, 1.0);             \n"
                    + "}                              \n";

    //Program handles----------------------------//
    /** This is a handle to our per-vertex cube shading program. */
    private static int mPerVertexProgramHandle = 0;
    /** This is a handle to our light point program. */
    private static int mPointProgramHandle = 0;

    //Shander Handles----------------------------------------------------------------//
    protected static int vertexShaderHandle = 0;  //shaders
    protected static int fragmentShaderHandle = 0;
    protected static int point_vertexShaderHandle = 0;  //shaders for light
    protected static int point_fragmentShaderHandle = 0;

    //New class members
    /** This will be used to pass in the transformation matrix. */
    protected static int mMVPMatrixHandle = 0;

    /** This will be used to pass in the modelview matrix. */
    protected static int mMVMatrixHandle = 0;

    /** This will be used to pass in the light position. */
    protected static int mLightPosHandle = 0;

    /** This will be used to pass in the light power. */
    protected static int mLightPowHandle = 0;

    /** This will be used to pass in model position information. */
    protected static int mPositionHandle = 0;

    /** This will be used to pass in model color information. */
    protected static int mColorHandle = 0;

    /** This will be used to pass in model normal information. */
    protected static int mNormalHandle = 0;

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

    //Matrices for LIGHT SOURCE---------------------------------------------------------------------//
    /**
     * Stores a copy of the model matrix specifically for the light position. (OPTIMIZE???)
     */
    protected static float[] mLightModelMatrix = new float[16];

    /** Used to hold a light centered on the origin in model space. We need a 4th coordinate so we can get translations to work when
     *  we multiply this by our transformation matrices. */
    protected static float[] mLightPosInModelSpace = new float[] {0.0f, 0.0f, 0.0f, 1.0f};

    /** Used to hold the current position of the light in world space (after transformation via model matrix). */
    protected static float[] mLightPosInWorldSpace = new float[4];

    /** Used to hold the transformed position of the light in eye space (after transformation via modelview matrix) */
    protected static float[] mLightPosInEyeSpace = new float[4];

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

        //normal shader-------------------------------//
        vertexShaderHandle = LoadShader(GLES30.GL_VERTEX_SHADER, vertexShader);
        fragmentShaderHandle = LoadShader(GLES30.GL_FRAGMENT_SHADER, fragmentShader);

        //link
        mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"a_Position", "a_Color", "a_Normal"});

        //light shader-------------------------//
        point_vertexShaderHandle = LoadShader(GLES30.GL_VERTEX_SHADER, pointVertexShader);
        point_fragmentShaderHandle = LoadShader(GLES30.GL_FRAGMENT_SHADER, pointFragmentShader);

        //link
        mPointProgramHandle = createAndLinkProgram(point_vertexShaderHandle, point_fragmentShaderHandle,
                new String[] {"a_Position"});
    }

    /*************************************************************************************************
     * Compile and Load shader
     * param type: shader type
     *************************************************************************************************/
    private static int LoadShader(int type, String shaderCode){

        // Load in the vertex shader.
        int ShaderHandle = GLES30.glCreateShader(type);

        if (ShaderHandle != 0)
        {
            // Pass in the shader source.
            GLES30.glShaderSource(ShaderHandle, shaderCode);

            // Compile the shader.
            GLES30.glCompileShader(ShaderHandle);

            // Get the compilation status.
            final int[] compileStatus = new int[1];
            GLES30.glGetShaderiv(ShaderHandle, GLES30.GL_COMPILE_STATUS, compileStatus, 0);

            // If the compilation failed, delete the shader.
            if (compileStatus[0] == 0)
            {
                GLES30.glDeleteShader(ShaderHandle);
                ShaderHandle = 0;
            }
        }

        if (ShaderHandle == 0)
            throw new RuntimeException("Error creating vertex shader.");

        return ShaderHandle;
    }

    /*************************************************************************************************
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @param attributes Attributes that need to be bound to the program.
     * @return An OpenGL handle to the program.
     *************************************************************************************************/
    private static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
    {
        int programHandle = GLES30.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES30.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES30.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes
            if (attributes != null)
            {
                final int size = attributes.length;
                for (int i = 0; i < size; i++)
                {
                    GLES30.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

            // Link the two shaders together into a program.
            GLES30.glLinkProgram(programHandle);

            // Get the link status.
            final int[] linkStatus = new int[1];
            GLES30.glGetProgramiv(programHandle, GLES30.GL_LINK_STATUS, linkStatus, 0);

            // If the link failed, delete the program.
            if (linkStatus[0] == 0)
            {
                GLES30.glDeleteProgram(programHandle);
                programHandle = 0;
            }
        }

        if (programHandle == 0)
        {
            throw new RuntimeException("Error creating program.");
        }

        return programHandle;
    }

    /*************************************************************************************************
     * Surface created: call in corrosponding func in Controller
     *************************************************************************************************/
    public static void onSurfaceCreated()
    {
        //clear the screen-------------------------------------------------//
        GLES30.glClearColor(255.f, 0f, 0f, 1f);

        // Use culling to remove back faces.
        GLES30.glEnable(GLES30.GL_CULL_FACE);

        // Enable depth testing
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        //init shaders------------------------------//
        initShaders();

        // Set the background clear color to gray.
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 2.5f;

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
        GLES30.glViewport(0, 0, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio * 2.f;
        final float right = ratio * 2.f;
        final float bottom = -2.0f;
        final float top = 2.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
    }

    /*************************************************************************************************
     * Set up shader and uniforms
     *************************************************************************************************/
    protected static void SetShaderAndUniforms()
    {
        // Tell OpenGL to use this program when rendering.
        GLES30.glUseProgram(mPerVertexProgramHandle);

        // Set program handles. These will later be used to pass in values to the program.
        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES30.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES30.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES30.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
        mLightPowHandle = GLES30.glGetUniformLocation(mPerVertexProgramHandle, "u_Power");
        mPositionHandle = GLES30.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
        mColorHandle = GLES30.glGetAttribLocation(mPerVertexProgramHandle, "a_Color");
        mNormalHandle = GLES30.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal");
    }

    //=============================================================================================================================//
    //|||||||||||||||||||||||||||||||||||||| *** RENDER FUNCTIONS *** ||||||||||||||||||||||||||||||||||||||//
    //=============================================================================================================================//

    /*************************************************************************************************
     * Setup: call BEFORE calling any render functions
     *************************************************************************************************/
    public static void PreRender()
    {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        SetShaderAndUniforms();

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, -1.0f, 0.0f, 1.0f);
        //Matrix.rotateM(mLightModelMatrix, 0, 20.f, 0.0f, 1.0f, 0.0f);
       // Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);

        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);
    }

    /*************************************************************************************************
     * Setup: call AFTER calling any render functions
     *************************************************************************************************/
    public static void PostRender()
    {
        // Draw a point to indicate the light.
        GLES30.glUseProgram(mPointProgramHandle);
        drawLight();
    }

    /*************************************************************************************************
     * Draw a mesh
     *************************************************************************************************/
    public static void drawMesh(Mesh mesh)
    {
        // Pass in the position information
        mesh.vertices.position(mesh.mPositionOffset);
        GLES30.glVertexAttribPointer(mPositionHandle, mesh.mPositionDataSize, GLES30.GL_FLOAT, false,
                mesh.mStrideBytes, mesh.vertices);

        GLES30.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        mesh.vertices.position(mesh.mColorOffset);
        GLES30.glVertexAttribPointer(mColorHandle, mesh.mColorDataSize, GLES30.GL_FLOAT, false,
                mesh.mStrideBytes, mesh.vertices);

        GLES30.glEnableVertexAttribArray(mColorHandle);

        //=========================================================================================================//
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
    }

    public static void drawCube(MeshAdvanced mesh)
    {
        // Pass in the position information
        mesh.vertices.position(0);
        GLES30.glVertexAttribPointer(mPositionHandle, mesh.mPositionDataSize, GLES30.GL_FLOAT, false,
                0, mesh.vertices);

        GLES30.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        mesh.color_buffer.position(0);
        GLES30.glVertexAttribPointer(mColorHandle, mesh.mColorDataSize, GLES30.GL_FLOAT, false,
                0, mesh.color_buffer);

        GLES30.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        mesh.normal_buffer.position(0);
        GLES30.glVertexAttribPointer(mNormalHandle, mesh.mNormalDataSize, GLES30.GL_FLOAT, false,
               0, mesh.normal_buffer);

        GLES30.glEnableVertexAttribArray(mNormalHandle);

        //=========================================================================================================//
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // Pass in the modelview matrix.
        GLES30.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in light power
        GLES30.glUniform1f(mLightPowHandle, 2.f);

        // Pass in the combined matrix.
        GLES30.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Pass in the light position in eye space.
        GLES30.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        // Draw the cube.
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 36);
    }

    /**
     * Draws a point representing the position of the light.
     */
    private static void drawLight()
    {
        final int pointMVPMatrixHandle = GLES30.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
        final int pointPositionHandle = GLES30.glGetAttribLocation(mPointProgramHandle, "a_Position");

        // Pass in the position.
        GLES30.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES30.glDisableVertexAttribArray(pointPositionHandle);

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES30.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the point.
        GLES30.glDrawArrays(GLES30.GL_POINTS, 0, 1);
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

    public static void SetTransMat_toScale(float X, float Y, float Z){
        Matrix.scaleM(mModelMatrix, 0, X, Y, Z);
    }
}
