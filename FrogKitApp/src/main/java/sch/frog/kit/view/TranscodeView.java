package sch.frog.kit.view;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import sch.frog.kit.common.CustomViewControl;
import sch.frog.kit.server.handle.annotation.RequestAction;
import sch.frog.kit.server.handle.annotation.RequestParam;
import sch.frog.kit.view.transcode.TranscodeSubView;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

public class TranscodeView extends CustomViewControl {

    private HashMap<String, TranscodeSubView.ITransfer> transferMap;

    @FXML
    private TabPane containerPane;

    @Override
    public void init() {
        this.initTab();
    }

    public void initTab(){
        this.transferMap = new HashMap<>();
        TranscodeSubView.ITransfer unicodeTransfer = new TranscodeSubView.ITransfer() {
            @Override
            public String encode(String content) throws Exception {
                int len = content.length();
                StringBuilder sb = new StringBuilder(len);
                for (int i = 0; i < len; i++) {
                    char ch = content.charAt(i);
                    if (ch <= 127) {
                        sb.append(ch);
                    } else {
                        String hex = Integer.toHexString(ch);
                        sb.append("\\u");
                        if (hex.length() < 4) {
                            sb.append("0000", hex.length(), 4);
                        }
                        sb.append(hex);
                    }
                }
                return sb.toString();
            }

            @Override
            public String decode(String content) throws Exception {
                int len = content.length();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < len; i++) {
                    char ch = content.charAt(i);
                    if (ch == '\\' && i + 5 < len && content.charAt(i + 1) == 'u') {
                        String hex = content.substring(i + 2, i + 6);
                        sb.append((char) Integer.parseInt(hex, 16));
                        i += 5;
                    } else {
                        sb.append(ch);
                    }
                }
                return sb.toString();
            }
        };
        addTab("unicode", new TranscodeSubView(unicodeTransfer));

        TranscodeSubView.ITransfer urlTransfer = new TranscodeSubView.ITransfer() {
            @Override
            public String encode(String content) throws Exception {
                return URLEncoder.encode(content, StandardCharsets.UTF_8);
            }

            @Override
            public String decode(String content) throws Exception {
                return URLDecoder.decode(content, StandardCharsets.UTF_8);
            }
        };
        addTab("url", new TranscodeSubView(urlTransfer));
        TranscodeSubView.ITransfer base64Transfer = new TranscodeSubView.ITransfer() {
            @Override
            public String encode(String content) throws Exception {
                return Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public String decode(String content) throws Exception {
                return new String(Base64.getDecoder().decode(content.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
            }
        };
        addTab("base64", new TranscodeSubView(base64Transfer));
        TranscodeSubView.ITransfer hexTransfer = new TranscodeSubView.ITransfer() {
            @Override
            public String encode(String content) throws Exception {
                byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < bytes.length; i++) {
                    byte b = bytes[i];
                    sb.append(String.format("%02X", b));
                    sb.append(' ');
                }
                return sb.toString();
            }

            @Override
            public String decode(String content) throws Exception {
                content = content.replaceAll(" ", "");
                int len = content.length();
                byte[] bytes = new byte[len >> 1];
                for(int j = 0, i = 0; j < bytes.length; i += 2, j++){
                    bytes[j] = (byte)Integer.parseInt(content.substring(i, i + 2), 16);
                }
                return new String(bytes, StandardCharsets.UTF_8);
            }
        };
        addTab("hex", new TranscodeSubView(hexTransfer));

        transferMap.put("unicode", unicodeTransfer);
        transferMap.put("base64", base64Transfer);
        transferMap.put("url", urlTransfer);
        transferMap.put("hex", hexTransfer);
    }

    private void addTab(String name, TranscodeSubView view){
        Tab tab = new Tab(name);
        tab.setClosable(false);
        tab.setContent(view);
        this.containerPane.getTabs().add(tab);
    }

    @RequestAction(path = "/transcode/encode", description = "encode text to specified code")
    public String encode(@RequestParam(name = "content", description = "plain content") String content,
                       @RequestParam(name = "code", description = "code algorithm") String code){
        if(content == null){ return null; }
        TranscodeSubView.ITransfer transfer = transferMap.get(code);
        if(transfer == null){
            throw new IllegalArgumentException("not support code algorithm");
        }
        try {
            return transfer.encode(content);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }

    @RequestAction(path = "/transcode/decode", description = "decode text to specified code")
    public String decode(@RequestParam(name = "content", description = "after encode content") String content,
                         @RequestParam(name = "code", description = "code algorithm") String code){
        if(content == null){ return null; }
        TranscodeSubView.ITransfer transfer = transferMap.get(code);
        if(transfer == null){
            throw new IllegalArgumentException("not support code algorithm");
        }
        try {
            return transfer.decode(content);
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage());
        }
    }
}
