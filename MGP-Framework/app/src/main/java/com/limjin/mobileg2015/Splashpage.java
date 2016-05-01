package com.limjin.mobileg2015;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.Image;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class Splashpage extends Activity {

    protected boolean _active = true;
    protected int _splashTime = 500; // time to display in ms
    //MediaPlayer ourSong;
    ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);// hide title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //hide top bar

        setContentView(R.layout.splashpage);

//        final ImageView iv = (ImageView) findViewById(R.id.imageView1);
//        final Animation an = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
//        final Animation an2 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.splashfadeout);
//
//        iv.startAnimation(an);
//        an.setAnimationListener(new Animation.AnimationListener()
//        {
//            @Override
//            public void onAnimationStart(Animation animation){
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation){
//                iv.startAnimation(an2);
//                finish();
//                Intent i = new Intent(Splashpage.this, MainMenu.class);
//                startActivity(i);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation){
//
//            }
//        });


        //ourSong = MediaPlayer.create(Splashpage.this, R.raw.splashmusic);
       // ourSong.start();

        overridePendingTransition(R.anim.splashfadein, R.anim.splashfadeout);

        //thread for displaying the Splash Screen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(200);
                        if (_active) {
                            waited += 200;
                        }
                    }
                } catch (InterruptedException e) {
                    //do nothing
                } finally {
                    finish();

                    // Add codes
                    Intent intent = new Intent(Splashpage.this, MainMenu.class);

                    startActivity(intent);

                   //overridePendingTransition(R.anim.splashfadein, R.anim.splashfadeout);
                }
            }
        };
        splashTread.start();
    }

    @Override
    protected  void onPause(){
        super.onPause();
       // ourSong.release();
    }


    public boolean onTouchEvent(MotionEvent event){
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            _active = false;
        }
        return true;
    }
}
