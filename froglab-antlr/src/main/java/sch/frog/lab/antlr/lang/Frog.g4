grammar Frog;

@header {
package sch.frog.lab.antlr.lang;
}

program: statements? EOF; // rule start
statements: (statement)+;
statement: statement_block
          | if_statement
          | iteration_statement
          | function_statement
          | try_catch_finally
          | return_statement SEMICOLON
          | break_statement SEMICOLON
          | continue_statement SEMICOLON
          | variable_statement SEMICOLON
          | package_statement SEMICOLON
          | import_statement SEMICOLON
          | throw_statement SEMICOLON
          | function_call SEMICOLON
          | expression SEMICOLON
          ;
statement_block: LBRACE statements? RBRACE;
return_statement: RETURN expression?;
break_statement: BREAK;
continue_statement: CONTINUE;
variable_body: IDENTIFIER (ASSIGN expression)?;
variable_statement: GLOBAL_DECLARE variable_body (COMMA variable_body)?
          | LOCAL_DECLARE variable_body (COMMA variable_body)?;
package_statement: PACKAGE IDENTIFIER (DOT IDENTIFIER)*;
import_statement: IMPORT IDENTIFIER (DOT IDENTIFIER)*;

nest_statement: statement_block | statement;

catch_argument: LPAREN IDENTIFIER RPAREN;
catch_part: CATCH catch_argument? statement_block;
finally_part: FINALLY statement_block;
try_catch_finally: TRY statement_block catch_part? finally_part?;
throw_statement: THROW expression;

if_entry: IF LPAREN expression RPAREN nest_statement;
else_if_entry: ELSE if_entry;
else_entry: ELSE nest_statement;
if_statement: if_entry else_if_entry* else_entry?;

iteration_statement: do_while_statement
        | while_statement
        | for_statement;

for_initializer: variable_statement | variable_body;
for_statement: FOR LPAREN for_initializer? SEMICOLON expression_list? SEMICOLON expression_list? RPAREN nest_statement;

while_statement: WHILE LPAREN expression RPAREN nest_statement;

do_while_statement: DO nest_statement WHILE LPAREN expression RPAREN;


expression_list: expression (COMMA expression)*;
array_expression: array_obj (array_caller | object_caller)?;
object_expression: object_obj object_caller?;
function_expression: function_declare function_caller?;
function_ref_call: IDENTIFIER function_caller;
group_expression: LPAREN expression RPAREN;
constant_expression: NUMBER | STRING | BOOL | IDENTIFIER | NULL;
function_expression_or_group_expression: function_ref_call | function_expression | group_expression;
plain_expression: constant_expression
        | array_expression
        | object_expression
        | function_expression_or_group_expression
        | function_normal_declare
        ;
fix_expression: (MINUS | PLUS | BANG)? plain_expression SUFFIX_OPERATOR?;
expression: expression (STAR | SLASH) expression
        | expression (PLUS | MINUS) expression
        | expression (EQ | NOT_EQ | GT | GTE | LT | LTE) expression
        | expression (OR | AND | SHORT_CIRCLE_OR | SHORT_CIRCLE_AND) expression
        | expression (PLUS_ASSIGN | MINUS_ASSIGN | STAR_ASSIGN | SLASH_ASSIGN) expression
        | <assoc = right> IDENTIFIER ASSIGN expression
        | fix_expression;

caller: object_caller | array_index | function_caller;

formal_parameter_list: IDENTIFIER (COMMA IDENTIFIER)*;
function_declare: LPAREN formal_parameter_list? RPAREN ARROW statement_block;
function_caller: LPAREN expression_list? RPAREN caller?;
function_direct_call: function_declare function_caller;
function_indentifier_call: IDENTIFIER function_caller;
function_call: function_indentifier_call | function_direct_call;
function_normal_declare: FUNCTION LPAREN formal_parameter_list? RPAREN statement_block;
function_statement: FUNCTION IDENTIFIER LPAREN formal_parameter_list? RPAREN statement_block;

entry_key: IDENTIFIER | STRING;
entry: entry_key COLON expression;
entry_list: entry (COMMA entry)*;
object: LBRACE entry_list? RBRACE;
object_caller: DOT IDENTIFIER  caller?;
object_obj: IDENTIFIER | object;

array: LBRACKET expression_list? RBRACKET;
array_index: LBRACKET expression RBRACKET;
array_caller: array_index caller?;
array_obj: IDENTIFIER | array;

DO: 'do';
WHILE: 'while';
FOR: 'for';
IF: 'if';
ELSE: 'else';
BOOL : 'true' | 'false';
NULL : 'null';
FUNCTION: 'function';
THROW: 'throw';
TRY: 'try';
CATCH: 'catch';
FINALLY: 'finally';
IMPORT: 'import';
PACKAGE: 'package';
CONTINUE: 'continue';
BREAK: 'break';
RETURN: 'return';
GLOBAL_DECLARE: 'var';
LOCAL_DECLARE: 'let';
LBRACKET: '[';
RBRACKET: ']';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
COMMA: ',';
DOT: '.';
COLON: ':';
SEMICOLON: ';';
ARROW: '=>';
SUFFIX_OPERATOR: '++' | '--';
PLUS: '+';
MINUS: '-';
BANG: '!';
STAR: '*';
SLASH: '/';
ASSIGN: '=';
OR: '|';
AND: '&';
SHORT_CIRCLE_OR: '||';
SHORT_CIRCLE_AND: '&&';
EQ: '==';
NOT_EQ: '!=';
GT: '>';
LT: '<';
GTE: '>=';
LTE: '<=';
PLUS_ASSIGN: '+=';
MINUS_ASSIGN: '-=';
STAR_ASSIGN: '*=';
SLASH_ASSIGN: '/=';
DECIMAL_PART: DOT DIGIT* ('_' DIGIT+)?; //// "_"后面是无限循环部分
NUMBER: NO_ZERO_DIGIT DIGIT* DECIMAL_PART?
     | '0' DECIMAL_PART?;
IDENTIFIER: CHARACTER (CHARACTER | DIGIT | '_')*;
STRING: '"' (~["\\] | '\\' .)* '"';
WS: [ \t\r\n]+ -> skip; // 忽略换行符等
COMMENT : ('//' .*? '\n' | '/*' .*? '*/') -> skip; // 忽略注释
NO_ZERO_DIGIT: [1-9];
DIGIT: [0-9];
CHARACTER: [a-zA-Z];