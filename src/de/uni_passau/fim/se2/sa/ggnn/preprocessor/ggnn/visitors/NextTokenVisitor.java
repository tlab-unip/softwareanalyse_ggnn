package de.uni_passau.fim.se2.sa.ggnn.preprocessor.ggnn.visitors;

import de.uni_passau.fim.se2.sa.ggnn.ast.model.AstNode;
import de.uni_passau.fim.se2.sa.ggnn.ast.visitor.AstVisitorWithDefaults;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.IdentityWrapper;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.Pair;

import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Visitor for inferring NEXT_TOKEN edges.
 */
public class NextTokenVisitor implements
        AstVisitorWithDefaults<Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>>, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>>> {

    private final IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap;

    public NextTokenVisitor(IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap) {
        this.astNodeMap = astNodeMap;
    }

    // TODO: Implement required visitors
    @Override
    public Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> defaultAction(AstNode node,
            Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        astNodeMap.putIfAbsent(node, IdentityWrapper.of(node));
        for (int i = 0; i < node.children().size() - 1; ++i) {
            var curr = node.children().get(i);
            var next = node.children().get(i + 1);
            arg.add(new Pair<>(astNodeMap.get(curr), astNodeMap.get(next)));
        }
        visitChildren(node, arg);
        return arg;
    }
}
