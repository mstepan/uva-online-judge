package graph;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class GraphTraversal {

    private GraphTraversal() {
        throw new AssertionError(GraphTraversal.class.getCanonicalName() + " should be used in static way only.");
    }

    enum VertexState {
        UNVISITED,
        MARKED,
        VISITED
    }

    public static List<String> dfsIterative(UndirectedGraph graph, String startVertex) {
        final List<String> traversalTree = new ArrayList<>();
        traversalTree.add(startVertex);

        Map<String, String> parent = new HashMap<>();
        parent.put(startVertex, startVertex);

        Map<String, VertexState> traversalState = new HashMap<>();
        traversalState.put(startVertex, VertexState.MARKED);

        Deque<String> stack = new ArrayDeque<>();
        stack.push(startVertex);

        MAIN:
        while (!stack.isEmpty()) {
            String curVer = stack.pop();

            for (String adj : graph.adjacent(curVer)) {
                VertexState adjState = traversalState.getOrDefault(adj, VertexState.UNVISITED);

                if (adjState == VertexState.UNVISITED) {

                    traversalTree.add(adj);

                    stack.push(curVer);

                    traversalState.put(adj, VertexState.MARKED);
                    stack.push(adj);

                    parent.put(adj, curVer);

                    continue MAIN;
                }

                if (adjState == VertexState.MARKED && !parent.get(curVer).equals(adj)) {

                    // back edge found, so we have cycle
                    System.out.printf("Back edge detected: %s-%s%n", curVer, adj);

                    List<String> cycle = findVertexesFromCycle(adj, curVer, stack);

                    System.out.printf("cycle: %s%n", cycle);
                }
            }

            traversalState.put(curVer, VertexState.VISITED);
        }

        return traversalTree;
    }

    private static List<String> findVertexesFromCycle(String last, String preLast, Deque<String> stack) {
        List<String> cycle = new ArrayList<>();
        cycle.add(last);
        cycle.add(preLast);

        for (String nextVerFromCycle : stack) {
            cycle.add(nextVerFromCycle);

            if (nextVerFromCycle.equals(last)) {
                break;
            }
        }
        return cycle;
    }

    public static void main(String[] args) {
        UndirectedGraph graph = UndirectedGraph.parse("1-3,1-2,2-3,2-4");

        List<String> dfsTree = GraphTraversal.dfsIterative(graph, "2");

        System.out.println(dfsTree);

    }

}
