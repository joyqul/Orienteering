package com.nctucs.orienteering.project.album;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nctucs.orienteering.project.JSONMsg.JSONType;
import com.nctucs.orienteering.project.Param.ServerInfo;
import com.nctucs.orienteering.project.R;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import com.nctucs.orienteering.project.tcpSocket.tcpSocket;
/**
 * Created by Shamrock on 2015/4/10.
 */
public class ActivityLogin extends Activity implements View.OnClickListener {

    SharedPreferences sharedPreferences = null;
    EditText userID;
    Button submit;
    int gameId;
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("userData", MODE_PRIVATE);
        gameId = sharedPreferences.getInt("gameId", 0);
        token = sharedPreferences.getString("token", null);
        submit = (Button)findViewById(R.id.button_submit);
        submit.setOnClickListener(this);

        userID = (EditText)findViewById(R.id.edit_text);


    }


    private class LogInThread extends Thread implements Runnable{
        @Override
        public void run() {
            super.run();
            String ID = userID.getText().toString();
            try {

                JSONType json = new JSONType(0);
                json.put("token", token);
                json.put("playerName", ID);
                json.put("gameId", gameId);
                tcpSocket socket = new tcpSocket();
                Log.e("json", json.toString());
                socket.send( json );
                JSONObject result = socket.recieve();

                if (result.getBoolean( "success" ) == true){
                    socket.close();
                    sharedPreferences.edit().putString("userName", ID).apply();
                    sharedPreferences.edit().putString("answer", result.getString("answer")).apply();
                    sharedPreferences.edit().putFloat("goalLat", ((float)result.getDouble("goalLat"))).apply();
                    sharedPreferences.edit().putFloat("goalLong", ((float)result.getDouble("goalLong"))).apply();
                    sharedPreferences.edit().putInt("totalKey", result.getInt("totalKey")).apply();
                    Intent intent = new Intent(ActivityLogin.this , ActivityMain.class);
                    intent.putExtra("isNewGame", true);
                    startActivity(intent);
                }else{
//                    Toast.makeText( ActivityLogin.this , "LogIn Failed!" , Toast.LENGTH_SHORT );
                    socket.close();
                }


            }
            catch ( Exception e ){
                e.printStackTrace();
//                Toast.makeText( ActivityLogin.this , "Login Failed" , Toast.LENGTH_SHORT ).show();
            }
        }
    }




    @Override
    public void onClick(View v) {

        String ID = userID.getText().toString();

        if ( ID == null )
            Toast.makeText( ActivityLogin.this , "Please Enter something, stupid!!!" , Toast.LENGTH_SHORT  ).show();
        else{
            new LogInThread().start();

        }



    }
}
