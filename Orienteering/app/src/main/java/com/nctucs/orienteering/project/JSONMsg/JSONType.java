package com.nctucs.orienteering.project.JSONMsg;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Shamrock on 2015/4/10.
 */
public class JSONType extends JSONObject {



    public JSONType( int type ) throws JSONException{
        super();
        put( "jsonType" , 0 );
    }


}
