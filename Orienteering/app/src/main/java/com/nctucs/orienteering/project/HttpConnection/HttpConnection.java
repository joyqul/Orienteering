package com.nctucs.orienteering.project.HttpConnection;

import android.util.Log;

import com.nctucs.orienteering.project.Param.ServerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yu ShangHsin on 2015/6/20.
 */

public class HttpConnection{

    DataInputStream is;
    DataOutputStream os;
    HttpURLConnection connection;

    public HttpConnection() throws IOException{
        connection = (HttpURLConnection) new URL(ServerInfo.URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        is = new DataInputStream(connection.getInputStream());
        os = new DataOutputStream(connection.getOutputStream());
    }

    public void send(JSONObject obj) {
        try {
            Log.e("send", obj.toString());
            os.write( obj.toString().getBytes("UTF-8") );
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
