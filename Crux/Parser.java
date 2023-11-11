package Crux;

import java.util.List;

// You should remove this line because the package name should match the folder structure.
// package Interpreter_Scala.Crux;


class Parser {
    private static class ParseError extends RuntimeException {};
    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    private Expr Expression() {
        return equality();
    }

    Expr parse() {
        try {
            return Expression();
        } catch (ParseError error) {
            return null;
        }
    }

    private Expr equality() {
        Expr expressions = comparison();
        while (match(Tokentype.BANG_EQUAL, Tokentype.EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparison();
            expressions = new Expr.Binary(expressions, operator, right);
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
    private Token consume(Tokentype type, String message) {
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
        return  tokens.get(current - 1);
    }
  //  private ParseError error(Token token, String message) {
//Lox.error(token, message);
//return new ParseError();
//}
        private void synchronize() {
        advance();
        while (!isAtEnd()) {
            if (previous().type == Tokentype.NEXTLINE) return;
        }
        switch (peek().type) {

            case IF:
            case ELSE:
            case FOR:
            case CLASS:
            case WHILE:
            case RETURN:
            case VAR:
            case PRINT:
            case AND:
            case OR:
            case TRAIT:
            case VAL:
            case CASE:
                return;
        }
        advance();
    }
//}

    private Expr comparison() {
        Expr expressions = term();
        while (match(Tokentype.GREATER, Tokentype.GREATER_EQUAL, Tokentype.LESS, Tokentype.LESS_EQUAL)) {
            Token operator = previous();
            Expr right = factor();
            expressions = new Expr.Binary(expressions, operator, right);
        }
        return expressions;
    }

    private Expr term() {
        Expr expressions = factor();
        while (match(Tokentype.MINUS, Tokentype.PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expressions = new Expr.Binary(expressions, operator, right);
        }
        return expressions;
    }

    private Expr factor() {
        Expr expressions = unary();
        while (match(Tokentype.SLASH, Tokentype.STAR)) {
            Token operator = previous();
            Expr right = unary();
            expressions = new Expr.Binary(expressions, operator, right);
        }
        return expressions;
    }

    private Expr unary() {
        if (match(Tokentype.BANG, Tokentype.MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }
        return primary();
    }

    private Expr primary() {

if (match(Tokentype.FALSE)) return new Expr.Literal(false);
if (match(Tokentype.TRUE)) return new Expr.Literal(true);
if (match(Tokentype.NIL)) return new Expr.Literal(null);
if (match(Tokentype.DOUBLE, Tokentype.IDENTIFIER)) {
 return new Expr.Literal(previous().literal);}


        if (match(Tokentype.LEFT_PAREN)) {
            Expr expressions = Expression();
            consume(Tokentype.RIGHT_PAREN, "Expect ')' after expression");
            return new Expr.Grouping(expressions);
        }
        throw error(peek(), "Expect expression");
    }
//    private Token consume(Tokentype type, String message) {
//        if (check(type)) return advance();
//        throw error(peek(), message);
//    }


    private ParseError error(Token token, String message) {
        Scala.error(token, message); // Assuming 'lox' is a valid reference.
        return new ParseError();
    }}


