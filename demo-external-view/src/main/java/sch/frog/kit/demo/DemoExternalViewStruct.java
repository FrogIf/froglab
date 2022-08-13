package sch.frog.kit.demo;

import sch.frog.kit.common.ExternalViewStruct;
import sch.frog.kit.common.FieldInfo;
import sch.frog.kit.common.StringMap;
import sch.frog.kit.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class DemoExternalViewStruct implements ExternalViewStruct {

    private final ArrayList<FieldInfo> inputField = new ArrayList<>();

    private final ArrayList<FieldInfo> outputField = new ArrayList<>();

    public DemoExternalViewStruct() {
        inputField.add(FieldInfo.Builder.newBuilder().setName("input").setDescription("这是一个字段").build());

        outputField.add(FieldInfo.Builder.newBuilder().setName("output").setDescription("这是输出结果").build());
    }

    @Override
    public String getDescription() {
        return "演示应用";
    }

    @Override
    public List<FieldInfo> getInputField() {
        return this.inputField;
    }

    @Override
    public List<FieldInfo> getOutputFiled() {
        return this.outputField;
    }

    @Override
    public StringMap execute(StringMap params) {
        String input = params.get("input");
        if(StringUtil.isBlank(input)){ return null; }

        StringMap map = new StringMap();
        map.put("output", input);
        return map;
    }

    @Override
    public String getViewName() {
        return "demo";
    }

}
