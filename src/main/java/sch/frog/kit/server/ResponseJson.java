package sch.frog.kit.server;

public class ResponseJson {

    public final boolean ok;

    private final String message;

    public static final ResponseJson SUCCESS = new ResponseJson(true, "success");

    public static final ResponseJson ERROR = new ResponseJson(false, "internal error");

    public ResponseJson(boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public String toString(){
        return this.toJson();
    }

    public String toJson(){
        return "{\"success\":"+ok+",\"message\":\""+this.message+"\"}";
    }
}
