package sch.frog.kit.server;

import com.alibaba.fastjson2.JSON;

public class ResponseJson {

    public static final int CODE_SUCCESS = 200;

    public static final int CODE_ERROR = 500;

    public static final int CODE_NOT_FOUND = 404;

    public static final int CODE_BAD_REQUEST = 400;

    public static final int CODE_FORBIDDEN = 403;

    public final int code;

    private final String message;

    private Object data;

    public static final ResponseJson SUCCESS = new ResponseJson(CODE_SUCCESS, "success");

    public static final ResponseJson ERROR = new ResponseJson(CODE_ERROR, "internal error");

    public static final ResponseJson NOT_FOUND = new ResponseJson(CODE_NOT_FOUND, "not found");

    public ResponseJson(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toString(){
        return this.toJson();
    }

    public String toJson(){
        return JSON.toJSONString(this);
    }
}
