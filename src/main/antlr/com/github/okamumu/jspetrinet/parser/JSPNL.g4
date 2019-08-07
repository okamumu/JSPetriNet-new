grammar JSPNL;

@header {
package com.github.okamumu.jspetrinet.parser;
}

prog
    :	((statement)? NEWLINE)*
    ;

statement
    : declaration
    | simple
    ;

simple_block
    : (NEWLINE)* '{' (simple)? (NEWLINE (simple)?)* '}'
    ;

// declaration

declaration
    : node_declaration
    | arc_declaration
    | assert_declaration
    ;

node_declaration
    : node='place' id=ID ('(' option_list ')')?
    | node='trans' id=ID ('(' option_list ')')? (simple_block)?
    | node=ID id=ID ('(' option_list ')')? (simple_block)?
    ;

arc_declaration
    : type=('arc'|'iarc'|'oarc'|'harc') srcName=ID 'to' destName=ID ('(' option_list ')')?
    ;

assert_declaration
    : 'assert' (expression | simple_block)
    ;

// option

option_list
    : option_value (',' option_list)*
    ;

option_value
    : assign_expression
    ;

// simple

simple
    : assign_expression
    | expression
    ;

// assign

assign_expression returns [int type]
    : id=ID '=' expression { $type = 1; }
    | id=ID ':=' expression { $type = 2; }
    | ntoken_expression '=' expression { $type = 3; }
    ;

// expression

expression returns [int type]
    : op=('!'|'+'|'-') expression { $type = 1; }
    | expression op=('*'|'/'|'div'|'mod') expression { $type = 2; }
    | expression op=('+'|'-') expression { $type = 3; }
    | expression op=('<'|'<='|'>'|'>=') expression { $type = 4; }
    | expression op=('=='|'!=') expression { $type = 5; }
    | expression op='&&' expression { $type = 6; }
    | expression op='||' expression { $type = 7; }
    | op='ifelse' '(' expression ',' expression ',' expression ')' { $type = 8; }
    | function_expression { $type = 9; }
    | ntoken_expression { $type = 10; }
    | enable_expression { $type = 14; }
    | literal_expression { $type = 11; }
    | id=ID { $type = 12; }
    |	'(' expression ')' { $type = 13; }
    ;

// function_expression

function_expression
    : id=ID '(' arg_list ')'
    ;

// arg

arg_list
    : arg_value (',' arg_list)*
    ;

arg_value
    : val=expression
    ;

// ntoken

ntoken_expression
    : '#' id=ID
    ;

// enable

enable_expression
    : '?' id=ID
    ;

// literal

literal_expression returns [int type]
    : val=INT      { $type = 1; }
    | val=FLOAT    { $type = 2; }
    | val=LOGICAL  { $type = 3; }
    | val=STRING   { $type = 4; }
    ;

// TOKENS

LOGICAL: TRUE | FALSE ;

ID: CHAR (DIGIT+ | CHAR+ | '.')* ;

INT: DIGIT+ ;

FLOAT
    : DIGIT+ '.' (DIGIT+)? (EXPONENT)?
    | '.' (DIGIT+)? (EXPONENT)?
    | DIGIT+ EXPONENT
    ;

STRING : '"' ( ESCAPED_QUOTE | ~('\n'|'\r') )*? '"';

NEWLINE : [\r\n;EOF]+ ;

WS      : [ \t]+ -> skip ;

LINE_COMMENT
    : '//' ~[\r\n]* -> channel(HIDDEN)
    ;

BLOCK_COMMENT
    : '/*' .*? '*/' -> channel(HIDDEN)
    ;

fragment
DIGIT: [0-9];

fragment
EXPONENT: [eE] ('+'|'-')? (DIGIT+)? ;

fragment
CHAR    : [a-zA-Z_] ;

fragment
TRUE    : [Tt][Rr][Uu][Ee] ;

fragment
FALSE   : [Ff][Aa][Ll][Ss][Ee] ;

fragment ESCAPED_QUOTE : '\\"';
