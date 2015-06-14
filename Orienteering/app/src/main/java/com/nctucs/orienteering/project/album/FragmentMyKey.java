package com.nctucs.orienteering.project.album;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nctucs.orienteering.project.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shamrock on 2015/6/14.
 */
public class FragmentMyKey extends android.support.v4.app.Fragment{
    private View view;
    private TextView txtKey;
    private Button vuforia, final_key;
    private EditText input;

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Log.e("get Another app info","get");
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new PackageManager.NameNotFoundException();
            }

            context.startActivity(i);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        txtKey = (TextView)getView().findViewById( R.id.text_key );
        vuforia = (Button) getView().findViewById( R.id.button_vuforia );
        vuforia.setOnClickListener(
            new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    openApp( getActivity() , "com.orienteering.orienteering" );
                }
            }
        );
        final_key = (Button) getView().findViewById( R.id.button_check_ans );
        final_key.setOnClickListener(
               new Button.OnClickListener(){
                   @Override
                   public void onClick(View v) {
                       AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
                       input = new EditText( getActivity() );
                       builder.setView( input );
                       builder.setTitle( "Input the thing you see in vuforia" );
                       builder.setPositiveButton("Confirm",
                               new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialog, int which) {
                                       if ( input.getText().toString().equals("treasure") ){
                                           Intent intent = new Intent( getActivity() , ActivityGG.class );
                                           startActivity( intent );
                                           getActivity().finish();
                                       }
                                       else{
                                           Toast.makeText( getActivity() , "invalid password" , Toast.LENGTH_SHORT ).show();
                                       }
                                   }
                               }

                               );
                        builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                       builder.create().show();

                   }
               }

        );
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences( "userData" , Context.MODE_PRIVATE );
        int ans_len = sharedPreferences.getString( "answer" , "" ).length();
        String display = "";
        for ( int i = 0 ; i < ans_len ; i++ ) {
            display += sharedPreferences.getString("key"+i,"_");

            if ( i != ans_len-1 ) display += " ";
        }
        txtKey.setText( display );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.fragment_my_key , container, false);
        } catch (Exception e) {
        /* map is already there, just return view as it is */
        }
        return view;
    }


}
