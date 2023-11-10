package Crux;
class AstPrinter implements Expressions.Visitor<String> {
  String print(Expressions expr) {
    return expr.accept(this);
  }
  @Override
  public String visitBinaryExpr(Expressions.Binary expr) {
    return parenthesize(expr.operator.lexeme,
            expr.left, expr.right);
  }
  @Override
  public String visitGroupingExpr(Expressions.Grouping expr) {
    return parenthesize("group", expr.expr);
  }

  public String visitLiteralExpr(Expressions.Literal expr) {
    if (expr.value == null) return "nil";
    return expr.value.toString();
  }
  @Override
  public String visitUnaryExpr(Expressions.Unary expr) {
    return parenthesize(expr.operator.lexeme, expr.right);
  }

  @Override
  public String visitLiteral(Expressions.Literal expr) {
    return null;
  }

  //write for remaining ones also..
  private String parenthesize(String name, Expressions... exprs) {
    StringBuilder builder = new StringBuilder();
    builder.append("(").append(name);
    for (Expressions expr : exprs) {
      builder.append(" ");
      builder.append(expr.accept(this));
    }
    builder.append(")");
    return builder.toString();
  }
  public static void main(String[] args) {
    Expressions expression = new Expressions.Binary(
            new Expressions.Unary(
                    new Token(Tokentype.MINUS, "-", null, 1),
                    new Expressions.Literal(123)),
            new Token(Tokentype.STAR, "*", null, 1),
            new Expressions.Grouping(
                    new Expressions.Literal(45.67)));
    System.out.println(new AstPrinter().print(expression));
  }
}

