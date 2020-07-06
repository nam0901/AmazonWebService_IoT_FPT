package com.example.namho.awssigninui.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class DuLieuGuiDi {
    static final String STATEJS = "state";
    static final String DESIREDJS = "desired";
    public static final String BLUE_VALUE = "Blue_value";
    public static final String RED_VALUE = "Red_value";
    public static final String YELLOW_VALUE = "Yellow_value";
    public static final String GREEN_VALUE = "Green_value";
    static final String GUI_DI = "{\"state\": {\"desired\": {\"Led_value\": \"NULL\",\"Blue_value\": \"NULL\",\"Red_value\": \"NULL\",\"Yellow_value\": \"NULL\",\"Green_value\": \"NULL\"}}}";

    JSONObject rootJSGui, stateJSGui, desiredJSGui;
    String blueValueGui, redValueGui, greenValueGui, yellowValueGui;
    String guiDi;

    public DuLieuGuiDi() throws JSONException {
        rootJSGui = new JSONObject(GUI_DI);
        stateJSGui = rootJSGui.getJSONObject(STATEJS);
        desiredJSGui = stateJSGui.getJSONObject(DESIREDJS);
    }

    public JSONObject getRootJSGui() {
        return rootJSGui;
    }

    public void setRootJSGui(JSONObject rootJSGui) {
        this.rootJSGui = rootJSGui;
    }

    public JSONObject getStateJSGui() {
        return stateJSGui;
    }

    public void setStateJSGui(JSONObject stateJSGui) {
        this.stateJSGui = stateJSGui;
    }

    public JSONObject getDesiredJSGui() {
        return desiredJSGui;
    }

    public void setDesiredJSGui(JSONObject desiredJSGui) {
        this.desiredJSGui = desiredJSGui;
    }

    public String getBlueValueGui() {
        return blueValueGui;
    }

    public void setBlueValueGui(String blueValueGui) {
        this.blueValueGui = blueValueGui;
    }

    public String getRedValueGui() {
        return redValueGui;
    }

    public void setRedValueGui(String redValueGui) {
        this.redValueGui = redValueGui;
    }

    public String getGreenValueGui() {
        return greenValueGui;
    }

    public void setGreenValueGui(String greenValueGui) {
        this.greenValueGui = greenValueGui;
    }

    public String getYellowValueGui() {
        return yellowValueGui;
    }

    public void setYellowValueGui(String yellowValueGui) {
        this.yellowValueGui = yellowValueGui;
    }

    public String getGuiDi() {
        return guiDi;
    }

    public void setGuiDi(String guiDi) {
        this.guiDi = guiDi;
    }

    public boolean XulyJSGuiDi(String value, String tinhTrang) throws JSONException {
        if(desiredJSGui.has(value)) {
            desiredJSGui.put(value, tinhTrang);
            guiDi = rootJSGui.toString();
            return true;
        }
        return false;
    }
}
