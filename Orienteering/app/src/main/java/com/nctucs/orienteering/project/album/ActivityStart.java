package com.nctucs.orienteering.project.album;

///////////////////////////STUDENT//////////////////////////
///////////// 姓名:林鈺璇 /////////////////////////////////
///////////// 學號:0116224 //////////////////////////////
//////////////////////////////////////////////////////////////////
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.ProgressBar;

import orienteering.album.R;

public class ActivityStart extends Activity {

    ListView lv;
    ProgressBar loadingBar = null;
    SharedPreferences userData = null;
    final int animateSec = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        userData = getSharedPreferences( "userData" , MODE_PRIVATE );
        loadingBar = (ProgressBar)findViewById( R.id.progress_bar );
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Thread( new Runnable() {
            @Override
            public void run() {

                Random random = new Random();
                for ( int i = 10 ; i <= 100 ; i += random.nextInt() % 5 + 5 ){
                    try{
                        Thread.sleep( 300 , 0 );
                    }
                    catch ( Exception e ){
                        e.printStackTrace();
                    }
                    loadingBar.setProgress( i );

                }

                Intent intent = new Intent( ActivityStart.this , ActivityChooseMode.class );
                startActivity( intent );

            }
        } ).start();

    }
}
