package de.uni_passau.fim.se2.sa.ggnn.preprocessor.ggnn;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import de.uni_passau.fim.se2.sa.ggnn.ast.parser.AstCodeParser;
import de.uni_passau.fim.se2.sa.ggnn.preprocessor.shared.MethodsExtractor;

public class PreprocessorGGNNTest {
    String sourceCodeTernary = "public class Ternary {" +
            "int ternary(int x, int y) {" +
            "return x >= y ? x : y;}}";

    String sourceCodeExtendedExample = "public class ExtendedExample {\r\n" + //
            "    public static void main(final String[] args) {\r\n" + //
            "        int sum = 0;\r\n" + //
            "        for (int i = 1; i < 10; i++) {\r\n" + //
            "            sum = sum + i;\r\n" + //
            "        }\r\n" + //
            "\r\n" + //
            "        while (sum > 0) {\r\n" + //
            "            if (sum % 2 == 0) {\r\n" + //
            "                sum = sum / 2;\r\n" + //
            "            } else {\r\n" + //
            "                sum = sum + 1;\r\n" + //
            "            }\r\n" + //
            "        }\r\n" + //
            "\r\n" + //
            "        switch (sum) {\r\n" + //
            "            case 0:\r\n" + //
            "                System.out.println(\"zero\");\r\n" + //
            "            case 1: {\r\n" + //
            "                final String one = \"one\";\r\n" + //
            "                System.out.println(one);\r\n" + //
            "            }\r\n" + //
            "            default:\r\n" + //
            "                System.out.println(\"DEFAULT\");\r\n" + //
            "        }\r\n" + //
            "\r\n" + //
            "        if (args.length == 0) {\r\n" + //
            "            return;\r\n" + //
            "        }\r\n" + //
            "\r\n" + //
            "        int i = 0;\r\n" + //
            "        do {\r\n" + //
            "            final String arg = args[i];\r\n" + //
            "            System.out.println(arg);\r\n" + //
            "        } while (++i < args.length);\r\n" + //
            "    }\r\n" + //
            "}\r\n" + //
            "";

    @Test
    public void GGNNGraphBuilderTernaryTest() throws Exception {
        var parser = new AstCodeParser();
        var root = parser.parseCodeToCompilationUnit(sourceCodeTernary);
        var methodDecls = new MethodsExtractor(false).process(root);
        assertEquals(methodDecls.size(), 1);

        var builder = new GGNNGraphBuilder();
        var graph = builder.build(methodDecls.getFirst());
        assertEquals(graph.getVertices().size(), 17);
        assertEquals(graph.getEdges().size(), 35);
    }

    @Test
    public void GGNNGraphBuilderExtendedExampleTest() throws Exception {
        var parser = new AstCodeParser();
        var root = parser.parseCodeToCompilationUnit(sourceCodeExtendedExample);
        var methodDecls = new MethodsExtractor(false).process(root);
        assertEquals(methodDecls.size(), 1);

        var builder = new GGNNGraphBuilder();
        var graph = builder.build(methodDecls.getFirst());
        assertEquals(graph.getVertices().size(), 153);
        assertEquals(graph.getEdges().size(), 343);
    }
}