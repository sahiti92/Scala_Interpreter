package Crux;

import java.util.List;

// You should remove this line because the package name should match the folder structure.
// package Interpreter_Scala.Crux;

import java.sql.Statement;
import java.util.List;
import java.util.ArrayList;

class Parser {
    private static class ParseError extends RuntimeException {};
    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Expressions Expression() {
        return equality();
    }

    Expressions parse() {
        try {
            return Expression();
        } catch (ParseError error) {
            return null;
        }
    }

    private Expressions equality() {
        Expressions expressions = comparison();
        while (match(Tokentype.BANG_EQUAL, Tokentype.EQUAL_EQUAL)) {
            Token operator = previous();
            Expressions right = comparison();
            expressions = new Expressions.Binary(expressions, operator, right);
        }
        return expressions;
    }

    private boolean match(Tokentype... types) {
        for (Tokentype type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }
        return false;
    }
    private Token consume(TokenType type, String message) {
if (check(type)) return advance();
throw error(peek(), message);
}

    private boolean check(Tokentype type) {
        if (isAtEnd()) return false;
        return peek().type == type;
    }

    private Token peek() {
        return tokens.get(current);
    }

    private boolean isAtEnd() {
        return peek().type == Tokentype.EOF;
    }

    private Token advance() {
        if (!isAtEnd()) current++;
        return previous();
    }

    private Token previous() {
        return tokens.get(current - 1);
    }
  //  private ParseError error(Token token, String message) {
//Lox.error(token, message);
//return new ParseError();
//}
    //    private void synchronize() {
//        advance();
//        while (!isAtEnd()) {
//            if (previous().type == Tokentype.SEMICOLON) return;
//        }
//        switch (peek().type) {
//            case Tokentype.IF:
//            case Tokentype.ELSE:
//            case Tokentype.FOR:
//            case Tokentype.FUN:
//            case Tokentype.CLASS:
//            case Tokentype.WHILE:
//            case Tokentype.RETURN:
//            case Tokentype.VAR:
//            case Tokentype.PRINT:
//                return;
//        }
//        advance();
//    }
//}

    private Expressions comparison() {
        Expressions expressions = term();
        while (match(Tokentype.GREATER, Tokentype.GREATER_EQUAL, Tokentype.LESS, Tokentype.LESS_EQUAL)) {
            Token operator = previous();
            Expressions right = factor();
            expressions = new Expressions.Binary(expressions, operator, right);
        }
        return expressions;
    }

    private Expressions term() {
        Expressions expressions = factor();
        while (match(Tokentype.MINUS, Tokentype.PLUS)) {
            Token operator = previous();
            Expressions right = factor();
            expressions = new Expressions.Binary(expressions, operator, right);
        }
        return expressions;
    }

    private Expressions factor() {
        Expressions expressions = unary();
        while (match(Tokentype.SLASH, Tokentype.STAR)) {
            Token operator = previous();
            Expressions right = unary();
            expressions = new Expressions.Binary(expressions, operator, right);
        }
        return expressions;
    }

    private Expressions unary() {
        if (match(Tokentype.BANG, Tokentype.MINUS)) {
            Token operator = previous();
            Expressions right = unary();
            return new Expressions.Unary(operator, right);
        }
        return primary();
    }

    private Expressions primary() {
        private Expr primary() {
//if (match(FALSE)) return new Expr.Literal(false);
//if (match(TRUE)) return new Expr.Literal(true);
//if (match(NIL)) return new Expr.Literal(null);
//if (match(NUMBER, STRING)) {
//return new Expr.Literal(previous().literal);}
        if (match(Tokentype.LEFT_PAREN)) {
            Expressions expressions = Expression();
            consume(Tokentype.RIGHT_PAREN, "Expect ')' after expression");
            return new Expressions.Grouping(expressions);
        }
        throw error(peek(), "Expect expression");
    }

    private Token consume(Tokentype type, String message) {
        if (check(type)) return advance();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Scala.error(token, message); // Assuming 'lox' is a valid reference.
        return new ParseError();
    }


