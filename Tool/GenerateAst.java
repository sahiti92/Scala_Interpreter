package Tool;

import java.util.*;
import java.io.*;
import java.io.IOException;
//commit
public class GenerateAst {
    public static void main(String[] args) throws IOException {
    //if (args.length != 1) {
       // System.err.println("Usage: generate_ast <output directory>");
       // System.exit(64);
    //}
       // String outputDir = "C:\\Users\\ikshitha j\\Interpreter_Scala\\Crux";
    String outputDir = "C:\\Users\\DHANANJAY V\\IdeaProjects\\Interpreter_Scala\\Crux";
   defineAst(outputDir, "Expr", Arrays.asList(
            "Binary : Expr left, Crux.Token operator, Expr right",
            "Grouping : Expr expression",
            "Literal : Object value",
           "Logical : Expr left, Crux.Token operator, Expr right",
            "Unary : Crux.Token operator, Expr right",
            "Variable : Crux.Token name",
         "Assign : Crux.Token name, Expr value"
//        "Call : Expr callee, Crux.Token paren, List<Expr> arguments",
//        "Get : Expr object, Crux.Token name",
//        "Set : Expr object, Crux.Token name, Expr value",


));
defineAst(outputDir, "Stmt", Arrays.asList(
        "Block : List<Stmt> statements",
        "Expression : Expr expression",
        "If : Expr condition, Stmt thenBranch," + " Stmt elseBranch",
        //        "Conditional : Expr condition, Expr thenBranch, Expr elseBranch"/earlier gpt
        "Print : Expr expression",
        "Var : Token name, Expr initializer",
        //need to change..not so sure
        "Val : Token name, Expr initializer",
        "While : Expr condition, Stmt body"
        ));
    }


private static void defineAst(String outputDir, String baseName, List<String> types)
            throws IOException {
               
String path = outputDir + "/" + baseName + ".java";
PrintWriter writer = new PrintWriter(path, "UTF-8");
writer.println("package Crux;");
writer.println();
writer.println("import java.util.List;");
writer.println();
writer.println("abstract class " + baseName + " {");
defineVisitor(writer, baseName, types);               
        for (String type : types) {
String className = type.split(":")[0].trim();
String fields = type.split(":")[1].trim();
defineType(writer, baseName, className, fields);}
writer.println();
writer.println(" abstract <R> R accept(Visitor<R> visitor);");
writer.println("}");
writer.close();

        // Generate AST classes as before...

    }
    private static void defineVisitor(
PrintWriter writer, String baseName, List<String> types) {
writer.println(" interface Visitor<R> {");
for (String type : types) {
String typeName = type.split(":")[0].trim();
writer.println(" R visit" + typeName + baseName + "(" +
typeName + " " + baseName.toLowerCase() + ");");
}
writer.println(" }");
}

    private static void defineType(
            PrintWriter writer, String baseName,
 String className, String fieldList) {
 writer.println(" static class " + className + " extends " +
 baseName + " {");
 // Constructor.
 writer.println(" " + className + "(" + fieldList + ") {");
 // Store parameters in fields.
 String[] fields = fieldList.split(", ");
 for (String field : fields) {
 String name = field.split(" ")[1];
 writer.println(" this." + name + " = " + name + ";");
 }
 writer.println(" }");
writer.println();
writer.println(" @Override");
writer.println(" <R> R accept(Visitor<R> visitor) {");
writer.println(" return visitor.visit" +
className + baseName + "(this);");
writer.println(" }");
 // Fields.
 writer.println();
 for (String field : fields) {
writer.println(" final " + field + ";");
 }
 writer.println(" }");
 }
    }

