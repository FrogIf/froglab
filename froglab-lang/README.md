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

## 语法(EBNF)

```bnf
digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9";
digit_non_zero = "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9";
decimal_part = "." , digit , [ "_", digit ] 
                | "." , "_" , digit
                ;    (* "_"后面是无限循环部分 *)
number = "0" 
        | ["-"], digit_non_zero, digit , [ decimal_part ] 
        | ["-"], "0.", decimal_part
        ;
character = "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
        | "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z";
indentifier = character, [{character | digit | "-" | "_"}];
all_character = ?all visible characters?;
string = '"', {all_character - '"'}, '"';
bool = "true" | "false";
null = "null";
operator = "+" | "-" | "*" | "/" | "<" | ">" | "<=" | ">=" | "==" | "!=" | "&&" | "||" | "|" | "&";
comment = "//" , {all_character - ?newline?} , ?newline?
        | "/*", {all_characher - "*/"} , "*/";

expression_list = expression, {",", expression};
function_caller = "(", {expression_list}, ")";
function_call = indentifier, function_caller , { object_caller | array_index | function_caller };
object_caller = "." , indentifier, { function_caller };
array_indentifier_call = indentifier, array_index, { object_caller | array_index | function_caller };
array_direct_call = array, array_index, { object_caller | array_index | function_caller };
array_call =  array_indentifier_call | array_direct_call;
object_indentifier_call = indentifier, object_caller , { object_caller | array_index | function_caller };
object_direct_call = object, object_caller, { object_caller | array_index | function_caller };
object_call = object_indentifier_call | object_direct_call;
indentifier_call = function_call | array_indentifier_call | object_indentifier_call | indentifier;
plain_value_expression = number | string | bool | indentifier_call | array_direct_call | object_direct_call;
simple_value_expression = plain_value_expression | operate_expression;
formal_parameter_list = indentifier, {"," , indentifier};
function_declare = "(", formal_parameter_list, ")", "=>", expression;
expression_group = "(", expression, ")";
array_expression = array , { object_caller | array_index };
object_expression = object, { object_caller } 
expression = array_expression
        | object_expression
        | simple_value_expression 
        | expression_group
        | function_declare, { function_caller }
        ;
array = "[", {expression_list}, "]";
entry = indentifier , ":", expression;
entry_list = entry, { ",", entry };
object = "{" , {entry_list}, "}";
array_index = "[", simple_value_expression, "]";
operate_expression = plain_value_expression , operator , simple_value_expression;
```