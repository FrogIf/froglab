package sch.frog.kit.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

/**
 * custom control parent class, must observe rule:
 * 1. for example class name is 'AbcDef', the fxml name must be abc-def.fxml, and the file must in view directory;
 * 2. fxml file's root must be:
 *         <fx:root type="javafx.scene.layout.VBox"  ... ></fx:root>
 */
public abstract class CustomViewControl extends VBox {

    public CustomViewControl() {
        Class<? extends CustomViewControl> clazz = getClass();
        FXMLLoader loader = new FXMLLoader(clazz.getResource(parseCamelToFxml(clazz.getSimpleName())));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    /**
     * 将驼峰命名转为约定的view
     * @param camel 驼峰名称
     * @return 转换后的文件
     */
    private String parseCamelToFxml(String camel){
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

    public void onClose(){
        // do nothing
    }

}
