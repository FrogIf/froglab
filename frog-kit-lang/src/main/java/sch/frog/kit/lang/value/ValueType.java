package sch.frog.kit.lang.value;

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
     * 符号引用
     */
    SYMBOL,
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
     * 句柄
     */
    HANDLE;

}
