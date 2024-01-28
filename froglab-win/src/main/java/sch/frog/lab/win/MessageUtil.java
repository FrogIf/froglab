package sch.frog.lab.win;

public class MessageUtil {

    static MessageEmitter messageEmitter;

    public static void info(String msg){
        messageEmitter.emitInfo(msg);
    }

    public static void warn(String msg){
        messageEmitter.emitWarn(msg);
    }

    public static void error(String msg){
        messageEmitter.emitError(msg);
    }

    public static void clear(){
        messageEmitter.clear();
    }

}
