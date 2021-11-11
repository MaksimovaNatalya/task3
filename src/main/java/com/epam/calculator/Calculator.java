package com.epam.calculator;

import java.util.ArrayList;
import java.util.List;


public class Calculator {
    /*------------------------------------------------------------------
     * PARSER RULES
     *------------------------------------------------------------------*/

//    expr : plusminus* EOF ;
//
//    plusminus: multdiv ( ( '+' | '-' ) multdiv )* ;
//
//    multdiv : factor ( ( '*' | '/' ) factor )* ;
//
//    factor : NUMBER | '(' expr ')' ;

    public int calculateExpression(String expression) {
        List<Lexeme> lexemes = lexAnalyze(expression);
        LexemeBuffer lexemeBuffer = new LexemeBuffer(lexemes);
        return getExpressionInBrackets(lexemeBuffer);
    }

    private static List<Lexeme> lexAnalyze(String expression) {
        ArrayList<Lexeme> lexemes = new ArrayList<>();
        int position = 0;
        while (position < expression.length()) {
            char character = expression.charAt(position);
            switch (character) {
                case '(':
                    lexemes.add(Lexeme.makeALexeme(LexemeType.LEFT_BRACKET, character));
                    position++;
                    continue;
                case ')':
                    lexemes.add(Lexeme.makeALexeme(LexemeType.RIGHT_BRACKET, character));
                    position++;
                    continue;
                case '+':
                    lexemes.add(Lexeme.makeALexeme(LexemeType.OP_PLUS, character));
                    position++;
                    continue;
                case '-':
                    lexemes.add(Lexeme.makeALexeme(LexemeType.OP_MINUS, character));
                    position++;
                    continue;
                case '*':
                    lexemes.add(Lexeme.makeALexeme(LexemeType.OP_MUL, character));
                    position++;
                    continue;
                case '/':
                    lexemes.add(Lexeme.makeALexeme(LexemeType.OP_DIV, character));
                    position++;
                    continue;
                default:
                    if (character <= '9' && character >= '0') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(character);
                            position++;
                            if (position >= expression.length()) {
                                break;
                            }
                            character = expression.charAt(position);
                        } while (character <= '9' && character >= '0');
                        lexemes.add(Lexeme.makeALexeme(LexemeType.NUMBER, sb.toString()));
                    } else {
                        if (character != ' ') {
                            throw new RuntimeException("Unexpected character: " + character);
                        }
                        position++;
                    }
            }
        }
        lexemes.add(Lexeme.makeALexeme(LexemeType.EOF, ""));
        return lexemes;
    }

    private static int getExpressionInBrackets(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        if (lexeme.getType() == LexemeType.EOF) {
            return 0;
        } else {
            lexemes.back();
            return getPlusMinusResult(lexemes);
        }
    }

    private static int getPlusMinusResult(LexemeBuffer lexemes) {
        int value = getMulDivResult(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.getType()) {
                case OP_PLUS:
                    value += getMulDivResult(lexemes);
                    break;
                case OP_MINUS:
                    value -= getMulDivResult(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                    lexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.getValue()
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    private static int getMulDivResult(LexemeBuffer lexemes) {
        int value = getFactor(lexemes);
        while (true) {
            Lexeme lexeme = lexemes.next();
            switch (lexeme.getType()) {
                case OP_MUL:
                    value *= getFactor(lexemes);
                    break;
                case OP_DIV:
                    value /= getFactor(lexemes);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case OP_PLUS:
                case OP_MINUS:
                    lexemes.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + lexeme.getValue()
                            + " at position: " + lexemes.getPos());
            }
        }
    }

    private static int getFactor(LexemeBuffer lexemes) {
        Lexeme lexeme = lexemes.next();
        switch (lexeme.getType()) {
            case NUMBER:
                return Integer.parseInt(lexeme.getValue());
            case LEFT_BRACKET:
                int value = getPlusMinusResult(lexemes);
                lexeme = lexemes.next();
                if (lexeme.getType() != LexemeType.RIGHT_BRACKET) {
                    throw new RuntimeException("Unexpected token: " + lexeme.getValue()
                            + " at position: " + lexemes.getPos());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + lexeme.getValue()
                        + " at position: " + lexemes.getPos());
        }
    }

}
