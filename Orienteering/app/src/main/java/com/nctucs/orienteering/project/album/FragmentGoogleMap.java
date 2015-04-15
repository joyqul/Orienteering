package com.nctucs.orienteering.project.album;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
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

import com.nctucs.orienteering.project.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shamrock on 2015/4/10.
 */
public class FragmentGoogleMap extends android.support.v4.app.Fragment implements LocationListener{
    private static View view;
    private WebView webView;


    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if ( location != null ){
            final String setMarker = "javascript:moveMarkerTo(" +
                    location.getLatitude() + "," +
                    location.getLongitude() + ")";
            webView.loadUrl(setMarker);
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
                        if ( newProgress >= 100 ){
                            LocationManager lm = (LocationManager)getActivity().getSystemService( Context.LOCATION_SERVICE );
                            lm.requestLocationUpdates( LocationManager.GPS_PROVIDER , 500 , 1 , FragmentGoogleMap.this );
                            lm.requestLocationUpdates( LocationManager.NETWORK_PROVIDER , 1000 , 1 , FragmentGoogleMap.this );
                        }
                    }
                }
        );

        webView.getSettings().setGeolocationDatabasePath( getActivity().getFilesDir().getPath() );
        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("file:///android_asset/googlemaps.html");


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
