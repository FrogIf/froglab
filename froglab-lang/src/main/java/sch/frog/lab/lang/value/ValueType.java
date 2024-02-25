package sch.frog.lab.lang.value;

public enum ValueType {

    /**
     * null
     */
    NULL,
    /**
     * 布尔值
     */
    BOOL,
    /**
     * 数字
     */
    NUMBER,
    /**
     * 字符串
     */
    STRING,
    /**
     * 对象, 类json格式
     */
    OBJECT,
    /**
     * 数组, 类json格式
     */
    LIST,
    /**
     * 函数
     */
    FUNCTION,
    /**
     * 空值
     */
    VOID,
    /**
     * 未定义
     */
    UNDEFINE,
    /**
     * 符号引用
     */
    SYMBOL, // TODO 符号类型
    /**
     * 句柄
     */
    HANDLE; // TODO 句柄类型

}
