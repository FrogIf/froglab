package sch.frog.kit.common;

import java.util.List;

public abstract class ExternalViewStruct {

    private final String viewName;

    public ExternalViewStruct(String viewName) {
        this.viewName = viewName;
    }

    public abstract List<FieldInfo> getInputField();

    public abstract List<FieldInfo> getOutputFiled();

    public abstract List<ValueObj> execute(List<ValueObj> params);

    public String getViewName() {
        return viewName;
    }
}
