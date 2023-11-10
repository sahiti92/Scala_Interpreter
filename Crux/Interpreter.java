package com.craftinginterpreters.lox;

class Interpreter implements Expr.Visitor<Object>{
    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {
        Object right = evaluate(expr.right);
        Object left = evaluate(expr.left);
        switch (expr.operator.type){
            case MINUS : return (double) left - (double) right;
           // case PLUS: return (double) left + (double) right;
            case STAR: return (double)left * (double) right;
            case SLASH: return (double)left / (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }
                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }
                break;
                
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double)left > (double)right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double)left < (double)right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double)left <= (double)right;
        }
        return null;
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if(left instanceof Double && right instanceof Double) return;
        throw new RuntimeError(operator,"Both Operands must be numbers");
    }

    private boolean isEqual(Object left, Object right) {
        if(left == null && right == null) return true;
        if(left == null) return false;
        return left.equals(right);
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expr);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type){
            case MINUS : return -(double)right;
           // case BANG: return !isTruth(right);
            case BANG: return !(right);  
        }
        return null;
    }
    //in scala there is no concept of truthy or falsey values. Only Boolean type contains truth values.so updated methods accordingly.

  //  private boolean isTruth(Object object) {
     //   if (object == null) return false;
       // if(object instanceof Boolean) return (boolean) object;
       // return true;
   // }

    @Override
    public Object visitLiteral(Expr.Literal expr) {
        return expr.value;
    }
    private Object evaluate(Expr expr){
        return expr.accept(this);
    }
    void interpret(Expr expression){
       try {
           Object value = evaluate(expression);
           System.out.println(Stringify(value));
       }catch (RuntimeError Error){
           lox.runtimeError(Error);
       }

    }

    private String Stringify(Object value) {
        if (value == null) return "null";
        if(value instanceof Double){
            String text = value.toString();
            if(text.endsWith(".0")){
                text = text.substring(0,text.indexOf('.')-1);
            }
            return text;
        }
        return value.toString();
    }
}
