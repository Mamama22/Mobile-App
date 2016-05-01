package com.limjin.mobileg2015;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

/*************************************************************************************************
 * The 'Main' function/entry point of Android app.
 *************************************************************************************************/
public class MainMenu extends Activity implements OnClickListener {

    // Button Variables
    private Button btn_start;
    /*
    private Button btn_option;
    private Button btn_gallery;
    private Button btn_score;
    private Button btn_facebook;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        setContentView(R.layout.mainmenu);

        overridePendingTransition(R.anim.splashfadein, R.anim.splashfadeout);

       /* // Set up background music
        if(checkmainMusic == 0) {
            bgMusic = MediaPlayer.create(MainMenu.this, R.raw.chasers);
            bgMusic.setVolume(20, 20);
            bgMusic.setLooping(true);
            bgMusic.start();
        }*/

       btn_start = (Button)findViewById(R.id.btn_start);
       btn_start.setOnClickListener(this);

        /*
        btn_option = (Button)findViewById(R.id.btn_option);
        btn_option.setOnClickListener(this);

        btn_gallery = (Button)findViewById(R.id.btn_gallery);
        btn_gallery.setOnClickListener(this);

        btn_score = (Button)findViewById(R.id.btn_score);
        btn_score.setOnClickListener(this);

        btn_facebook = (Button)findViewById(R.id.btn_facebook);
        btn_facebook.setOnClickListener(this);*/
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent();

        if(view == btn_start)
        {
            intent.setClass(this, GamePage.class);
        }
        /*
        else if(view == btn_option){
            intent.setClass(this, OptionPage.class);
            buttonClick.start();
        }

        else if(view == btn_gallery){
            intent.setClass(this, GalleryPage.class);
            buttonClick.start();
        }

        else if(view == btn_score){
            intent.setClass(this, ScorePage.class);
            buttonClick.start();
        }

        else if(view == btn_facebook){
            intent.setClass(this, FBPage.class);
            buttonClick.start();
        }*/

        startActivity(intent);
    }

    protected void onPause(){
        super.onPause();
        /*
        if(checkmainMusic == 0) {
            bgMusic.release();
            //  finish();
        }*/
    }

    protected void onStop(){
        super.onStop();
    }

    protected void onDestroy(){
        super.onDestroy();
    }
}
