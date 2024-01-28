package sch.frog.lab.win.editor;

import javafx.scene.control.IndexRange;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.fxmisc.richtext.model.Paragraph;
import org.fxmisc.richtext.model.StyledDocument;

import java.util.Collection;
import java.util.Collections;

public class ConsoleCodeArea extends CustomCodeArea {

    private final String prefix;

    private final int prefixLen;

    private final InputListener inputListener;

    private final HistoryChain historyChain;

    public ConsoleCodeArea(String prefix, InputListener inputListener, int historyCount) {
        init();
        this.prefix = prefix;
        this.prefixLen = this.prefix.length();
        this.appendText(prefix);
        this.inputListener = inputListener;
        this.historyChain = new HistoryChain(historyCount);
        this.setWrapText(true);
        this.caretPositionProperty().addListener((observable, o, n) -> {
            int d1 = this.deadline();
            int d2 = d1 - this.prefixLen;
            if(n >= d2 && n < d1){
                this.displaceCaret(d1);
            }
        });
        this.selectionProperty().addListener((observableValue, or, nr) -> {
            int d = this.deadline();
            if(nr.getEnd() >= d && nr.getStart() < d){
                this.selectRange(d, nr.getEnd());
            }
        });
    }

    public void init(){
        this.addEventFilter(KeyEvent.KEY_TYPED, this::keyEventFilter);
        this.addEventFilter(KeyEvent.KEY_PRESSED, this::keyEventFilter);
        this.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if(e.getCode() == KeyCode.ENTER){
                String preLine = this.getParagraph(this.getParagraphs().size() - 2).getText();
                if(preLine.startsWith(prefix)){
                    preLine = preLine.substring(prefixLen);
                }
                if(!preLine.isBlank()){
                    historyChain.add(preLine);
                    String resp = inputListener.txAndRx(preLine);
                    if(resp != null){
                        this.appendText(resp + "\n");
                    }
                }
                this.append(prefix, Collections.emptyList());
            }else if(e.isControlDown()){
                if(e.getCode() == KeyCode.L){
                    this.clearConsole();
                }
            }
            historyChain.reset();
        });
    }

    private void keyEventFilter(KeyEvent e){
        if(e.getCode() == KeyCode.BACK_SPACE){
            IndexRange range = this.getSelection();
            if(range.getLength() > 0){
                if(range.getStart() < deadline()){
                    e.consume();
                }
            }else{
                if(this.getCaretPosition() <= deadline()){
                    e.consume();
                }
            }
        } else if(e.getCode() == KeyCode.ENTER){
            int len = this.getText().length();
            this.selectRange(len, len);
            this.displaceCaret(len);
        } else if(e.getCode() == KeyCode.UP){   // 历史命令
            if(this.deadline() <= this.getCaretPosition()){
                String command = historyChain.up();
                if(command != null){
                    inputCommand(command);
                }
                e.consume();
            }
        } else if(e.getCode() == KeyCode.DOWN){ // 历史命令
            if(this.deadline() <= this.getCaretPosition()){
                String command = historyChain.down();
                if(command == null){ command = ""; }
                inputCommand(command);
                e.consume();
            }
        }
    }

    public void inputCommand(String command){
        Paragraph<Collection<String>, String, Collection<String>> lastParagraph =
                this.getParagraph(this.getParagraphs().size() - 1);
        int lastLineLen = lastParagraph.getText().length();
        int length = this.getText().length();
        int start = length - lastLineLen + this.prefixLen;
        if(start < 0 || start > length){
            return;
        }
        this.replace(start, length, command, "");
    }

    public String getActiveCommand(){
        String command = this.getParagraph(this.getParagraphs().size() - 1).getText();
        if(command.startsWith(prefix)){
            command = command.substring(prefixLen);
        }
        return command;
    }

    private int deadline(){
        int totalLen = this.getText().length();
        String text = this.getParagraph(this.getParagraphs().size() - 1).getText();
        int lastLineLen = text.length();
        int offset = text.startsWith(prefix) ? prefixLen : 0;
        return totalLen - lastLineLen + offset;
    }

    private boolean clearText = false;

    @Override
    public void replace(int start, int end, StyledDocument<Collection<String>, String, Collection<String>> replacement) {
        if(clearText){
            clearText = false;
            super.replace(start, end, replacement);
        }else{
            int deadline = this.deadline();
            boolean editable = start >= deadline;
            if(!editable){
                start = deadline;
                if(end < start){ end = start; }
                this.selectRange(deadline, deadline);
            }
            super.replace(start, end, replacement);
        }
    }

    public void output(String str) {
        this.appendText(str + "\n");
    }

    public interface InputListener{
        String txAndRx(String line);
    }

    public void clearConsole(){
        clearText = true;
        this.clear();
        this.appendText(prefix);
    }
}
