package com.epam.calculator;

import java.util.List;

 class LexemeBuffer {
    private int pos;

    int getPos() {
        return pos;
    }

    private final List<Lexeme> lexemes;

    LexemeBuffer(List<Lexeme> lexemes) {
        this.lexemes = lexemes;
    }

    Lexeme next() {
        return lexemes.get(pos++);
    }

    void back() {
        pos--;
    }
}