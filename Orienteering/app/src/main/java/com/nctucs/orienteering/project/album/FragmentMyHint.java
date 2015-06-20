package com.nctucs.orienteering.project.album;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.nctucs.orienteering.project.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentMyHint extends android.support.v4.app.Fragment {
    private View view;
    private GridView gridView;
    private SimpleAdapter adapter;
    private List< Map<String,Object> > items= new ArrayList< Map<String,Object> > ();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        gridView = (GridView)view.findViewById( R.id.gv );
        adapter = new SimpleAdapter(
            getActivity(),
            items,
            R.layout.hint_element_layout,
            new String[]{"image","text"},
            new int[]{ R.id.image , R.id.text }
        );

        gridView.setAdapter( adapter );
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userData" , Context.MODE_PRIVATE);
        int hintCnt = sharedPreferences.getInt( "hintCnt" , 0 );
        Log.e("HintCnt" , hintCnt+"");
        items.clear();
        for ( int i = 0 ; i < hintCnt ; i++ ){
            Map<String,Object> map = new HashMap<String, Object>();
            map.put( "text", sharedPreferences.getString("hint"+i , "NULL") );
            map.put( "image" , R.drawable.idea);

            items.add( map );
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_my_hint, container, false);
        } catch (Exception e) {
        /* map is already there, just return view as it is */
        }
        return view;
    }
}
