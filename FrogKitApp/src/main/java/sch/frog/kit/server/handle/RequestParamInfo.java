package sch.frog.kit.server.handle;

public class RequestParamInfo {

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

    public static class Builder{
        private boolean isRequired;
        private Class<?> type;
        private String name;
        private String description;
        public static Builder newBuilder(){
            return new Builder();
        }

        public boolean isRequired() {
            return isRequired;
        }

        public Builder setRequired(boolean required) {
            isRequired = required;
            return this;
        }

        public Class<?> getType() {
            return type;
        }

        public Builder setType(Class<?> type) {
            this.type = type;
            return this;
        }

        public String getName() {
            return name;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public RequestParamInfo build(){
            RequestParamInfo param = new RequestParamInfo();
            param.setDescription(description);
            param.setRequired(isRequired);
            param.setName(name);
            param.setType(type);
            return param;
        }
    }

}
