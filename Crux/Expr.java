package Crux;

import java.util.List;

abstract class Expr {
 interface Visitor<R> {
 R visitBinaryExpr(Binary expr);
 R visitGroupingExpr(Grouping expr);
 R visitLiteralExpr(Literal expr);
 R visitUnaryExpr(Unary expr);
 R visitVariableExpr(Variable expr);
 R visitAssignExpr(Assign expr);
 }
 static class Binary extends Expr {
 Binary(Expr left, Crux.Token operator, Expr right) {
 this.left = left;
 this.operator = operator;
 this.right = right;
 }

 @Override
 <R> R accept(Visitor<R> visitor) {
 return visitor.visitBinaryExpr(this);
 }

 final Expr left;
 final Crux.Token operator;
 final Expr right;
 }
 static class Grouping extends Expr {
 Grouping(Expr expression) {
 this.expression = expression;
 }

 @Override
 <R> R accept(Visitor<R> visitor) {
 return visitor.visitGroupingExpr(this);
 }

 final Expr expression;
 }
 static class Literal extends Expr {
 Literal(Object value) {
 this.value = value;
 }

 @Override
 <R> R accept(Visitor<R> visitor) {
 return visitor.visitLiteralExpr(this);
 }

 final Object value;
 }
 static class Unary extends Expr {
 Unary(Crux.Token operator, Expr right) {
 this.operator = operator;
 this.right = right;
 }

 @Override
 <R> R accept(Visitor<R> visitor) {
 return visitor.visitUnaryExpr(this);
 }

 final Crux.Token operator;
 final Expr right;
 }
 static class Variable extends Expr {
 Variable(Crux.Token name) {
 this.name = name;
 }

 @Override
 <R> R accept(Visitor<R> visitor) {
 return visitor.visitVariableExpr(this);
 }

 final Crux.Token name;
 }
 static class Assign extends Expr {
 Assign(Crux.Token name, Expr value) {
 this.name = name;
 this.value = value;
 }

 @Override
 <R> R accept(Visitor<R> visitor) {
 return visitor.visitAssignExpr(this);
 }

 final Crux.Token name;
 final Expr value;
 }

 abstract <R> R accept(Visitor<R> visitor);
}
