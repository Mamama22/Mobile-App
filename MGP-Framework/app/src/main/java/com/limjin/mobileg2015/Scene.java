package com.limjin.mobileg2015;

/**
 * Created by tanyiecher on 3/5/2016.
 */

import android.content.Context;

/*************************************************************************************************
 * Scene abstract class: inherit to create and define your own scene
 *************************************************************************************************/
public abstract class Scene
{
    /*************************************************************************************************
     * Values---------------------------------------------------------------------------------------
     *************************************************************************************************/
    //Scene states ENUM---------------------------//
    protected static final int TRANS_IN = 0;
    protected static final int RUN = 1;
    protected static final int TRANS_OUT = 2;

    /*************************************************************************************************
     * Variables---------------------------------------------------------------------------------------
     *************************************************************************************************/
    protected int scene_state;


    /*************************************************************************************************
     * Functions---------------------------------------------------------------------------------------
     *************************************************************************************************/
    public void Init()    //set up common stuff
    {
        scene_state = TRANS_IN;
        LocalInit();
    }

    protected abstract void LocalInit();    //init local scene

    //update functions---------------------------------//
    protected abstract void Update_TransIn();
    protected abstract void Update_Scene();
    protected abstract void Update_TransOut();
    public void Update()
    {
        if(scene_state == RUN)
            Update_Scene();
        else if(scene_state == TRANS_OUT)
            Update_TransOut();
        else
            Update_TransIn();
    }

    //render functions---------------------------------//
    protected abstract void Render_TransIn();
    protected abstract void Render_Scene();
    protected abstract void Render_TransOut();
    public void Render()
    {
        if(scene_state == RUN)
            Render_Scene();
        else if(scene_state == TRANS_OUT)
            Render_TransOut();
        else
            Render_TransIn();
    }

    //when changing scene etc.----------------------//
    public abstract void Pause();
    public abstract void Resume();

    //Not using scene anymore-----------------------//
    public abstract void Quit();    //quit and destroy everything
}
