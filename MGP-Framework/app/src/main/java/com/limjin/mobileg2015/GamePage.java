package com.limjin.mobileg2015;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/*************************************************************************************************
 * Android page hosting game thread
 *************************************************************************************************/
public class GamePage extends Activity{

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //Page Setup-------------------------------------------------------------------//
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        //Create game 'thread'-------------------------------//
        setContentView(new Controller(this));
    }

    protected void onPause(){
        super.onPause();
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}
