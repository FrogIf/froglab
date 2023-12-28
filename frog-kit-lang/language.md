
## 语法规则

```EBNF
digit = "0" | "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9";
digit_non_zero = "1" | "2" | "3" | "4" | "5" | "6" | "7" | "8" | "9";
decimal_part = "." , digit , [ "_", digit ] 
                | "." , "_" , digit
                ;    (* "_"后面是无限循环部分 *)
number = digit_non_zero, digit , [decimal_part] 
        | "0", [decimal_part]
        ;
character = "a" | "b" | "c" | "d" | "e" | "f" | "g" | "h" | "i" | "j" | "k" | "l" | "m" | "n" | "o" | "p" | "q" | "r" | "s" | "t" | "u" | "v" | "w" | "x" | "y" | "z"
        | "A" | "B" | "C" | "D" | "E" | "F" | "G" | "H" | "I" | "J" | "K" | "L" | "M" | "N" | "O" | "P" | "Q" | "R" | "S" | "T" | "U" | "V" | "W" | "X" | "Y" | "Z";
indentifier = character, [{character | digit | "_"}];
all_character = ?all visible characters?;
string = '"', {all_character - '"'}, '"';
bool = "true" | "false";
null = "null";
operator = "+" | "-" | "*" | "/" | "<" | ">" | "<=" | ">=" | "==" | "!=" | "&&" | "||" | "|" | "&";
comment = "//" , {all_character - ?newline?} , ?newline?
        | "/*", {all_characher - "*/"} , "*/";
assign_operator = "=";
global_declare = "var";
local_declare = "let";
statement_end = ";";

formal_parameter_list = indentifier, {"," , indentifier};
function_declare = "(", formal_parameter_list, ")", "=>", statement_block;
function_caller = "(", {expression_list}, ")";
function_direct_call = function_declare, function_caller, { object_caller | array_index | function_caller };
function_indentifier_call = indentifier, function_caller , { object_caller | array_index | function_caller };
function_call = function_indentifier_call | function_direct_call;

entry = indentifier , ":", expression;
entry_list = entry, { ",", entry };
object = "{" , {entry_list}, "}";
object_caller = "." , indentifier;
object_indentifier_call = indentifier, object_caller , { object_caller | array_index | function_caller };
object_direct_call = object, object_caller, { object_caller | array_index | function_caller };
object_call = object_indentifier_call | object_direct_call;

array = "[", {expression_list}, "]";
array_index = "[", simple_value_expression, "]";
array_indentifier_call = indentifier, array_index, { object_caller | array_index | function_caller };
array_direct_call = array, array_index, { object_caller | array_index | function_caller };
array_call =  array_indentifier_call | array_direct_call;

statements = statement, { statement_end, statement }, {statement_end};
statement = statement_block 
          | return_statement 
          | break_statement 
          | continue_statement 
          | variable_statement
          | expression
          | if_statement
          | iteration_statement;

statement_block = "{" , {statement}, "}";
return_statement = "return", {expression};
break_statement = "break";
continue_statement = "continue";
variable_statement = 'var', indentifier, { assign_operator, expression };

expression_list = expression, {",", expression};


// -----------以下未完成---------
expression = array , {array_index, { object_caller | array_index | function_caller } }
        | object, object_caller, { object_caller | array_index | function_caller }
        | simple_value_expression 
        | function_declare
        ;

indentifier_call = function_call | array_indentifier_call | object_indentifier_call | indentifier;




plain_value_expression = number | string | bool | indentifier_call | array_direct_call | object_direct_call | assign_expression;
simple_value_expression = plain_value_expression | operate_expression;
expression = array , { object_caller | array_index | function_caller }
        | object, { object_caller | array_index | function_caller }
        | simple_value_expression 
        | function_declare
        ;

operate_expression = plain_value_expression , operator , simple_value_expression;
declare_expression = {global_declare | local_declare} , indentifier;
assign_expression = declare_expression , indentifier, assign_operator, plain_value_expression
        | indentifier, assign_operator, plain_value_expression;
        
statement = statement_block 
          | return_statement 
          | break_statement 
          | continue_statement 
          | variable_statement
          | expression_statement
          | if_statement
          | iteration_statement;




expression = 
```

> start: statement 