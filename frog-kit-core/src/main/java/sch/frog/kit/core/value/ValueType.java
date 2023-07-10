package sch.frog.kit.core.value;

public enum ValueType {

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
     * 标识符
     */
    VARIABLE,
    /**
     * 空值
     */
    VOID;

}
