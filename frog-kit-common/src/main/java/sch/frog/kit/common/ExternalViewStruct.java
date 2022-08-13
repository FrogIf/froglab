package sch.frog.kit.common;

import java.util.List;

public interface ExternalViewStruct {

    String EXECUTE_FUNCTION_NAME = "execute";

    String getDescription();

    List<FieldInfo> getInputField();

    List<FieldInfo> getOutputFiled();

    StringMap execute(StringMap params);

    String getViewName();
}
