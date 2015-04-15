package com.nctucs.orienteering.project.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nctucs.orienteering.project.R;


/**
 * Created by Shamrock on 2015/4/10.
 */
public class ActivityChooseMode extends Activity implements View.OnClickListener{

    Button btnHost , btnPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView( R.layout.activity_choose_mode );
        btnHost = (Button) findViewById( R.id.btn_host );
        btnPlayer = (Button) findViewById( R.id.btn_player );

        btnHost.setOnClickListener( this );
        btnPlayer.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        switch( v.getId() ){
            case R.id.btn_host:
                Toast.makeText( ActivityChooseMode.this , "You should buy the pro version to activate this!" , Toast.LENGTH_SHORT ).show();
            break;
            case R.id.btn_player:

                startActivity( new Intent( ActivityChooseMode.this , ActivityLogin.class ) );
            break;
        }
    }

}
