package sch.frog.kit.common;

public class FieldInfo {

    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static class Builder{

        private String name;
        private String description;

        public static Builder newBuilder(){
            return new Builder();
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

        public FieldInfo build(){
            FieldInfo fieldInfo = new FieldInfo();
            fieldInfo.name = this.name;
            fieldInfo.description = this.description;
            return fieldInfo;
        }
    }
}
