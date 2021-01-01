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
    HASH? (IDENTIFIER | DOT)+ (NEWLINE+ | <EOF>);

listValue:
    (HYPHEN singleValue)+;

qualifiedName:
    IDENTIFIER (DOT IDENTIFIER)*;

MODEL: 'Modelname';
NAMESPACE: 'Namespace';

COLON: ':';
SEMICOLON: ';';
HYPHEN: '-';
DOT: '.';
OPENING_BRACE: '(';
CLOSING_BRACE: ')';
HASH: '#';
GREATER_THAN: '>';
NEWLINE: [\n];
WS: [ \r\t]+ -> skip;

IDENTIFIER: [a-zA-Z0-9_]+;
