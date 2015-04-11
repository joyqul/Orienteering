package com.nctucs.orienteering.project.album;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import orienteering.album.R;


/**
 * Created by Shamrock on 2015/4/10.
 */
public class ActivityLogin extends Activity implements View.OnClickListener {

    SharedPreferences userData = null;
    EditText userID;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_login );
        userData = getSharedPreferences( "userData" , MODE_PRIVATE );

        userID = ( EditText ) findViewById( R.id.edit_text );
        submit = ( Button )   findViewById( R.id.button_submit );

        submit.setOnClickListener( this );


    }


    @Override
    public void onClick(View v) {

        String ID = userID.getText().toString();

        if ( ID == null )
            Toast.makeText( ActivityLogin.this , "Please Enter something, stupid!!!" , Toast.LENGTH_SHORT  ).show();
        else{

            try {


                /*
                JSONType0 msg = new JSONType0();
                msg.put("playerName", ID);

                Socket socket = new Socket( ServerInfo.SERVER_IP , ServerInfo.PORT );
                DataOutputStream os = new DataOutputStream( socket.getOutputStream() );
                DataInputStream  is = new DataInputStream(  socket.getInputStream() );

                os.writeUTF(msg.toString());


                JSONObject result = new JSONObject( is.readUTF() );

                if ( result.get( "success" ).equals( true ) ){
                    socket.close();
                    Intent intent = new Intent( ActivityLogin.this , ActivitySaveLoad.class );
                    startActivity( intent );
                }
                else{
                    Toast.makeText( ActivityLogin.this , "LogIn Failed!" , Toast.LENGTH_SHORT );
                    socket.close();
                }
*/
                Intent intent = new Intent( ActivityLogin.this , ActivitySaveLoad.class );
                startActivity( intent );

            }
            catch ( Exception e ){
                e.printStackTrace();
                Toast.makeText( ActivityLogin.this , "Login Failed" , Toast.LENGTH_SHORT ).show();
            }

        }



    }
}
