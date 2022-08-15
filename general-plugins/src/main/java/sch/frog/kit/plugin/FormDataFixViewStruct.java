package sch.frog.kit.plugin;

import sch.frog.kit.common.ExternalViewStruct;
import sch.frog.kit.common.FieldInfo;
import sch.frog.kit.common.LogKit;
import sch.frog.kit.common.StringMap;
import sch.frog.kit.common.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class FormDataFixViewStruct implements ExternalViewStruct {

    private final ArrayList<FieldInfo> inputField = new ArrayList<>();

    private final ArrayList<FieldInfo> outputField = new ArrayList<>();

    private static final String INPUT_FORM_DATA = "formData";

    private static final String OUTPUT_TEXT = "fixText";

    public FormDataFixViewStruct() {
        inputField.add(FieldInfo.Builder.newBuilder().setName(INPUT_FORM_DATA).setDescription("原始kv文本").build());
        outputField.add(FieldInfo.Builder.newBuilder().setName(OUTPUT_TEXT).setDescription("修正后的文本").build());
    }

    @Override
    public String getDescription() {
        return "表单kv数据修正";
    }

    @Override
    public List<FieldInfo> getInputField() {
        return inputField;
    }

    @Override
    public List<FieldInfo> getOutputFiled() {
        return outputField;
    }

    @Override
    public StringMap execute(StringMap params) {
        String formData = params.get(INPUT_FORM_DATA);
        if(StringUtil.isBlank(formData)){
            LogKit.error("no text input");
            return null;
        }
        formData = formData.trim();
        String[] kvs = formData.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String kv : kvs) {
            int i = kv.indexOf(":");
            if(i > 0){
                String key = kv.substring(0, i).trim();
                String value = kv.substring(i + 1).trim();
                sb.append(key).append(':').append(value).append('\n');
            }
        }
        StringMap result = new StringMap();
        result.put(OUTPUT_TEXT, sb.toString());
        return result;
    }

    @Override
    public String getViewName() {
        return "FormDataFix";
    }
}
