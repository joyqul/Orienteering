package orienteering.album;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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



        }



    }
}
