package Crux;

abstract class Expr {
    interface Visitor<R> {
        R visitBinaryExpr(Binary expr);
        R visitGroupingExpr(Grouping expr);
        R visitUnaryExpr(Unary expr);
        R visitLiteral(Literal expr);}
    //        "Variable : Crux.Token name",
//            "Assign : Crux.Token name, Expr value",
//            "Call : Expr callee, Crux.Token paren, List<Expr> arguments",
//            "Get : Expr object, Crux.Token name",
//            "Set : Expr object, Crux.Token name, Expr value",
//            "Logical : Expr left, Crux.Token operator, Expr right",
//            "Conditional : Expr condition, Expr thenBranch, Expr elseBranch"


    static class Binary extends Expr{
        Binary(Expr left,Token operator,Expr right){
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
        final Expr left;
        final Token operator;
        final Expr right;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return null;
        }
    }
    static class Unary extends Expr{
        Unary(Token operator,Expr right){
            this.operator = operator;
            this.right = right;
        }
        final Token operator;
        final Expr right;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return null;
        }
    }
    static class Grouping extends Expr{
        Grouping(Expr expr){
            this.expr = expr;
        }
        final Expr expr;

        @Override
        <R> R accept(Visitor<R> visitor) {
            return null;
        }
    }
    static class Literal extends Expr{
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
