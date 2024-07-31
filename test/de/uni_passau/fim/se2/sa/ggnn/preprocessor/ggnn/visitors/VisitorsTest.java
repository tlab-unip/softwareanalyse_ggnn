package de.uni_passau.fim.se2.sa.ggnn.preprocessor.ggnn.visitors;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import de.uni_passau.fim.se2.sa.ggnn.ast.parser.AstCodeParser;
import de.uni_passau.fim.se2.sa.ggnn.preprocessor.shared.MethodsExtractor;

public class VisitorsTest {
    String sourceCode = "public class Ternary {" +
            "int ternary(int x, int y) {" +
            "return x >= y ? x : y;}}";

    @Test
    public void GGNNEdgesVisitorTest() throws Exception {
        var parser = new AstCodeParser();
        var root = parser.parseCodeToCompilationUnit(sourceCode);
        var methodDecls = new MethodsExtractor(false).process(root);
        assertEquals(methodDecls.size(), 1);

        var visitor = new GGNNEdgesVisitor(methodDecls.getFirst());
        var edges = visitor.getEdges();
        assertEquals(edges.keySet().size(), 7);
    }
}
