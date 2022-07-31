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

    public static class RequestParamInfo{
        private boolean isRequired;
        private Class<?> type;
        private String name;
        private String description;

        public boolean isRequired() {
            return isRequired;
        }

        void setRequired(boolean required) {
            isRequired = required;
        }

        public Class<?> getType() {
            return type;
        }

        void setType(Class<?> type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        void setDescription(String description) {
            this.description = description;
        }
    }

}
