package de.uni_passau.fim.se2.sa.ggnn.preprocessor.ggnn.visitors;

import de.uni_passau.fim.se2.sa.ggnn.ast.model.AstNode;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.identifier.SimpleIdentifier;
import de.uni_passau.fim.se2.sa.ggnn.ast.visitor.AstVisitorWithDefaults;
import de.uni_passau.fim.se2.sa.ggnn.program_graphs.ddg.DataFlowFacts;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.IdentityWrapper;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.Pair;

import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Visitor for inferring LAST_READ edges.
 */
public class LastReadVisitor implements
        AstVisitorWithDefaults<Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>>, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>>> {

    private final IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap;
    private final DataFlowFacts dataFlowFacts;

    public LastReadVisitor(IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap, DataFlowFacts dataFlowFacts) {
        this.astNodeMap = astNodeMap;
        this.dataFlowFacts = dataFlowFacts;
    }

    // TODO: Implement required visitors
    @Override
    public Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> visit(SimpleIdentifier node,
            Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        astNodeMap.putIfAbsent(node, IdentityWrapper.of(node));
        var lastReads = dataFlowFacts.getUseUsePairs();
        if (lastReads != null) {
            for (var lastRead : lastReads) {
                var source = lastRead.a().variable();
                var target = lastRead.b().variable();
                astNodeMap.putIfAbsent(source, IdentityWrapper.of(source));
                astNodeMap.putIfAbsent(target, IdentityWrapper.of(target));
                if (source.equals(node)) {
                    arg.add(new Pair<>(astNodeMap.get(source), astNodeMap.get(target)));
                }
            }
        }
        return arg;
    }
}
