package com.nctucs.orienteering.project.album;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;

import com.nctucs.orienteering.project.R;
/**
 * Created by Shamrock on 2015/4/10.
 */
public class ActivityMain extends FragmentActivity {

    private boolean isNewGame = true;
    FragmentTabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabHost = (FragmentTabHost) findViewById( android.R.id.tabhost );
        SetTabs();

        Intent intent = getIntent();
        isNewGame = intent.getBooleanExtra( "isNewGame" , true );

    }

    private void SetTabs(){

        Log.e(" inside ", "hi");
        tabHost.setup(this, getSupportFragmentManager(), R.id.tab_real_content);
        tabHost.setCurrentTab(0);

        tabHost.addTab(tabHost.newTabSpec("遊戲畫面").setIndicator("遊戲畫面"), FragmentGoogleMap.class , null);
        tabHost.addTab(tabHost.newTabSpec("提示").setIndicator("提示") , FragmentMyHint.class , null );
        tabHost.addTab(tabHost.newTabSpec("提示").setIndicator("提示") , FragmentMyHint.class , null );

    }

}
