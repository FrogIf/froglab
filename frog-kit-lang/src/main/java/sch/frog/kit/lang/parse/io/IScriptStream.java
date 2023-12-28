package sch.frog.kit.lang.parse.io;

public interface IScriptStream {

    /**
     * 流结束
     */
    char EOF = 0;

    /**
     * 当前读到的字符串
     */
    char current();

    /**
     * 当前位置
     */
    int pos();

    /**
     * 预看下一个字符
     */
    char peek();

    /**
     * 游标移动到下一个, 并返回移动后的当前值
     * 当返回EOF表示读取结束
     */
    char next();

}
