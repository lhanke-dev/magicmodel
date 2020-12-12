grammar Magicmodel;

@header {
package de.lhankedev.magicmodel.antlr;
}

modeldefinition:
    modelDeclaration
    namespace?
    objectDefinition*;

modelDeclaration:
    MODEL COLON modelName SEMICOLON;

modelName:
    IDENTIFIER;

namespace:
    NAMESPACE COLON qualifiedName SEMICOLON;

objectDefinition:
    (objectParent GREATER_THAN)? qualifiedName (OPENING_BRACE objectId CLOSING_BRACE)? (COLON attributeDefinition+);

objectParent:
    IDENTIFIER DOT IDENTIFIER;

objectId:
    IDENTIFIER;

attributeDefinition:
    HYPHEN attributeName COLON attributeValue;

attributeName:
    IDENTIFIER;

attributeValue:
    HASH? IDENTIFIER;

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

WS: [ \r\n\t]+ -> skip;

IDENTIFIER: [a-zA-Z0-9_]+;