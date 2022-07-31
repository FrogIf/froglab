package sch.frog.kit.server;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public class RequestBody {

    protected boolean isArray;

    protected JSONArray jsonArray;

    protected JSONObject jsonObject;

    protected String simpleValue;

    protected RequestBody() {
        // do nothing
    }

    public RequestBody(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
        this.isArray = true;
    }

    public RequestBody(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
        this.isArray = false;
    }

    public RequestBody(String simpleValue) {
        this.simpleValue = simpleValue;
    }

    public boolean isArray() {
        return isArray;
    }

    public JSONArray getJsonArray() {
        return jsonArray;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public RequestBody get(String name){
        JSONObject jsonObject = this.jsonObject.getJSONObject(name);
        if(jsonObject != null){
            return new RequestBody(jsonObject);
        }else{
            JSONArray jsonArray = this.jsonObject.getJSONArray(name);
            if(jsonArray != null){
                return new RequestBody(jsonArray);
            }else{
                String value = this.jsonObject.getString(name);
                if(value == null){ return null; }
                return new RequestBody(value);
            }
        }
    }

    public String getStringValue(String param){
        return this.jsonObject.getString(param);
    }
}
