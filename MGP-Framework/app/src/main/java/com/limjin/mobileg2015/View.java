package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 3/5/2016.
 */

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;
import com.limjin.mobileg2015.Utilities.MiscUtilities;

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

    //Handles: ATTRIBUTES and UNIFORMS (seperate next time)--------------------------------------------------//
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

    /** This will be used to pass in the texture. */
    protected static int mTextureUniformHandle = 0;

    /** This will be used to pass in model texture coordinate information. */
    private static int mTextureCoordinateHandle = 0;

    /** This is a handle to our texture data. */
    private static int mTextureDataHandle = 0;

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

    //Misc----------------------------//
    protected static float[] LightPos = new float[3];

    protected static Context context;

    //=============================================================================================================================//
    //|||||||||||||||||||||||||||||||||||||| *** INIT *** ||||||||||||||||||||||||||||||||||||||//
    //=============================================================================================================================//

    /*************************************************************************************************
     * Init shaders
     *************************************************************************************************/
    private static void initShaders() {

        ///normal shader-------------------------------//
        String vert_texture1 = MiscUtilities.readTextFileFromRawResource(context, R.raw.vert_texture1);
        String frag_texture1 = MiscUtilities.readTextFileFromRawResource(context, R.raw.frag_texture1);

        //light shader--------------------------------//
        String vert_light = MiscUtilities.readTextFileFromRawResource(context, R.raw.vert_light);
        String frag_light = MiscUtilities.readTextFileFromRawResource(context, R.raw.frag_light);

        //normal shader-------------------------------//

            vertexShaderHandle = LoadShader(GLES20.GL_VERTEX_SHADER, vert_texture1);
            fragmentShaderHandle = LoadShader(GLES20.GL_FRAGMENT_SHADER, frag_texture1);


        //link and pass in attributes
        mPerVertexProgramHandle = createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle,
                new String[]{"a_Position", "a_Color", "a_Normal", "a_TexCoordinate"});

        //light shader-------------------------//
        point_vertexShaderHandle = LoadShader(GLES20.GL_VERTEX_SHADER, vert_light);
        point_fragmentShaderHandle = LoadShader(GLES20.GL_FRAGMENT_SHADER, frag_light);

        //link and pass in attributes
        mPointProgramHandle = createAndLinkProgram(point_vertexShaderHandle, point_fragmentShaderHandle,
                new String[] {"a_Position"});
    }

    /*************************************************************************************************
     * Compile and Load shader
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
     * Helper function to compile and link a program.
     *
     * @param vertexShaderHandle An OpenGL handle to an already-compiled vertex shader.
     * @param fragmentShaderHandle An OpenGL handle to an already-compiled fragment shader.
     * @param attributes Attributes that need to be bound to the program.
     * @return An OpenGL handle to the program.
     *************************************************************************************************/
    private static int createAndLinkProgram(final int vertexShaderHandle, final int fragmentShaderHandle, final String[] attributes)
    {
        int programHandle = GLES20.glCreateProgram();

        if (programHandle != 0) {
            // Bind the vertex shader to the program.
            GLES20.glAttachShader(programHandle, vertexShaderHandle);

            // Bind the fragment shader to the program.
            GLES20.glAttachShader(programHandle, fragmentShaderHandle);

            // Bind attributes (ATTRIBUTES MUST BE BINDED TO SHADER)
            if (attributes != null)
            {
                final int size = attributes.length;
                for (int i = 0; i < size; i++)
                {
                    GLES20.glBindAttribLocation(programHandle, i, attributes[i]);
                }
            }

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

        return programHandle;
    }

    /*************************************************************************************************
     * Surface created: call in corrosponding func in Controller
     *************************************************************************************************/
    public static void onSurfaceCreated(Context context)
    {
        View.context = context;

        //clear the screen-------------------------------------------------//
        GLES20.glClearColor(255.f, 0f, 0f, 1f);

        // Use culling to remove back faces.
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        // Enable depth testing
        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        //init shaders------------------------------//
        initShaders();

        // Set the background clear color to gray.
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 9.5f;

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

        //light pos----------------------------------//
        LightPos[0] = 0.f;LightPos[1] = 0.f;LightPos[2] = 1.f;
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
        final float left = -ratio * 0.5f;
        final float right = ratio * 0.5f;
        final float bottom = -0.5f;
        final float top = 0.5f;
        final float near = 1.0f;
        final float far = 100.0f;

        Matrix.frustumM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        //Matrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, near, far);
        //Matrix.perspectiveM(mProjectionMatrix, 0, 45.f, ratio, near, far);
    }

    /*************************************************************************************************
     * Set up shader and uniforms (NON_LIGHT SNADER, WE HAVE MULTIPLE SHADERS)
     *************************************************************************************************/
    protected static void SetShaderAndUniforms()
    {
        // Tell OpenGL to use this program when rendering.
        GLES20.glUseProgram(mPerVertexProgramHandle);

        // Set program handles. These will later be used to pass in values to the program.
        // Set program handles for cube drawing.
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVPMatrix");
        mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_MVMatrix");
        mLightPosHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_LightPos");
        mLightPowHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_Power");
        mTextureUniformHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "u_Texture");

        mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Position");
        mColorHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Color");
        mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_Normal");
        mTextureCoordinateHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "a_TexCoordinate");

        // Set the active texture unit to texture unit 0, ASSUMES ALL USES TEXTURE 0------------------//
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
    }

    //=============================================================================================================================//
    //|||||||||||||||||||||||||||||||||||||| *** RENDER FUNCTIONS *** ||||||||||||||||||||||||||||||||||||||//
    //=============================================================================================================================//

    /*************************************************************************************************
     * Setup: call BEFORE calling any render functions
     *************************************************************************************************/
    public static void PreRender()
    {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        //Light pos----------------------------------------------------//
        LightPos[0] += 0.05f;
        if(LightPos[0] >= 2.f)
            LightPos[0] = -2.f;

        SetShaderAndUniforms();

        // Calculate position of the light. Rotate and then push into the distance.
        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, LightPos[0], LightPos[1], LightPos[2]);
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
        GLES20.glUseProgram(mPointProgramHandle);
        drawLight();
    }

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

        //=========================================================================================================//
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    public static void drawCube(MeshAdvanced mesh)
    {
        //texture assign----------------------------------------------------//
        mTextureDataHandle = mesh.Texture_Handle;


        // Pass in the position information
        mesh.vertices.position(0);
        GLES20.glVertexAttribPointer(mPositionHandle, mesh.mPositionDataSize, GLES20.GL_FLOAT, false,
                0, mesh.vertices);

        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // Pass in the color information
        mesh.color_buffer.position(0);
        GLES20.glVertexAttribPointer(mColorHandle, mesh.mColorDataSize, GLES20.GL_FLOAT, false,
                0, mesh.color_buffer);

        GLES20.glEnableVertexAttribArray(mColorHandle);

        // Pass in the normal information
        mesh.normal_buffer.position(0);
        GLES20.glVertexAttribPointer(mNormalHandle, mesh.mNormalDataSize, GLES20.GL_FLOAT, false,
                0, mesh.normal_buffer);

        GLES20.glEnableVertexAttribArray(mNormalHandle);

        // Pass in the texture information
        mesh.texCoord_buffer.position(0);
        GLES20.glVertexAttribPointer(mTextureCoordinateHandle, mesh.mTextureCoordinateDataSize, GLES20.GL_FLOAT, false,
                0, mesh.texCoord_buffer);

        GLES20.glEnableVertexAttribArray(mTextureCoordinateHandle);

        //=========================================================================================================//
        /****************************************** Transformation ******************************************/
        // This multiplies the view matrix by the model matrix, and stores the result in the MVP matrix
        // (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // Pass in the modelview matrix.
        GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVPMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Pass in light power
        GLES20.glUniform1f(mLightPowHandle, 2.f);

        // Pass in the combined matrix.
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Pass in the light position in eye space.
        GLES20.glUniform3f(mLightPosHandle, mLightPosInEyeSpace[0], mLightPosInEyeSpace[1], mLightPosInEyeSpace[2]);

        /****************************************** Texture ******************************************/
        // Bind the texture to this unit.
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandle);

        // Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0.
        GLES20.glUniform1i(mTextureUniformHandle, 0);

        /****************************************** Draw ******************************************/
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 36);
    }

    /**
     * Draws a point representing the position of the light.
     */
    private static void drawLight()
    {
        final int pointMVPMatrixHandle = GLES20.glGetUniformLocation(mPointProgramHandle, "u_MVPMatrix");
        final int pointPositionHandle = GLES20.glGetAttribLocation(mPointProgramHandle, "a_Position");

        // Pass in the position.
        GLES20.glVertexAttrib3f(pointPositionHandle, mLightPosInModelSpace[0], mLightPosInModelSpace[1], mLightPosInModelSpace[2]);

        // Since we are not using a buffer object, disable vertex arrays for this attribute.
        GLES20.glDisableVertexAttribArray(pointPositionHandle);

        // Pass in the transformation matrix.
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mLightModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(pointMVPMatrixHandle, 1, false, mMVPMatrix, 0);

        // Draw the point.
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, 1);
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
