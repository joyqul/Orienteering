

package com.nctucs.orienteering.project.album;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

import com.nctucs.orienteering.project.JSONMsg.JSONType;
import com.nctucs.orienteering.project.R;
import com.nctucs.orienteering.project.tcpSocket.tcpSocket;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FragmentLeaveMessage extends android.support.v4.app.Fragment implements View.OnClickListener {

    static View view;
    EditText inputText;
    Button submit , cancel;
    SharedPreferences userData;
    String token;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        inputText = (EditText) view.findViewById( R.id.editText );
        submit = (Button)view.findViewById( R.id.send_button );
        submit.setOnClickListener( this );
        cancel = (Button)view.findViewById( R.id.back_button );
        cancel.setOnClickListener( this );

    }

    private class LeaveMessageThread extends Thread implements  Runnable {
        private String toSend;

        public LeaveMessageThread(String msg) {
            toSend = msg;
        }

        @Override
        public void run() {
            super.run();
            try {
                tcpSocket socket = new tcpSocket();
                JSONType json = new JSONType(3);
                json.put("token", token);
                json.put("msg", toSend);
                socket.send(json);
            }
            catch ( Exception e ){
                e.printStackTrace();
            }
        }
    };


    @Override
    public void onClick(View v) {
        if ( v.getId() == R.id.send_button ){
            Log.e("input" , inputText.getText().toString() );
            String content = inputText.getText().toString();
            userData = getActivity().getSharedPreferences("userData", Context.MODE_PRIVATE);
            token = userData.getString("token", null);
            LeaveMessageThread t = new LeaveMessageThread(content);
            t.start();
        }
        else{
            inputText.setText( "" );
        }

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_leave_msg, container, false);
        } catch (Exception e) {
        /* map is already there, just return view as it is */
        }
        return view;
    }
}
