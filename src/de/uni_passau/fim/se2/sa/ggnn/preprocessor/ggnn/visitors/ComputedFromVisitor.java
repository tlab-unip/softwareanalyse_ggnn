package de.uni_passau.fim.se2.sa.ggnn.preprocessor.ggnn.visitors;

import de.uni_passau.fim.se2.sa.ggnn.ast.model.AstNode;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.declaration.VariableDeclaration;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.expression.binary.AssignmentExpr;
import de.uni_passau.fim.se2.sa.ggnn.ast.visitor.AstVisitorWithDefaults;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.IdentityWrapper;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.Pair;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Visitor for inferring COMPUTED_FROM edges.
 */
public class ComputedFromVisitor implements
        AstVisitorWithDefaults<Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>>, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>>> {

    private final IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap;

    public ComputedFromVisitor(IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap) {
        this.astNodeMap = astNodeMap;
    }

    // TODO: Implement required visitors
    @Override
    public Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> visit(AssignmentExpr node,
            Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        astNodeMap.putIfAbsent(node, IdentityWrapper.of(node));
        var leftVariables = node.left().accept(new VariableTokenVisitor(), new HashSet<>());
        var rightVariables = node.right().accept(new VariableTokenVisitor(), new HashSet<>());
        for (var left : leftVariables) {
            astNodeMap.putIfAbsent(left, IdentityWrapper.of(left));
            for (var right : rightVariables) {
                astNodeMap.putIfAbsent(right, IdentityWrapper.of(right));
                arg.add(new Pair<>(astNodeMap.get(left), astNodeMap.get(right)));
            }
        }

        visitChildren(node, arg);
        return arg;
    }

    @Override
    public Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> visit(VariableDeclaration node,
            Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        astNodeMap.putIfAbsent(node, IdentityWrapper.of(node));
        if (node.initializer().isPresent()) {
            var left = node.identifier();
            astNodeMap.putIfAbsent(left, IdentityWrapper.of(left));
            var rightVariables = node.initializer().get().accept(new VariableTokenVisitor(), new HashSet<>());
            for (var right : rightVariables) {
                astNodeMap.putIfAbsent(right, IdentityWrapper.of(right));
                arg.add(new Pair<>(astNodeMap.get(left), astNodeMap.get(right)));
            }
        }

        visitChildren(node, arg);
        return arg;
    }
}
