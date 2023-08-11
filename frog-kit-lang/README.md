# 语法介绍

## 内置函数

函数调用方式:

```scheme
date(now(), 'yyyy-MM-dd')
```

* now() -- 获取当前时间, 返回时间戳毫秒值
* date(timestamp, pattern) -- 格式化时间戳, 返回格式化的字符串
  * 如果不传pattern, 默认使用yyyy-MM-dd HH:mm:ss
  * 如果不传timestamp, 默认使用当前时间
* uuid(true/false) -- 随机生成uuid, true/false为可选参数, 不传或者传false都是直接生成, 传true则会将生成uuid中的-去掉
* exec(args...) -- 可以传无限个参数, 会顺序执行每一个参数对应的表达式
* print(arg) -- 输出参数到终端
* call(funName, args...) -- 调用指定的方法
* get(var) -- 获取某个变量的值
* set(var, val) -- 设置某个变量的值

## 自定义函数

自定义函数的写法示例如下:

```scheme
(a, b) => date(a, b)
```

## 复杂示例

```scheme
define(bbb, {aaa: {bbb: [1, (a, b) => begin(print(a), print(b), (a, b) => date(a, b))]}}).aaa.bbb[1]('aaa', 'bbb')(now(), 'yyyy-MM-dd')
```

## 计划

1. 目前符号系统混乱, 需要改造