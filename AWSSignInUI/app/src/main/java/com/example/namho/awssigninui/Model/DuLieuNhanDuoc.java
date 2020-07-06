package com.example.namho.awssigninui.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class DuLieuNhanDuoc {
     static final String STATEJS = "state";
     static final String DESIREDJS = "desired";
     static final String BLUE_VALUE = "Blue_value";
     static final String RED_VALUE = "Red_value";
     static final String YELLOW_VALUE = "Yellow_value";
     static final String GREEN_VALUE = "Green_value";

    JSONObject rootJS, stateJS, desiredJS;
    String blueValue, redValue, greenValue, yellowValue;
    String nhanDuoc;

    public DuLieuNhanDuoc(String msg) throws JSONException {
        rootJS = new JSONObject(msg);
        stateJS = rootJS.getJSONObject(STATEJS);
        desiredJS = stateJS.getJSONObject(DESIREDJS);
    }

    public JSONObject getRootJS() {
        return rootJS;
    }

    public String getNhanDuoc() {
        return nhanDuoc;
    }

    public void setNhanDuoc(String nhanDuoc) {
        this.nhanDuoc = nhanDuoc;
    }
    public void setRootJS(JSONObject rootJS) {
        this.rootJS = rootJS;
    }

    public JSONObject getStateJS() {
        return stateJS;
    }

    public void setStateJS(JSONObject stateJS) {
        this.stateJS = stateJS;
    }

    public JSONObject getDesiredJS() {
        return desiredJS;
    }

    public void setDesiredJS(JSONObject desiredJS) {
        this.desiredJS = desiredJS;
    }

    public String getBlueValue() {
        return blueValue;
    }

    public void setBlueValue(String blueValue) {
        this.blueValue = blueValue;
    }

    public String getRedValue() {
        return redValue;
    }

    public void setRedValue(String redValue) {
        this.redValue = redValue;
    }

    public String getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(String greenValue) {
        this.greenValue = greenValue;
    }

    public String getYellowValue() {
        return yellowValue;
    }

    public void setYellowValue(String yellowValue) {
        this.yellowValue = yellowValue;
    }

    public void XulyDuLieu(JSONObject JS) throws JSONException {
        if(JS != null) {
            if (JS.has(BLUE_VALUE)) {
                blueValue = JS.getString(BLUE_VALUE);
            }
            if (JS.has(RED_VALUE)) {
                redValue = JS.getString(RED_VALUE);
            }
            if (JS.has(YELLOW_VALUE)) {
                greenValue = JS.getString(GREEN_VALUE);
            }
            if (JS.has(GREEN_VALUE)) {
                yellowValue = JS.getString(YELLOW_VALUE);
            }
        }
    }
}
