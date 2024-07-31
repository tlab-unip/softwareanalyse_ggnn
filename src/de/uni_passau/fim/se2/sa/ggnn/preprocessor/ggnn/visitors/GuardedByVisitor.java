package de.uni_passau.fim.se2.sa.ggnn.preprocessor.ggnn.visitors;

import de.uni_passau.fim.se2.sa.ggnn.ast.model.AstNode;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.expression.TernaryExpr;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.statement.DoWhileStmt;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.statement.ForStmt;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.statement.IfStmt;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.statement.WhileStmt;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.statement.try_statement.CatchClause;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.switch_node.Switch.SwitchStmt;
import de.uni_passau.fim.se2.sa.ggnn.ast.visitor.AstVisitorWithDefaults;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.IdentityWrapper;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.Pair;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Set;

/**
 * Visitor for inferring GUARDED_BY edges.
 */
public class GuardedByVisitor
        implements AstVisitorWithDefaults<Void, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>>> {

    private final IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap;

    public GuardedByVisitor(IdentityHashMap<AstNode, IdentityWrapper<AstNode>> astNodeMap) {
        this.astNodeMap = astNodeMap;
    }

    // TODO: Implement required visitors
    @Override
    public Void visit(IfStmt node, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        var guard = node.condition();
        var guardVariables = guard.accept(new VariableTokenVisitor(), new HashSet<>());

        var thenVariables = node.thenStmt().accept(new VariableTokenVisitor(), new HashSet<>());
        for (var variable : thenVariables) {
            if (guardVariables.contains(variable)) {
                arg.add(new Pair<>(astNodeMap.get(variable), astNodeMap.get(guard)));
            }
        }

        if (node.elseStmt().isPresent()) {
            var elseVariables = node.elseStmt().get().accept(new VariableTokenVisitor(), new HashSet<>());
            for (var variable : elseVariables) {
                if (guardVariables.contains(variable)) {
                    arg.add(new Pair<>(astNodeMap.get(variable), astNodeMap.get(guard)));
                }
            }
        }

        return defaultAction(node, arg);
    }

    @Override
    public Void visit(ForStmt node, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        var guard = node.forControl();
        var guardVariables = guard.accept(new VariableTokenVisitor(), new HashSet<>());

        var blockVariables = node.statementBlock().accept(new VariableTokenVisitor(), new HashSet<>());
        for (var variable : blockVariables) {
            if (guardVariables.contains(variable)) {
                arg.add(new Pair<>(astNodeMap.get(variable), astNodeMap.get(guard)));
            }
        }

        return defaultAction(node, arg);
    }

    @Override
    public Void visit(WhileStmt node, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        var guard = node.condition();
        var guardVariables = guard.accept(new VariableTokenVisitor(), new HashSet<>());

        var blockVariables = node.block().accept(new VariableTokenVisitor(), new HashSet<>());
        for (var variable : blockVariables) {
            if (guardVariables.contains(variable)) {
                arg.add(new Pair<>(astNodeMap.get(variable), astNodeMap.get(guard)));
            }
        }

        return defaultAction(node, arg);
    }

    @Override
    public Void visit(DoWhileStmt node, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        var guard = node.condition();
        var guardVariables = guard.accept(new VariableTokenVisitor(), new HashSet<>());

        var blockVariables = node.block().accept(new VariableTokenVisitor(), new HashSet<>());
        for (var variable : blockVariables) {
            if (guardVariables.contains(variable)) {
                arg.add(new Pair<>(astNodeMap.get(variable), astNodeMap.get(guard)));
            }
        }

        return defaultAction(node, arg);
    }

    @Override
    public Void visit(CatchClause node, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        var guardVariable = node.identifier();

        var blockVariables = node.catchBlock().accept(new VariableTokenVisitor(), new HashSet<>());
        for (var variable : blockVariables) {
            if (variable.equals(guardVariable)) {
                arg.add(new Pair<>(astNodeMap.get(variable), astNodeMap.get(node)));
            }
        }

        return defaultAction(node, arg);
    }

    @Override
    public Void visit(TernaryExpr node, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        var guard = node.testExpr();
        var guardVariables = guard.accept(new VariableTokenVisitor(), new HashSet<>());

        var thenVariables = node.thenExpr().accept(new VariableTokenVisitor(), new HashSet<>());
        for (var variable : thenVariables) {
            if (guardVariables.contains(variable)) {
                arg.add(new Pair<>(astNodeMap.get(variable), astNodeMap.get(guard)));
            }
        }

        var elseVariables = node.elseExpr().accept(new VariableTokenVisitor(), new HashSet<>());
        for (var variable : elseVariables) {
            if (guardVariables.contains(variable)) {
                arg.add(new Pair<>(astNodeMap.get(variable), astNodeMap.get(guard)));
            }
        }

        return defaultAction(node, arg);
    }

    @Override
    public Void visit(SwitchStmt node, Set<Pair<IdentityWrapper<AstNode>, IdentityWrapper<AstNode>>> arg) {
        var guard = node.check();
        var guardVariables = guard.accept(new VariableTokenVisitor(), new HashSet<>());

        for (var switchCase : node.cases()) {
            var switchCaseVariables = switchCase.block().accept(new VariableTokenVisitor(), new HashSet<>());
            for (var variable : switchCaseVariables) {
                if (guardVariables.contains(variable)) {
                    arg.add(new Pair<>(astNodeMap.get(variable), astNodeMap.get(guard)));
                }
            }
        }

        return defaultAction(node, arg);
    }
}
