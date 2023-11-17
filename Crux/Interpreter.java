package Crux;
import java.util.List;
//class Interpreter implements Expr.Visitor<Object>{
    class Interpreter implements Expr.Visitor<Object>,Stmt.Visitor<Void> {

        private Environment environment = new Environment();

    @Override
    public Object visitBinaryExpr(Expr.Binary expr) {



        Object right = evaluate(expr.right);
        Object left = evaluate(expr.left);
        System.out.println(left.getClass());
        System.out.println(right.getClass());
        switch (expr.operator.type) {
            case MINUS -> {
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left - (int) right;
                }
                return ((Number) left).doubleValue() - ((Number) right).doubleValue();
            }
            // case PLUS: return (double) left + (double) right;
            case STAR -> {
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left * (int) right;
                }
                return ((Number) left).doubleValue() * ((Number) right).doubleValue();
            }
//            case SLASH -> {
//                checkNumberOperands(expr.operator, left, right);
//                return ;
//            }
            case SLASH -> {
                checkNumberOperands(expr.operator, left, right);
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left / (int) right;
                }
                return ((Number) left).doubleValue() / ((Number) right).doubleValue();
            }
            case PLUS -> {//when 2 var passed


                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left + (int) right;
                }
                if (left instanceof Double && right instanceof Integer) {
                    return (double) left + (int) right;
                }
                if (left instanceof Integer && right instanceof Double) {
                    return (int) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                } else {
                    throw new RuntimeError(expr.operator, "Operands must be two numbers or two strings.");
                }
            }
            case BANG_EQUAL -> {
                return !isEqual(left, right);
            }
            case EQUAL_EQUAL -> {
                return isEqual(left, right);
            }
            case GREATER -> {
//                checkNumberOperands(expr.operator, left, right);
//                return (double) left > (double) right;
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left > (int) right;
                }
                return ((Number) left).doubleValue() > ((Number) right).doubleValue();
            }
            case GREATER_EQUAL -> {
//                checkNumberOperands(expr.operator, left, right);
//                return (double) left >= (double) right;
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left >= (int) right;
                }
                return ((Number) left).doubleValue() >= ((Number) right).doubleValue();
            }
            case LESS -> {
//                checkNumberOperands(expr.operator, left, right);
//                return (double) left < (double) right;
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left < (int) right;
                }
                return ((Number) left).doubleValue() <= ((Number) right).doubleValue();
            }
            case LESS_EQUAL -> {
//                checkNumberOperands(expr.operator, left, right);
//                return (double) left <= (double) right;
                if (left instanceof Integer && right instanceof Integer) {
                    return (int) left <= (int) right;
                }
                return ((Number) left).doubleValue() <=((Number) right).doubleValue();}
        }
        return null;
    }


    private void checkNumberOperands(Token operator, Object left, Object right) {
        if ((left instanceof Number || left instanceof Character) && (right instanceof Number || right instanceof Character)) {
            return;
        }
        throw new RuntimeError(operator, "Both operands must be numbers");
    }
    private boolean isEqual(Object left, Object right) {
        if(left == null && right == null) return true;
        if(left == null) return false;
        return left.equals(right);
    }

    @Override
    public Object visitGroupingExpr(Expr.Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitUnaryExpr(Expr.Unary expr) {
        Object right = evaluate(expr.right);
        switch (expr.operator.type){
            case MINUS :
                checkNumberOperand1(expr.operator, right);
                return -(double)right;
            // case BANG: return !isTruth(right);
            case BANG: return !isTruth(right);
        }
        return null;
    }

//    @Override
//    public Object visitVariableExpr(Expr.Variable expr) {
//        return null;
//    }
//
//    @Override
//    public Object visitAssignExpr(Expr.Assign expr) {
//        return null;
//    }
//
//    @Override
//    public Object visitCallExpr(Expr.Call expr) {
//        return null;
//    }
//
//    @Override
//    public Object visitGetExpr(Expr.Get expr) {
//        return null;
//    }
//
//    @Override
//    public Object visitSetExpr(Expr.Set expr) {
//        return null;
//    }
//
//    @Override
//    public Object visitLogicalExpr(Expr.Logical expr) {
//        return null;
//    }
//
//    @Override
//    public Object visitConditionalExpr(Expr.Conditional expr) {
//        return null;
//    }

    private void checkNumberOperand1(Token operator, Object operand) {
        if (operand instanceof Double) return;
        throw new RuntimeError(operator, "Operand must be a number.");
    }
    //in scala there is no concept of truthy or falsey values. Only Boolean type contains truth values.so updated methods accordingly.

    private boolean isTruth(Object object) {
        if (object == null) return false;
        if(object instanceof Boolean) return (boolean) object;
        //true /false check
        return true;
    }

    @Override
    public Object visitLiteralExpr(Expr.Literal expr) {
        return expr.value;
    }
    @Override
    public Object visitLogicalExpr(Expr.Logical expr) {
        Object left = evaluate(expr.left);
        if (expr.operator.type == Tokentype.OR) {
            if (isTruth(left)) return left;
        } else {
            if (!isTruth(left)) return left;
        }
        return evaluate(expr.right);
    }
    private Object evaluate(Expr expr){
        return expr.accept(this);
    }
    private void execute(Stmt stmt) {
        stmt.accept(this);
    }
    void executeBlock(List<Stmt> statements,
                      Environment environment) {
        Environment previous = this.environment;
        try {
            this.environment = environment;
            for (Stmt statement : statements) {
                execute(statement);
            }
        } finally {
            this.environment = previous;
        }
    }
    @Override
    public Void visitBlockStmt(Stmt.Block stmt) {
        executeBlock(stmt.statements, new Environment(environment));
        return null;
    }
    @Override
    public Void visitExpressionStmt(Stmt.Expression stmt) {
        evaluate(stmt.expression);
        return null;
    }
    @Override
    public Void visitIfStmt(Stmt.If stmt) {
        if (isTruth(evaluate(stmt.condition))) {
            execute(stmt.thenBranch);
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch);
        }
        return null;
    }
    @Override
    public Void visitPrintStmt(Stmt.Print stmt) {
        Object value = evaluate(stmt.expression);
        System.out.println(stringify(value));
        return null;
    }
    @Override
    public Void visitVarStmt(Stmt.Var stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, value);
    //        System.out.println(value.toString());
        return null;
}
    @Override
    public Void visitValStmt(Stmt.Val stmt) {
        Object value = null;
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer);
        }
        environment.define(stmt.name.lexeme, value);
