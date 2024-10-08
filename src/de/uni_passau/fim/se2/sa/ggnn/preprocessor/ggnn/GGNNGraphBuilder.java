// SPDX-FileCopyrightText: 2023 Preprocessing Toolbox Contributors
//
// SPDX-License-Identifier: EUPL-1.2

package de.uni_passau.fim.se2.sa.ggnn.preprocessor.ggnn;

import de.uni_passau.fim.se2.sa.ggnn.ast.model.AstNode;
import de.uni_passau.fim.se2.sa.ggnn.ast.model.declaration.MethodDeclaration;
import de.uni_passau.fim.se2.sa.ggnn.preprocessor.ggnn.visitors.GGNNEdgesVisitor;
import de.uni_passau.fim.se2.sa.ggnn.program_graphs.Builder;
import de.uni_passau.fim.se2.sa.ggnn.util.functional.IdentityWrapper;

import java.util.Set;

import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

/**
 * Builder of the GGNN Graph.
 */
public class GGNNGraphBuilder implements Builder<MethodDeclaration, GGNNGraph> {

    /**
     * Builds the GGNN graph by first defining all GGNN-Edges using the
     * {@link GGNNEdgesVisitor}.
     * Given the GGNN-Edges the GGNN graph is built by adding all required nodes and
     * linking them accordingly.
     *
     * @param method based on which the GGNN will be built.
     * @return the GGNN graph.
     */
    @Override
    public GGNNGraph build(final MethodDeclaration method) {
        // TODO Implement me
        var visitor = new GGNNEdgesVisitor(method);
        var edgeTypeSetMap = visitor.getEdges();
        var graph = new DefaultDirectedGraph<IdentityWrapper<AstNode>, DefaultEdge>(DefaultEdge.class);
        for (var edges : edgeTypeSetMap.values()) {
            for (var edge : edges) {
                graph.addVertex(edge.a());
                graph.addVertex(edge.b());
                graph.addEdge(edge.a(), edge.b());
            }
        }
        var ggnnGraph = new GGNNGraph(method.name().name(), getLabelNodes(method), graph, edgeTypeSetMap);
        return ggnnGraph;
    }

    private Set<IdentityWrapper<AstNode>> getLabelNodes(final MethodDeclaration method) {
        return Set.of(IdentityWrapper.of(method.name()));
    }
}
