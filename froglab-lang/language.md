## 语法

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
prefix_operator = "-" | "+" | "@" | "!";
suffix_operator = "++" | "--";
operator = prefix_operator | "*" | "/" | "<" | ">" | "<=" | ">=" | "==" | "!=" | "&&" | "||" | "|" | "&" | "=";
comment = "//" , {all_character - ?newline?} , ?newline?
        | "/*", {all_characher - "*/"} , "*/";
assign_operator = "=";
global_declare = "var";
local_declare = "let";
statement_end = ";";



caller = object_caller | array_index | function_caller;

formal_parameter_list = indentifier, {"," , indentifier};
function_declare = "(", {formal_parameter_list}, ")", "=>", statement_block
         | "(", {formal_parameter_list}, ")", "=>", statement;
function_caller = "(", {expression_list}, ")", { caller };
function_direct_call = function_declare, function_caller;
function_indentifier_call = indentifier, function_caller;
function_call = function_indentifier_call | function_direct_call;
function_normal_declare = "function", "(", {formal_parameter_list}, ")", statement_block;
function_statement = "function", indentifier, "(", {formal_parameter_list}, ")", statement_block;

entry_key = indentifier | string;
entry = entry_key , ":", expression;
entry_list = entry, { ",", entry };
object = "{" , {entry_list}, "}";
object_caller = "." , indentifier, { caller };
object_obj = indentifier | object;

array = "[", {expression_list}, "]";
array_index = "[", expression, "]";
array_caller = array_index, { caller };
array_obj = indentifier | array;

statements = statement, { statement_end, statement }, {statement_end};
statement = statement_block 
          | return_statement 
          | break_statement 
          | continue_statement 
          | variable_statement
          | package_statement
          | import_statement
          | expression
          | if_statement
          | iteration_statement
          | function_statement
          ;

statement_block = "{" , {statement}, "}";
return_statement = "return", {expression};
break_statement = "break";
continue_statement = "continue";
variable_body = indentifier, { assign_operator, expression };
variable_statement = global_declare, variable_body, { variable_body }
          | local_declare, variable_body, { variable_body };
package_statement = "package", indentifier, {".", indentifier};
import_statement = "import", indentifier, {".", indentifier};

nest_statement = statement_block | statement;

if_entry = "if", "(", expression, ")", nest_statement;
else_if_entry = "else", if_entry;
else_entry = "else", nest_statement;
if_statement = if_entry, 
        { else_if_entry },
        [ else_entry ];
    
iteration_statement = do_while_statement
        | while_statement
        | for_statement;
        
for_initializer = variable_statement | variable_body;
for_statement = "for", "(", [for_initializer], ";", [expression_list], ";", [expression_list], ")", nest_statement;

while_statement = "while", "(", expression, ")", nest_statement;

do_while_statement = "do", nest_statement, "while", "(", expression, ")";


expression_list = expression, {",", expression};
array_expression = array_obj, { array_caller | object_caller };
object_expression = object_obj, { object_caller };
function_expression = function_declare, { function_caller };
function_ref_call = identifier, function_caller;
group_expression = "(", expression, ")";
constant_expression = number | string | bool | indentifier | null;
function_expression_or_group_expression = function_ref_call | function_expression | group_expression;
plain_expression = array_expression
        | object_expression
        | function_expression_or_group_expression
        | constant_expression
        | function_normal_declare
        ;
prefix_expression = {prefix_operator}, plain_expression;
fix_expression = prefix_expression , {suffix_operator};
expression = fix_expression, { operator , expression };
```

> statement 不保证有返回值, 只能独立, 不能内嵌
> expression 有返回值, 可以内嵌

TODO-LIST:

* 非lambda方式的函数定义
* 后缀表达式

## 示例

```javascript
let a = 12;
var b = 23;
let c = a + b;
let fc = () => {
   let a = 12;
   return a;
};
```