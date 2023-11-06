public class GenerateAst {
    private static void defineAst(
            String outputDir, String baseName, List<String> types)
            throws IOException {
        // Generate AST classes as before...
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
 // Fields.
 writer.println();
 for (String field : fields) {
writer.println(" final " + field + ";");
 }
 writer.println(" }");
 }
    }
}
