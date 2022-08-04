package sch.frog.kit.demo;

import sch.frog.kit.common.ExternalViewStruct;
import sch.frog.kit.common.FieldInfo;
import sch.frog.kit.common.ValueObj;
import sch.frog.kit.common.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DemoExternalViewStruct extends ExternalViewStruct {

    private final ArrayList<FieldInfo> inputField = new ArrayList<>();

    private final ArrayList<FieldInfo> outputField = new ArrayList<>();

    public DemoExternalViewStruct() {
        super("demo");

        inputField.add(FieldInfo.Builder.newBuilder().setName("input").setDescription("这是一个字段").build());

        outputField.add(FieldInfo.Builder.newBuilder().setName("output").setDescription("这是输出结果").build());
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
    public List<ValueObj> execute(List<ValueObj> params) {
        String input = null;
        for (ValueObj param : params) {
            if("input".equals(param.getName())){
                input = param.getValue();
                break;
            }
        }
        if(StringUtil.isBlank(input)){ return null; }

        return Collections.singletonList(new ValueObj("output", input));
    }

}
