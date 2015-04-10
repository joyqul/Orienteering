package com.nctucs.orienteering.project.album;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import orienteering.album.R;

/**
 * Created by Shamrock on 2015/4/10.
 */
public class FragmentGoogleMap extends android.support.v4.app.Fragment{
    private static View view;
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
