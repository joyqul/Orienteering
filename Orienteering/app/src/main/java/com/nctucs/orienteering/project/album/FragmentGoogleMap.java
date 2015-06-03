package com.nctucs.orienteering.project.album;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.nctucs.orienteering.project.JSONMsg.JSONType;
import com.nctucs.orienteering.project.R;
import com.nctucs.orienteering.project.tcpSocket.tcpSocket;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by Shamrock on 2015/4/10.
 */
public class FragmentGoogleMap extends android.support.v4.app.Fragment implements LocationListener{
    private static View view;
    private static Location lastKnowLocation = null;
    private WebView webView;
    boolean loadFinish = false;
    boolean updateLocation = false;
    boolean resetPosition = false;
    private SharedPreferences sharedPreferences;
    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if ( location != null && loadFinish ){
            Log.e("locationChanged", location.getLatitude()+" "+location.getLongitude());
            final String setMarker = "javascript:moveMarkerTo(" +
                    location.getLatitude() + "," +
                    location.getLongitude() + ")";
            lastKnowLocation = location;
            webView.loadUrl(setMarker);

            if ( resetPosition ){
                final String goToCurrentPosition = "javascript:goToCurrentPosition()";
                webView.loadUrl( goToCurrentPosition );
                resetPosition = false;
            }

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        webView = (WebView)view.findViewById( R.id.googlemapWebView );

        webView.setWebChromeClient(
                new WebChromeClient(){
                    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                        callback.invoke(origin, true, false);

                    }

                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        super.onProgressChanged(view, newProgress);
                        if ( newProgress >= 100 )
                            loadFinish = true;
                    }
                }
        );

        webView.getSettings().setGeolocationDatabasePath( getActivity().getFilesDir().getPath() );
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("file:///android_asset/googlemaps.html");


    }

    private android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                JSONObject json = new JSONObject(msg.getData().getString("json"));
                int playerCnt = json.getInt("playerCnt");
                int hintCnt = json.getInt("hintCnt");
                int msgCnt = json.getInt("msgCnt");
                int keyCnt = json.getInt("keyCnt");

                for (int i = 0; i < playerCnt; i++) {
                    JSONObject subJson = json.getJSONObject("player" + i);
                    final String setMarker = "javascript:setPlayerMarker(" +
                            i + "," +
                            subJson.getDouble("lat") + "," +
                            subJson.getDouble("long") + ")";
                    webView.loadUrl(setMarker);
                }
                for (int i = playerCnt; i < 4; i++) {
                    final String setMarker = "javascript:setPlayerMarker(" +
                            i + "," +
                            0 + "," +
                            0 + ")";
                    webView.loadUrl(setMarker);
                }


                int oriMsgCnt = sharedPreferences.getInt("msgCnt", 0);
                for (int i = 0; i < msgCnt; i++) {
                    JSONObject subJson = json.getJSONObject("msg" + i);
                    sharedPreferences.edit().putString("msg" + (i + oriMsgCnt) + "Content", subJson.getString("content")).apply();
                    sharedPreferences.edit().putFloat("msg" + (i + oriMsgCnt) + "Lat", (float) subJson.getDouble("lat")).apply();
                    sharedPreferences.edit().putFloat("msg" + (i + oriMsgCnt) + "Long", (float) subJson.getDouble("long")).apply();

                }
                int newMsgCnt = oriMsgCnt + msgCnt;
                sharedPreferences.edit().putInt("msgCnt", newMsgCnt).apply();
                for (int i = 0; i < newMsgCnt; i++) {
                    final String setMessage = "javascript:setMessageMarker(" +
                            i + "," +
                            sharedPreferences.getFloat("msg" + i + "Lat", 0) + "," +
                            sharedPreferences.getFloat("msg" + i + "Long", 0) + ",\"" +
                            sharedPreferences.getString("msg" + i + "Content" , "NULL") +
                            "\")";
                    webView.loadUrl(setMessage);
                }

                int oriHintCnt = sharedPreferences.getInt("hintCnt", 0);
                for (int i = 0; i < hintCnt; i++)
                    sharedPreferences.edit().putString("hint" + (i + oriHintCnt), json.getString("hint" + i)).apply();
                sharedPreferences.edit().putInt("hintCnt", oriHintCnt + hintCnt).apply();

                for(int i=0;i<keyCnt;i++){
                    JSONObject subJson = json.getJSONObject("key" + i);
                    int index = subJson.getInt("index");
                    String pwd = subJson.getString("pwd");
                    sharedPreferences.edit().putString("key" + index, pwd).apply();
                }
            }
            catch ( Exception e ){
                e.printStackTrace();
            }



        }
    };


    private class UpdateLocationsThread extends Thread implements Runnable{
        @Override
        public void run() {
            try {
                tcpSocket socket = new tcpSocket();

                while ( updateLocation ) {

                    if ( lastKnowLocation == null ) continue;

                    JSONType jsonType = new JSONType(2);

                    jsonType.put("lat", lastKnowLocation.getLatitude());
                    jsonType.put("long", lastKnowLocation.getLongitude());
                    jsonType.put("token", sharedPreferences.getString("token", null));

                    socket.send(jsonType);


                    JSONObject json = socket.recieve();
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putString( "json" , json.toString() );
                    msg.setData( bundle );
                    handler.sendMessage( msg );


                    Thread.sleep( 5000 );
                }
            }
            catch ( Exception e ){
                e.printStackTrace();
            }

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        sharedPreferences = getActivity().getSharedPreferences("userData" , Context.MODE_PRIVATE);
        LocationManager lm = (LocationManager)getActivity().getSystemService( Context.LOCATION_SERVICE );
        lm.requestLocationUpdates( LocationManager.GPS_PROVIDER , 500 , 1 , FragmentGoogleMap.this );
        lm.requestLocationUpdates( LocationManager.NETWORK_PROVIDER , 1000 , 1 , FragmentGoogleMap.this );



        if ( lastKnowLocation != null ){


            resetPosition = true;
        }


        updateLocation = true;
        new UpdateLocationsThread().start();

    }

    @Override
    public void onStop() {
        super.onStop();
        LocationManager lm = (LocationManager)getActivity().getSystemService( Context.LOCATION_SERVICE );
        lm.removeUpdates( FragmentGoogleMap.this );
        updateLocation = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_google_map, container, false);
        } catch (Exception e) {
        /* map is already there, just return view as it is */
        }
        return view;

    }
}
