package Crux;

abstract class Expressions {

    interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitUnaryExpr(Unary expr);
        R visitLiteral(Literal expr);
    }
    static class Binary extends Expressions{
        Binary(Expressions left, Token operator, Expressions right){
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
        final Expressions left;
        final Token operator;
        final Expressions right;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return null;
        }
    }
    static class Unary extends Expressions{
        Unary(Token operator,Expressions right){
            this.operator = operator;
            this.right = right;
        }
        final Token operator;
        final Expressions right;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return null;
        }
    }
    static class Grouping extends Expressions{
        Grouping(Expressions expr){
            this.expr = expr;
        }
        final Expressions expr;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return null;
        }
    }
    static class Literal extends Expressions{
        Literal(Object value){
            this.value = value;
        }
        final Object value;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return visitor.visitLiteral(this);
        }
    }
    abstract <R> R accept(Visitor<R> visitor);
}

//eppr file
