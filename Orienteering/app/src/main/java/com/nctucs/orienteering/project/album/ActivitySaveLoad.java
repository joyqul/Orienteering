package com.nctucs.orienteering.project.album;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.nctucs.orienteering.project.R;

public class ActivitySaveLoad extends Activity implements View.OnClickListener{

    Button newGame , conGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_save_load );

        newGame = (Button) findViewById( R.id.btn_new_game );
        conGame = (Button) findViewById( R.id.btn_continue );

        newGame.setOnClickListener( this );
        conGame.setOnClickListener( this );

    }

    @Override
    public void onClick(View v) {
        switch ( v.getId() ){
            case R.id.btn_new_game:
                Intent intent = new Intent(ActivitySaveLoad.this , ActivityChooseGame.class);
                startActivity(intent);
                ActivitySaveLoad.this.finish();
            break;

            case R.id.btn_continue:
                Intent intent2 = new Intent( ActivitySaveLoad.this , ActivityMain.class );
                intent2.putExtra( "isNewGame" , false );
                startActivity(intent2);
                ActivitySaveLoad.this.finish();
            break;

        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_load, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
