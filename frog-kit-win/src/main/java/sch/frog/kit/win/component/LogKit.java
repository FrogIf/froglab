package sch.frog.kit.win.component;

public class LogKit {

    private static ILoggerPrinter printer;

    public static void init(ILoggerPrinter printer){
        if(LogKit.printer != null){
            throw new IllegalStateException("log kit has been init.");
        }else{
            LogKit.printer = printer;
        }
    }

    public static void info(String msg){
        printer.info(msg);
    }

    public static void warn(String msg){
        printer.warn(msg);
    }

    public static void error(String msg){
        printer.error(msg);
    }

    public interface ILoggerPrinter{
        void info(String msg);
        void warn(String msg);
        void error(String msg);
    }
}
