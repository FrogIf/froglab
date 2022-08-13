package sch.frog.kit.server.handle;

import java.lang.reflect.Method;

public class RequestActionBox {

    private Object instanceObj;

    private String path;

    private Method method;

    private String description;

    private RequestParamInfo[] params;

    public String getPath() {
        return path;
    }

    void setPath(String path) {
        this.path = path;
    }

    public Method getMethod() {
        return method;
    }

    void setMethod(Method method) {
        this.method = method;
    }

    public RequestParamInfo[] getParams() {
        return params;
    }

    void setParams(RequestParamInfo[] params) {
        this.params = params;
    }

    public Object getInstanceObj() {
        return instanceObj;
    }

    void setInstanceObj(Object instanceObj) {
        this.instanceObj = instanceObj;
    }

    public String getDescription() {
        return description;
    }

    void setDescription(String description) {
        this.description = description;
    }

    public static class Builder{
        private Object instanceObj;

        private String path;

        private Method method;

        private String description;

        private RequestParamInfo[] params;

        public static Builder newBuilder(){
            return new Builder();
        }

        public Object getInstanceObj() {
            return instanceObj;
        }

        public Builder setInstanceObj(Object instanceObj) {
            this.instanceObj = instanceObj;
            return this;
        }

        public String getPath() {
            return path;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Method getMethod() {
            return method;
        }

        public Builder setMethod(Method method) {
            this.method = method;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public RequestParamInfo[] getParams() {
            return params;
        }

        public Builder setParams(RequestParamInfo[] params) {
            this.params = params;
            return this;
        }

        public RequestActionBox build(){
            RequestActionBox box = new RequestActionBox();
            box.setDescription(description);
            box.setInstanceObj(instanceObj);
            box.setMethod(method);
            box.setPath(path);
            box.setParams(params);
            return box;
        }

    }

}
