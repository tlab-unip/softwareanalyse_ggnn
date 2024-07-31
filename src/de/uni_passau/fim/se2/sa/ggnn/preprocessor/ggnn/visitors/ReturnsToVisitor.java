package de.uni_passau.fim.se2.sa.ggnn.preprocessor.ggnn.visitors;

import de.uni_passau.fim.se2.sa.ggnn.ast.model.AstNode;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.declaration.MethodDeclaration;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.statement.ReturnStmt;
import de.uni_passau.fim.se2.sa.ggnn.ast.visitor.AstVisitorWithDefaults;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.IdentityWrapper;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.Pair;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Visitor for inferring RETURNS_TO edges.
 */
public class ReturnsToVisitor
        implements AstVisitorWithDefaults<Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>>, Void> {

    private final IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap;
    private IdentityWrapper<AstNode> methodDecl;

    public ReturnsToVisitor(IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap) {
        this.astNodeMap = astNodeMap;
    }

    // TODO: Implement required visitors
    @Override
    public Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> visit(MethodDeclaration node, Void arg) {
        methodDecl = astNodeMap.get(node);
        var edges = new HashSet<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>>();
        node.fold().filter(n -> n instanceof ReturnStmt).forEach(stmt -> {
            edges.add(new Pair<>(astNodeMap.get(stmt), methodDecl));
        });
        return edges;
    }
}
