package com.nctucs.orienteering.project.album;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;

import com.nctucs.orienteering.project.JSONMsg.JSONType;
import com.nctucs.orienteering.project.R;
import com.nctucs.orienteering.project.tcpSocket.tcpSocket;

import org.json.JSONObject;

/**
 * Created by Shamrock on 2015/4/10.
 */
public class ActivityMain extends FragmentActivity {

    private boolean isNewGame ;


    FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (FragmentTabHost) findViewById( android.R.id.tabhost );
        SetTabs();

        Intent intent = getIntent();
        isNewGame = intent.getBooleanExtra( "isNewGame" , true );
        if ( isNewGame ){
            SharedPreferences sharedPreferences = getSharedPreferences( "userData" , MODE_PRIVATE );
            sharedPreferences.edit().putInt( "msgCnt" , 0 ).apply();
            sharedPreferences.edit().putInt( "hintCnt" , 0 ).apply();
            sharedPreferences.edit().putInt( "keyCnt" , 0 ).apply();
        }
        new GetHintThread().start();
    }

    private class GetHintThread extends Thread implements Runnable{
            @Override
            public void run () {
                try {
                    tcpSocket socket = new tcpSocket();
                    socket.send( new JSONType(1) );

                    JSONObject result = socket.recieve();

                    SharedPreferences sharedPreferences = getSharedPreferences( "userData" , MODE_PRIVATE );
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt( "hintCnt" , result.getInt("hintCnt") );
                    for ( int i = 0 , j = result.getInt( "hintCnt" ) ; i < j ; i++ )
                        editor.putString( "hint" + i , result.getString("hint"+i) );
                    editor.apply();
                }
                catch ( Exception e ){
                    e.printStackTrace();
                }

                SharedPreferences sharedPreferences = getSharedPreferences( "userData" , MODE_PRIVATE );
                SharedPreferences.Editor editor = sharedPreferences.edit();


            }
    };

    private void SetTabs(){


        tabHost.setup(this, getSupportFragmentManager(), R.id.tab_real_content);
        tabHost.setCurrentTab(0);

        tabHost.addTab(tabHost.newTabSpec("遊戲畫面").setIndicator("遊戲畫面"), FragmentGoogleMap.class , null);
        tabHost.addTab(tabHost.newTabSpec("提示").setIndicator("提示") , FragmentMyHint.class , null );
        tabHost.addTab(tabHost.newTabSpec("留言").setIndicator("留言") , FragmentMyHint.class , null );
        tabHost.addTab(tabHost.newTabSpec("鑰匙").setIndicator("鑰匙") , FragmentMyHint.class , null);
    }

}
