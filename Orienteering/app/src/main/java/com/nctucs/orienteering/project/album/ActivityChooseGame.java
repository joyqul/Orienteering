package com.nctucs.orienteering.project.album;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.nctucs.orienteering.project.HttpConnection.HttpConnection;
import com.nctucs.orienteering.project.JSONMsg.JSONType;
import com.nctucs.orienteering.project.R;
import com.nctucs.orienteering.project.tcpSocket.tcpSocket;

import org.json.JSONObject;

public class ActivityChooseGame extends Activity{

    SharedPreferences userData = null;
    String token;
    private GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
        userData = getSharedPreferences("userData", MODE_PRIVATE);
        token = userData.getString("token", null);
        new GetGameListThread().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_choose_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_settings){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetGameListThread extends Thread implements Runnable{
        @Override
        public void run () {
            try {
                HttpConnection connection = new HttpConnection();
                //tcpSocket socket = new tcpSocket();
                JSONObject json = new JSONType(4);
                json.put("token", token);
                connection.send(json);
                //socket.send(json);
                JSONObject result = connection.recieve();
                //JSONObject result = socket.recieve();
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("json", result.toString());
                msg.setData(bundle);
                handler.sendMessage(msg);
            }
            catch ( Exception e ){
                e.printStackTrace();
            }
        }
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                Bundle bundle = msg.getData();
                JSONObject json = new JSONObject(bundle.getString("json"));
                int gameCnt = json.getInt("gameCnt");
                gridView = (GridView)findViewById(R.id.gv_choose_game);
                gridView.setNumColumns(1);
                String[] gameList = new String[gameCnt];
                for(int i=0;i<gameCnt;i++) gameList[i] = json.getString("game"+i);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityChooseGame.this,
                        android.R.layout.simple_list_item_1, gameList);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(itemclick);
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        AdapterView.OnItemClickListener itemclick = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                Intent intent = new Intent(ActivityChooseGame.this, ActivityLogin.class);
                intent.putExtra("gameId", i);
                startActivity(intent);
                ActivityChooseGame.this.finish();
            }
        };
    };
}


