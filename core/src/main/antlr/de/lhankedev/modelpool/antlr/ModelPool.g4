grammar ModelPool;

@header {
package de.lhankedev.modelpool.antlr;
}

modeldefinition:
    modelDeclaration
    namespace?
    objectDefinition*;

modelDeclaration:
    MODEL COLON modelName NEWLINE+;

modelName:
    IDENTIFIER;

namespace:
    NAMESPACE COLON qualifiedName NEWLINE+;

objectDefinition:
    (objectParent GREATER_THAN)? qualifiedName (OPENING_BRACE objectId CLOSING_BRACE)? (NEWLINE+ | COLON NEWLINE+ attributeDefinition+)?;

objectParent:
    (IDENTIFIER DOT)? IDENTIFIER;

objectId:
    IDENTIFIER;

attributeDefinition:
    HYPHEN attributeName COLON attributeValue;

attributeName:
    IDENTIFIER;

attributeValue:
    singleValue
    | (NEWLINE+ listValue);

singleValue:
    terminalValue
    | reference
    | placeholder;

terminalValue:
    (IDENTIFIER | DOT)+ (NEWLINE+ | <EOF>);

reference:
    HASH IDENTIFIER (NEWLINE+ | <EOF>);

placeholder:
    DOLLAR OPENING_CURLY_BRACE IDENTIFIER CLOSING_CURLY_BRACE (NEWLINE+ | <EOF>);

listValue:
    (HYPHEN singleValue)+;

qualifiedName:
    IDENTIFIER (DOT IDENTIFIER)*;

MODEL: 'Modelname';
NAMESPACE: 'Namespace';
DOLLAR: '$';
COLON: ':';
SEMICOLON: ';';
HYPHEN: '-';
DOT: '.';
OPENING_BRACE: '(';
CLOSING_BRACE: ')';
OPENING_CURLY_BRACE: '{';
CLOSING_CURLY_BRACE: '}';
HASH: '#';
GREATER_THAN: '>';
NEWLINE: [\n];
WS: [ \r\t]+ -> skip;

IDENTIFIER: [a-zA-Z0-9_]+;
