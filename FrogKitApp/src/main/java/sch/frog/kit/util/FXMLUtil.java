package sch.frog.kit.util;

public class FXMLUtil {

    /**
     * 将驼峰命名转为约定的view
     * @param camel 驼峰名称
     * @return 转换后的文件
     */
    public static String parseCamelToFxml(String camel){
        StringBuilder sb = new StringBuilder();
        for(int i = 0, len = camel.length(); i < len; i++){
            char c = camel.charAt(i);
            if(Character.isUpperCase(c)){
                if(i != 0){ sb.append('-'); }
                sb.append(Character.toLowerCase(c));
            }else{
                sb.append(c);
            }
        }
        sb.append(".fxml");
        return sb.toString();
    }
}
