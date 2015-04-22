package com.nctucs.orienteering.project.tcpSocket;
import android.util.Log;
import android.widget.Toast;

import com.nctucs.orienteering.project.Param.ServerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class tcpSocket extends Socket {

    DataInputStream is;
    DataOutputStream os;
    //PrintWriter output;
    //BufferedReader input;

    public tcpSocket() throws IOException {
        super( ServerInfo.SERVER_IP  , ServerInfo.PORT );

        is = new DataInputStream(this.getInputStream());
        os = new DataOutputStream(this.getOutputStream());
        //output = new PrintWriter(os);
        //input = new BufferedReader(new InputStreamReader(is));
    }

    public void send(JSONObject obj) {
        try {
            Log.e("send", obj.toString());
            os.write( obj.toString().getBytes("UTF-8") );
            //os.writeUTF(obj.toString());
        }
        catch( Exception e){
            e.printStackTrace();
        }
    }

    public JSONObject recieve(){
        try {
            String s = is.readLine();
            Log.e("result", s);
            return new JSONObject(s);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
