// SPDX-FileCopyrightText: 2023 Preprocessing Toolbox Contributors
//
// SPDX-License-Identifier: EUPL-1.2

package de.uni_passau.fim.se2.sa.ggnn.ast.model.expression.pattern;

import de.uni_passau.fim.se2.sa.ggnn.ast.model.AstNode;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.expression.Expression;
import de.uni_passau.fim.se2.sa.ggnn.ast.visitor.AstVisitor;
import de.uni_passau.fim.se2.sa.ggnn.ast.visitor.Visitable;

import java.util.ArrayList;
import java.util.List;

public record GuardPattern(BasePattern guard, List<Expression> guardedConditions) implements Pattern, Visitable {

    @Override
    public <R, A> R accept(AstVisitor<R, A> visitor, A arg) {
        return visitor.visit(this, arg);
    }

    @Override
    public List<AstNode> children() {
        final List<AstNode> children = new ArrayList<>();
        children.add(guard);
        children.addAll(guardedConditions);
        return children;
    }
}
