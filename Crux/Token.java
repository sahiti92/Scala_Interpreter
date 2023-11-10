package Crux;

enum Tokentype{
    //Single Character tokens
    LEFT_PAREN,RIGHT_PAREN,LEFT_BRACE,RIGHT_BRACE,COMMA,DOT,MINUS,PLUS,SEMICOLON,SLASH,STAR,

    // one or two character tokens
    BANG,BANG_EQUAL,EQUAL,EQUAL_EQUAL,GREATER,GREATER_EQUAL,LESS,LESS_EQUAL,GENERATOR,NEXTLINE,
    //Literals
    IDENTIFIER,STRING,NUMBER,

    //Keywords
    AND,CLASS,ELSE,FALSE,FOR,IF,NIL,OR,PRINT,RETURN,SUPER,THIS,TRUE,VAR,WHILE,TRAIT,YIELD,MATCH,OBJECT,DEF,WITH,VAL,DO,CASE,TYPE,NEW,TRY,CATCH,FINALLY,THROW,EXTENDS,IMPORT,PACKAGE,
     //match tokens from lexer file 
    EOF
}


public class Token {
    final Tokentype type;
    final String lexeme;
    final Object literal;
    final int line;

    Token(Tokentype type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