//        System.out.println(value.toString());
        return null;
    }
    @Override
    public Void visitWhileStmt(Stmt.While stmt) {
      //  System.out.println(isTruth(evaluate(stmt.condition)));
        while (isTruth(evaluate(stmt.condition))) {
            execute(stmt.body);
        }
        return null;
    }
    @Override
    public Object visitAssignExpr(Expr.Assign expr) {
        Object value = evaluate(expr.value);
        environment.assign(expr.name, value);
        return value;
    }
    @Override
    public Object visitVariableExpr(Expr.Variable expr) {
//        System.out.println(environment.get(expr.name));
        return environment.get(expr.name);
    }
    //    void interpret(Expr expression){
//       try {
//           Object value = evaluate(expression);
//           System.out.println(Stringify(value));
//       }catch (RuntimeError Error){
//           Scala.runtimeError(Error);
//       }
//
//    }
    void interpret(List<Stmt> statements) {
        try {
            for (Stmt statement : statements) {
                execute(statement);
            }
        } catch (RuntimeError error) {
            Scala.runtimeError(error);
        }
    }

    private String stringify(Object value) {
        if (value == null) return "null";
//        if(value instanceof Double){
        String text = value.toString();
//            if(text.endsWith(".0")){
//                text = text.substring(0,text.indexOf('.')-1);
        //check diff from tb
//text = text.substring(0, text.length() - 2);
//            }
//            return text;
//        }
        return value.toString();
    }
}







