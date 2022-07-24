package sch.frog.kit.server;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

public class RequestJson {

    private String json;

    private boolean isArray;

    private JSONArray jsonArray;

    private JSONObject jsonObject;

    public RequestJson(String json) throws RequestFormatIllegalException {
        this.json = json;
        boolean valid = JSON.isValid(json);
        if(!valid){
            throw new RequestFormatIllegalException(json);
        }
        isArray = JSON.isValidArray(json);
        if(isArray){
            this.jsonArray = JSON.parseArray(json);
        }else{
            this.jsonObject = JSON.parseObject(json);
        }
    }

    public boolean isArray(){
        return isArray;
    }

    @Override
    public String toString() {
        return this.json;
    }
}
