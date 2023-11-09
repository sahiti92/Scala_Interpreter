package Crux;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
class Lexer {
    private final String sources;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    public Lexer(String sources) {
        this.sources = sources;
    }

    List<Token> scanTokens(){
        while (!isAtEnd()){
            start = current;
            scanToken();
        }
        tokens.add(new Token(Tokentype.EOF,"",null,line));
        return tokens;
    }
    private boolean isAtEnd(){
        return current >= sources.length();
    }
    private void scanToken(){
        char c = advance();
        switch (c){
            case '(': addToken(Tokentype.LEFT_PAREN); break;
            case ')': addToken(Tokentype.RIGHT_PAREN); break;
            case '{':addToken(Tokentype.LEFT_BRACE);break;
            case '}':addToken(Tokentype.RIGHT_BRACE);break;
            case ',':addToken(Tokentype.COMMA);break;
            case '.':addToken(Tokentype.DOT);break;
            case '-':addToken(Tokentype.MINUS);break;
            case '+':addToken(Tokentype.PLUS);break;
            case '\n':addToken(Tokentype.NEXTLINE);break;
                 case '\n':addToken(Tokentype.NEXTLINE);
                line++;
                break;
                
//            case '/':addToken(Tokentype.SLASH);break;
            case '/':
                if (match('/')) {
// A comment goes until the end of the line.
                    while (peek() != '\n' && !isAtEnd()) advance();
                } else {
                    addToken(Tokentype.SLASH);
                }
                break;
            case '*':addToken(Tokentype.STAR);break;
            case '!':
                addToken(match('=') ? Tokentype.BANG_EQUAL : Tokentype.BANG);
                break;
            case '=':
                addToken(match('=') ? Tokentype.EQUAL_EQUAL : Tokentype.EQUAL);
                break;
            case '<':
                if(match('=')){addToken(Tokentype.LESS_EQUAL);}
                if(match('-')){addToken(Tokentype.GENERATOR);}
                else {addToken(Tokentype.LESS);}

                break;
            case '>':
                addToken(match('=') ? Tokentype.GREATER_EQUAL : Tokentype.GREATER);
                break;
            case ' ':
            case '\r':
            case '\t':
// Ignore whitespace.
                break;
            case '"': string(); break;
            default:
                if (isDigit(c)) {
                    number();}
                else if (isAlpha(c)) {
                        identifier();
                    }
                else {
                    Scala.error(line, "Unexpected character.");
                }
                break;

        }
    }
    private void identifier() {
        while (isAlphaNumeric(peek())) advance();
        addToken(Tokentype.IDENTIFIER);
    }
    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') ||
                (c >= 'A' && c <= 'Z') ||
                c == '_';
    }
    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }
    private void number() {
        while (isDigit(peek())) advance();
// Look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
// Consume the "."
            advance();
            while (isDigit(peek())) advance();
        }
        addToken(Tokentype.NUMBER,
                Double.parseDouble(sources.substring(start, current)));
    }
    private char peekNext() {
        if (current + 1 >= sources.length()) return '\0';
        return sources.charAt(current + 1);
    }
    private boolean match(char expected) {
        if (isAtEnd()) return false;
        if (sources.charAt(current) != expected) return false;
        current++;
        return true;
    }

    private char advance(){
        current++;
        return sources.charAt(current-1);
    }
    private void addToken(Tokentype type){
        addToken(type,null);
    }
    private void addToken(Tokentype type, Object literal){
        String text = sources.substring(start,current);
        tokens.add(new Token(type,text,literal,line));
    }
    private void string(){
        while (peek() != '"' && !isAtEnd()){
            if(peek() == '\n') line++;
            advance();
        }
        if(isAtEnd()){
            Scala.error(line,"Unterminated String");
            return;
        }
        advance();
        String value = sources.substring(start+1,current-1);
        addToken(Tokentype.STRING,value);
    }
    private char peek(){
        if (isAtEnd()) return '\0';
        return sources.charAt(current);
    }
    private static final HashMap<String, Tokentype> keywords;
    static {
        keywords = new HashMap<>();
//      keywords.put("and", Tokentype.AND);
        keywords.put("class", Tokentype.CLASS);
        keywords.put("else", Tokentype.ELSE);
        keywords.put("false", Tokentype.FALSE);
        keywords.put("for", Tokentype.FOR);
//      keywords.put("fun", FUN);
        keywords.put("if", Tokentype.IF);
        keywords.put("nil", Tokentype.NIL);
//      keywords.put("or", Tokentype.OR);
        keywords.put("println", Tokentype.PRINT);
        keywords.put("return", Tokentype.RETURN);
        keywords.put("super", Tokentype.SUPER);
        keywords.put("this", Tokentype.THIS);
        keywords.put("true", Tokentype.TRUE);
        keywords.put("var", Tokentype.VAR);
        keywords.put("while", Tokentype.WHILE);
        keywords.put("trait", Tokentype.TRAIT);
        keywords.put("def", Tokentype.DEF);
        keywords.put("val", Tokentype.VAL);
        keywords.put("with", Tokentype.WITH);
        keywords.put("object", Tokentype.OBJECT);
        keywords.put("do", Tokentype.DO);
        keywords.put("match", Tokentype.MATCH);
        keywords.put("case", Tokentype.CASE);
        keywords.put("type", Tokentype.TYPE);
        keywords.put("new", Tokentype.NEW);
        keywords.put("try", Tokentype.TRY);
        keywords.put("catch", Tokentype.CATCH);
        keywords.put("finally", Tokentype.FINALLY);
        keywords.put("throw", Tokentype.THROW);
        keywords.put("extends", Tokentype.EXTENDS);
        keywords.put("with", Tokentype.WITH);
        keywords.put("import", Tokentype.IMPORT);
        keywords.put("package", Tokentype.PACKAGE);
    }
}
