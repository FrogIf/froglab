package sch.frog.kit.server;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

public class RequestJson extends RequestBody {

    private String json;

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

    private RequestJson(JSONObject jsonObject){
        this.jsonObject = jsonObject;
    }

    public boolean isArray(){
        return isArray;
    }

    @Override
    public String toString() {
        return this.json;
    }

}
