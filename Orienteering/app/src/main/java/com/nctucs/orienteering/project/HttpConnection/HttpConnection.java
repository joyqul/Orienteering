package com.nctucs.orienteering.project.HttpConnection;

import android.util.Log;

import com.nctucs.orienteering.project.Param.ServerInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Yu ShangHsin on 2015/6/20.
 */

public class HttpConnection{

    //DataInputStream is;
    //DataOutputStream os;
    HttpURLConnection connection;

    public HttpConnection() throws IOException{
        connection = (HttpURLConnection) new URL(ServerInfo.URL).openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "text/plain");
    }

    public void send(JSONObject obj) {
        try {
            Log.e("send", obj.toString());
            DataOutputStream os = new DataOutputStream( connection.getOutputStream());
            os.write( obj.toString().getBytes("UTF-8") );
        }
        catch( Exception e){
            e.printStackTrace();
        }
    }

    public JSONObject recieve(){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String s = in.readLine();
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
