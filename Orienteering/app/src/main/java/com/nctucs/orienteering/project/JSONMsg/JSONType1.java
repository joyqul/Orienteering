package com.nctucs.orienteering.project.JSONMsg;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by deepshine on 2015/4/6.
 */

public class JSONType1 extends JSONObject{

    public JSONType1() throws JSONException {
        put("jsonType", 1);
    }

}
