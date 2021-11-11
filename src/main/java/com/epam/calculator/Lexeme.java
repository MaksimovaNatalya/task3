package com.epam.calculator;

class Lexeme {
    private final LexemeType type;

    LexemeType getType() {
        return type;
    }

    private final String value;

    String getValue() {
        return value;
    }

    private Lexeme(LexemeType type, String value) {
        this.type = type;
        this.value = value;
    }

    static Lexeme makeALexeme (LexemeType type, String value) {
        return  new Lexeme(type, value);
    }

    private Lexeme(LexemeType type, Character value) {
        this.type = type;
        this.value = value.toString();
    }

    static Lexeme makeALexeme (LexemeType type, Character value) {
        return  new Lexeme(type, value);
    }
}