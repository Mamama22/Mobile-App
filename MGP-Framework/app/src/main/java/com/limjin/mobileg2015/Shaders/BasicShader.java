package com.limjin.mobileg2015.Shaders;

/**
 * Created by tanyiecher on 9/5/2016.
 */
/*************************************************************************************************
 * Contains shader code
 *************************************************************************************************/
public class BasicShader
{
    //shaders------------------------------------------//
    public static final String vertexShader =
                      "uniform mat4 u_MVPMatrix;      \n"     // A constant representing the combined model/view/projection matrix.
                    + "uniform mat4 u_MVMatrix;       \n"     // A constant representing the combined model/view matrix.
                    + "uniform vec3 u_LightPos;       \n"     // The position of the light in eye space.
                    + "uniform float u_Power;       \n"

                    + "attribute vec4 a_Position;     \n"     // Per-vertex position information we will pass in.
                    + "attribute vec4 a_Color;        \n"     // Per-vertex color information we will pass in.
                    + "attribute vec3 a_Normal;       \n"     // Per-vertex normal information we will pass in

                    + "varying vec3 v_Position;          \n"     // This will be passed into the fragment shader.
                    + "varying vec4 v_Color;          \n"     // This will be passed into the fragment shader.
                    + "varying vec3 v_Normal;          \n"     // This will be passed into the fragment shader.

                    + "void main()                    \n"     // The entry point for our vertex shader.
                    + "{                              \n"

// Transform the vertex into eye space.
                    + "  v_Position = vec3(u_MVMatrix * a_Position);              \n"

                    // Pass color to fragment
                    + "   v_Color = a_Color;              \n"

                    // Transform the normal's orientation into eye space.
                    + "   v_Normal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));              \n"

// gl_Position is a special variable used to store the final position.
// Multiply the vertex by the matrix to get the final point in normalized screen coordinates.
                    + "   gl_Position = u_MVPMatrix * a_Position;                            \n"
                    + "}";

    public static final String fragmentShader =
            "precision mediump float;       \n"     // Set the default precision to medium. We don't need as high of a
                    // precision in the fragment shader.

                    + "uniform vec3 u_LightPos;       \n"     // The position of the light in eye space.
                    + "uniform float u_Power;       \n"

                    + "varying vec3 v_Position;          \n"     // Interpolated position for this fragment.
                    + "varying vec4 v_Color;          \n"     // This is the color from the vertex shader interpolated across the
                    // triangle per fragment.
                    + "varying vec3 v_Normal;          \n"     // Interpolated normal for this fragment.


                    + "void main()                    \n"     // The entry point for our fragment shader.
                    + "{                              \n"

                    // Will be used for attenuation.
                    + "   float distance = length(u_LightPos - v_Position);             \n"

// Get a lighting direction vector from the light to the vertex.
                    + "   vec3 lightVector = normalize(u_LightPos - v_Position);        \n"

// Calculate the dot product of the light vector and vertex normal. If the normal and light vector are
// pointing in the same direction then it will get max illumination.
                    + "   float diffuse = max(dot(v_Normal, lightVector), 0.1);       \n"

// Attenuate the light based on distance.
                    + "   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n"

// Multiply the color by the illumination level. It will be interpolated across the triangle.
                    + "   gl_FragColor = v_Color * diffuse * u_Power;                    \n"
                    + "}                              \n";


    // Define a simple shader program for our point.
    public final static String pointVertexShader =
            "uniform mat4 u_MVPMatrix;      \n"
                    + "attribute vec4 a_Position;     \n"
                    + "void main()                    \n"
                    + "{                              \n"
                    + "   gl_Position = u_MVPMatrix   \n"
                    + "               * a_Position;   \n"
                    + "   gl_PointSize = 5.0;         \n"
                    + "}                              \n";

    public final static String pointFragmentShader =
            "precision mediump float;       \n"
                    + "void main()                    \n"
                    + "{                              \n"
                    + "   gl_FragColor = vec4(1.0,    \n"
                    + "   1.0, 1.0, 1.0);             \n"
                    + "}                              \n";
}
