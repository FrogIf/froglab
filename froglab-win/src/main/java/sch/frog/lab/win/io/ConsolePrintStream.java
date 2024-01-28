package sch.frog.lab.win.io;

import sch.frog.lab.lang.handle.SystemOutputHandle;
import sch.frog.lab.win.editor.ConsoleCodeArea;

public class ConsolePrintStream implements SystemOutputHandle.PrintStream {

    private final ConsoleCodeArea consoleCodeArea;

    public ConsolePrintStream(ConsoleCodeArea consoleCodeArea) {
        this.consoleCodeArea = consoleCodeArea;
    }

    @Override
    public void print(String str) {
        this.consoleCodeArea.output(str);
    }

    @Override
    public void println(String str) {
        this.consoleCodeArea.output(str + "\n");
    }
}
