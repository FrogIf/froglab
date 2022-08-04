package sch.frog.kit.server;

/**
 * 请求格式非法
 */
public class RequestFormatIllegalException extends Exception{

    public RequestFormatIllegalException(String requestBody) {
        super("request body is illegal : " + (requestBody == null
                    ? "null"
                    : (
                            requestBody.length() < 10 ? requestBody : requestBody.substring(0, 10)
                    )
                )
        );
    }
}
