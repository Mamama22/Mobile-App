package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 13/5/2016.
 */

import com.limjin.mobileg2015.Utilities.Buffer_Utilities;
import com.limjin.mobileg2015.Utilities.Mesh_Builder;
import com.limjin.mobileg2015.Utilities.Misc_Utilities;

/*************************************************************************************************
 * Basic OpenGL rendering
 *************************************************************************************************/
public class SceneBasicOpenGL extends Scene
{
    float angle;

    MeshVBO_combined cube, cube1, cube2;


    @Override
    protected void LocalInit()
    {
        scene_state = RUN;

        //Mesh-------------------------------------//
        cube = Mesh_Builder.GenerateCube(MeshMan.COLOR_NONE, MeshMan.TEX_FACEBOOK);
        cube1 = Mesh_Builder.GenerateCube(MeshMan.COLOR_GREEN, MeshMan.TEX_NONE);
        cube2 = Mesh_Builder.GenerateCube(MeshMan.COLOR_NONE, MeshMan.TEX_RECYCLE);

        angle = 0.f;
    }

    @Override
    protected void Update_TransIn() {

    }

    @Override
    protected void Update_Scene()
    {
        angle += 1.f;

        if(angle >= 360.f)
            angle = 0.f;
        else if(angle < 0.f)
            angle = 360.f;
    }

    @Override
    protected void Update_TransOut() {

    }

    @Override
    protected void Render_TransIn() {

    }

    @Override
    protected void Render_Scene()
    {
        //Render mesh----------------------------------------------//
        View.SetTransMat_toIdentity();
        View.SetTransMat_toTranslate(-3.8f, 0.f, 0.f);
        View.SetTransMat_toRotate(angle, 0.f, 1.f, 0.f);
        //View.SetTransMat_toScale(0.6f, 0.6f, 0.6f);
        View.drawMeshLight_VBO_combined(cube);

        View.SetTransMat_toIdentity();
        View.SetTransMat_toTranslate(1.6f, 0.f, -0.5f);
        View.SetTransMat_toRotate(angle, 0.f, 0.5f, 0.5f);
        //View.SetTransMat_toScale(0.8f, 0.8f, 0.8f);
        View.drawMeshLight_VBO_combined(cube1);

        View.SetTransMat_toIdentity();
        View.SetTransMat_toTranslate(0.1f, 1.f, -2.f);
        View.SetTransMat_toRotate(angle, 0.8f, 0.2f, 0.0f);
        View.SetTransMat_toScale(0.6f, 0.6f, 0.6f);
        View.drawMeshLight_VBO_combined(cube2);
    }

    @Override
    protected void Render_TransOut() {

    }

    @Override
    public void Pause() {

    }

    @Override
    public void Resume() {

    }

    @Override
    public void Quit() {

    }
}
