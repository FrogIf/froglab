module sch.frog.kit {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires io.netty.transport;
    requires io.netty.codec.http;
    requires io.netty.buffer;
    requires com.alibaba.fastjson2;

    opens sch.frog.kit to javafx.fxml;
    exports sch.frog.kit;
    exports sch.frog.kit.view;
    opens sch.frog.kit.view to javafx.fxml;
    exports sch.frog.kit.exception;
    opens sch.frog.kit.exception to javafx.fxml;
}